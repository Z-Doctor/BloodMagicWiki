package embedded.igwmod.gui;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.block.BlockDemonStairsBase;
import WayofTime.bloodmagic.block.BlockMimic;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.item.block.ItemBlockBloodTank;
import WayofTime.bloodmagic.item.block.ItemBlockDemonCrystal;
import WayofTime.bloodmagic.item.block.base.ItemBlockEnum;
import embedded.igwmod.ConfigHandler;
import embedded.igwmod.InfoSupplier;
import embedded.igwmod.TickHandler;
import embedded.igwmod.WikiUtils;
import embedded.igwmod.api.ItemWikiEvent;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.tabs.IWikiTab;
import embedded.igwmod.lib.Textures;
import embedded.igwmod.lib.Util;
import embedded.igwmod.lib.WikiLog;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.client.ClientProxy;
import zdoctor.bmw.wiki.events.BlockWikiEvent;
import zdoctor.bmw.wiki.events.EntityWikiEvent;
import zdoctor.bmw.wiki.events.PageChangeEvent;

/**
 * Derived from Vanilla's GuiContainerCreative
 */

public class GuiWiki extends GuiContainer {
	private static String currentFile = "";
	// The raw info directly retrieved from the .txt file.
	private static List<String> fileInfo = new ArrayList<String>();
	// A list of all the tabs registered.
	public static List<IWikiTab> wikiTabs = new ArrayList<IWikiTab>();

	private static IWikiTab currentTab;
	private static int currentTabPage = 0;
	private static String currentModIdPage = ModMain.MODID;

	private static List<IPageLink> visibleWikiPages = new ArrayList<IPageLink>();
	private static int matchingWikiPages;

	private static final List<LocatedStack> locatedStacks = new ArrayList<LocatedStack>();
	private static final List<LocatedString> locatedStrings = new ArrayList<LocatedString>();
	private static final List<IWidget> locatedTextures = new ArrayList<IWidget>();

	private static final ResourceLocation scrollbarTexture = new ResourceLocation(
			"textures/gui/container/creative_inventory/tabs.png");

	private static float currentPageLinkScroll;
	private static float currentPageScroll;
	private static int currentPageTranslation;

	/** True if the scrollbar is being dragged */
	private boolean isScrollingPageLink;
	private boolean isScrollingPage;
	private boolean wasClicking;
	private int lastMouseX;
	private int oldGuiScale;

	private GuiButton previousButton, nextButton;

	private static GuiTextField searchField;

	private static final int PAGE_LINK_SCROLL_X = 80;
	private static final int PAGE_LINK_SCROLL_HEIGHT = 214;
	private static final int PAGE_LINK_SCROLL_Y = 14;

	private static final int PAGE_SCROLL_X = 240;
	private static final int PAGE_SCROLL_HEIGHT = 230;
	private static final int PAGE_SCROLL_Y = 4;

	public static final double TEXT_SCALE = 0.5D;

	public static final int MAX_TEXT_Y = 453;
	public static final int MIN_TEXT_Y = 10;

	private static boolean clickDecbounce = false;

	public GuiWiki() {
		super(new ContainerBlockWiki());

		allowUserInput = true;
		ySize = 238;
		xSize = 256;
		if (currentTab == null)
			currentTab = wikiTabs.get(0);
	}

	public static String getCurrentFile() {
		return currentFile;
	}

	@Override
	public void initGui() {
		if (mc.gameSettings.guiScale != 0) {
			oldGuiScale = mc.gameSettings.guiScale;
			mc.gameSettings.guiScale = 0;
			mc.displayGuiScreen(this);
		} else {
			super.initGui();
			buttonList.clear();
			Keyboard.enableRepeatEvents(true);

			String lastSearch = "";
			if (searchField != null)
				lastSearch = searchField.getText();
			searchField = new GuiTextField(0, fontRendererObj, guiLeft + 40,
					guiTop + currentTab.getSearchBarAndScrollStartY(), 53, fontRendererObj.FONT_HEIGHT);
			searchField.setMaxStringLength(15);
			searchField.setEnableBackgroundDrawing(true);
			searchField.setVisible(true);
			searchField.setFocused(false);
			searchField.setCanLoseFocus(true);
			searchField.setText(lastSearch);
			updateSearch();

			previousButton = new GuiButton(0, guiLeft + 40, guiTop + 4, 25, 10, "<--");
			nextButton = new GuiButton(1, guiLeft + 68, guiTop + 4, 25, 10, "-->");
			previousButton.enabled = BrowseHistory.canGoPrevious();
			nextButton.enabled = BrowseHistory.canGoNext();
			buttonList.add(previousButton);
			buttonList.add(nextButton);
		}
	}

	public FontRenderer getFontRenderer() {
		return fontRendererObj;
	}

	public int getGuiLeft() {
		return guiLeft;
	}

	public int getGuiTop() {
		return guiTop;
	}

	@Override
	public void actionPerformed(GuiButton button) {
		BrowseHistory history;
		if (button.id == 0) {
			history = BrowseHistory.previous();
		} else {
			history = BrowseHistory.next();
		}
		currentFile = history.link;
		currentTab = history.tab;
		currentTabPage = getPageNumberForTab(currentTab);
		currentTab.onPageChange(this, currentFile, history.meta);
		updateWikiPage(history.meta);
		updateSearch();
		initGui();// update the textfield location.
		currentPageScroll = history.scroll;
		updatePageScrolling();
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat
	 * events
	 */
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		mc.gameSettings.guiScale = oldGuiScale;
	}

	@Override
	protected void handleMouseClick(Slot slot, int slotId, int mouseButton, ClickType type) {
		if (slot != null && slot.getHasStack()) {
			setCurrentFile(slot.getStack());
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button);
		searchField.mouseClicked(x, y, button);
		if (searchField.isFocused() && button == 1) {
			searchField.setText("");
			currentPageLinkScroll = 0;
			updateSearch();
		}

		List<IWikiTab> visibleTabs = getVisibleTabs();
		for (int i = 0; i < visibleTabs.size(); i++) {
			if (x <= 33 + guiLeft && x >= 1 + guiLeft && y >= 8 + guiTop + i * 35 && y <= 43 + guiTop + i * 35) {
				currentTab = visibleTabs.get(i);
				currentPageLinkScroll = 0;
				updateSearch();
				initGui();// update the textfield location.
				break;
			}
		}

		for (IPageLink link : visibleWikiPages) {
			if (link.onMouseClick(this, -guiLeft + x, -guiTop + y)) {
				return;
			}
		}
		for (LocatedString link : locatedStrings) {
			if (link.onMouseClick(this, -guiLeft + x, -guiTop + y)) {
				return;
			}
		}

		if (hasMultipleTabPages() && x < 33 + guiLeft && x >= 1 + guiLeft && y >= 214 + guiTop && y <= 236 + guiTop) {
			if (button == 0) {
				if (++currentTabPage >= getTotalTabPages())
					currentTabPage = 0;
			} else if (button == 1) {
				if (--currentTabPage < 0)
					currentTabPage = getTotalTabPages() - 1;
			}
		}
	}

	public void setCurrentFile(World world, BlockPos pos) {
		BlockWikiEvent wikiEvent = new BlockWikiEvent(world, pos);
		if (wikiEvent.drawnStack.getItem() == null)
			return;
		wikiEvent.pageOpened = WikiRegistry.getPageForItemStack(wikiEvent.drawnStack);
		if (wikiEvent.pageOpened == null)
			wikiEvent.pageOpened = wikiEvent.drawnStack.getUnlocalizedName().replace("tile.", "block/").replace("item.",
					"item/");
		MinecraftForge.EVENT_BUS.post(wikiEvent);
		setCurrentFile(wikiEvent.pageOpened, wikiEvent.drawnStack);
	}

	public void setCurrentFile(Entity entity) {
		EntityWikiEvent wikiEvent = new EntityWikiEvent(entity);
		wikiEvent.pageOpened = WikiRegistry.getPageForEntityClass(entity.getClass());
		MinecraftForge.EVENT_BUS.post(wikiEvent);
		setCurrentFile(wikiEvent.pageOpened, entity);
	}

	public void setCurrentFile(ItemStack stack) {

		String defaultName = WikiRegistry.getPageForItemStack(stack);
		if (defaultName == null)
			defaultName = stack.getUnlocalizedName().replace("tile.", "block/").replace("item.", "item/");
		ItemWikiEvent wikiEvent = new ItemWikiEvent(stack, defaultName);
		if (WikiUtils.getOwningModId(stack).equalsIgnoreCase(Constants.Mod.MODID)) {
			MinecraftForge.EVENT_BUS.post(wikiEvent);
			if (stack != null) {
				stack = stack.copy();
				stack.setCount(1);
			}
			setCurrentFile(wikiEvent.pageOpened, stack);
			clickDecbounce = true;
			Thread waitThread = new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(100);
						clickDecbounce = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			waitThread.start();
		}
	}

	public void setCurrentFile(String file, Object... metadata) {
		if (!clickDecbounce) {
			BrowseHistory.updateHistory(currentPageScroll);
			if (metadata.length == 0) {
				ItemStack displayedStack = WikiUtils.getStackFromName(file);
				if (displayedStack != null)
					metadata = new Object[] { displayedStack };
			}
			currentFile = file;
			IWikiTab tab = getTabForPage(currentFile);
			if (tab != null) {
				updateSearch();
				currentTab = tab;
			}
			currentTabPage = getPageNumberForTab(currentTab);
			currentTab.onPageChange(this, file, metadata);
			updateWikiPage(metadata);
			updateSearch();
			BrowseHistory.addHistory(file, currentTab, metadata);
			initGui();// update the textfield location.
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		if (searchField.textboxKeyTyped(par1, par2)) {
			currentPageLinkScroll = 0;
			updateSearch();
		} else {
			if (ClientProxy.openInterfaceKey.getKeyCode() == par2) {
				mc.player.closeScreen();
			} else {
				super.keyTyped(par1, par2);
			}
		}
	}

	private void updateSearch() {
		List<IPageLink> pages = currentTab.getPages(null);// request all pages.
		if (pages != null) {
			List<Integer> matchingIndexes = new ArrayList<Integer>();
			for (int i = 0; i < pages.size(); i++) {
				if (searchField.getText().toLowerCase().equals("")) {
					matchingIndexes.add(i);
				} else if (pages.get(i).getName().toLowerCase().contains(searchField.getText().toLowerCase())
						&& !(pages.get(i) instanceof LocatedSectionString)) {
					matchingIndexes.add(i);
				}
			}
			matchingWikiPages = matchingIndexes.size();
			int firstListedPageIndex = (int) (getScrollStates() * currentPageLinkScroll + 0.5F)
					* currentTab.pagesPerScroll();
			if (firstListedPageIndex < 0)
				firstListedPageIndex = 0;

			int[] indexes = new int[Math.min(
					Math.min(matchingIndexes.size() - firstListedPageIndex, matchingIndexes.size()),
					currentTab.pagesPerTab())];
			for (int i = 0; i < indexes.length; i++) {
				indexes[i] = matchingIndexes.get(firstListedPageIndex + i);
			}
			visibleWikiPages = currentTab.getPages(indexes);
		} else {
			visibleWikiPages = new ArrayList<IPageLink>();
			matchingWikiPages = 0;
		}
		((ContainerBlockWiki) inventorySlots).updateStacks(locatedStacks, visibleWikiPages);
	}

	private void updatePageScrolling() {
		int translation = -(int) (currentPageScroll * getMaxPageTranslation() / 2 + 0.5F) * 2 - currentPageTranslation;
		currentPageTranslation += translation;
		for (LocatedStack stack : locatedStacks) {
			stack.setY(stack.getY() + translation / 2);
		}
		for (LocatedString string : locatedStrings) {
			string.setY(string.getY() + translation);
		}
		for (IWidget image : locatedTextures) {
			image.setY(image.getY() + translation);
		}
		((ContainerBlockWiki) inventorySlots).updateStacks(locatedStacks, visibleWikiPages);
	}

	private int getMaxPageTranslation() {
		int maxTranslation = -100000;
		for (IWidget texture : locatedTextures) {
			maxTranslation = Math.max(maxTranslation, texture.getY() + texture.getHeight());
		}
		for (LocatedString string : locatedStrings) {
			maxTranslation = Math.max(maxTranslation, string.getY() + fontRendererObj.FONT_HEIGHT);
		}
		return Math.max(maxTranslation - currentPageTranslation - MAX_TEXT_Y, 0);
	}

	private boolean needsPageLinkScrollBars() {
		return matchingWikiPages > currentTab.pagesPerTab();
	}

	private boolean needsPageScrollBars() {
		return getMaxPageTranslation() > 0;
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();

		if (i != 0) {
			if (i > 0) {
				i = 1;
			}

			if (i < 0) {
				i = -1;
			}

			if (lastMouseX < PAGE_LINK_SCROLL_X + guiLeft + 14) {
				if (needsPageLinkScrollBars()) {
					int j = getScrollStates();

					currentPageLinkScroll = (float) (currentPageLinkScroll - (double) i / (double) j);

					if (currentPageLinkScroll < 0.0F) {
						currentPageLinkScroll = 0.0F;
					}

					if (currentPageLinkScroll > 1.0F) {
						currentPageLinkScroll = 1.0F;
					}
					updateSearch();
				}
			} else {
				if (needsPageScrollBars()) {
					int maxTranslation = getMaxPageTranslation();
					currentPageScroll -= (float) i / maxTranslation * 40;
					if (currentPageScroll > 1F)
						currentPageScroll = 1F;
					else if (currentPageScroll < 0F)
						currentPageScroll = 0F;
					updatePageScrolling();
				}
			}
		}
	}

	private int getScrollStates() {
		return (1 + matchingWikiPages - currentTab.pagesPerTab()) / currentTab.pagesPerScroll();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		lastMouseX = mouseX;
		boolean leftClicking = Mouse.isButtonDown(0);
		int pageLinkScrollX1 = guiLeft + PAGE_LINK_SCROLL_X;
		int pageLinkScrollY1 = guiTop + PAGE_LINK_SCROLL_Y + currentTab.getSearchBarAndScrollStartY();
		int pageLinkScrollX2 = pageLinkScrollX1 + 14;
		int pageLinkScrollY2 = pageLinkScrollY1 + PAGE_LINK_SCROLL_HEIGHT - currentTab.getSearchBarAndScrollStartY();

		int pageScrollX1 = guiLeft + PAGE_SCROLL_X;
		int pageScrollY1 = guiTop + PAGE_SCROLL_Y;
		int pageScrollX2 = pageScrollX1 + 14;
		int pageScrollY2 = pageScrollY1 + PAGE_SCROLL_HEIGHT;

		if (!wasClicking && leftClicking) {
			if (mouseX >= pageLinkScrollX1 && mouseY >= pageLinkScrollY1 && mouseX < pageLinkScrollX2
					&& mouseY < pageLinkScrollY2) {
				isScrollingPageLink = needsPageLinkScrollBars();
			} else if (mouseX >= pageScrollX1 && mouseY >= pageScrollY1 && mouseX < pageScrollX2
					&& mouseY < pageScrollY2) {
				isScrollingPage = needsPageScrollBars();
			}
		}

		if (!leftClicking) {
			isScrollingPageLink = false;
			isScrollingPage = false;
		}

		wasClicking = leftClicking;

		if (isScrollingPageLink) {
			currentPageLinkScroll = (mouseY - pageLinkScrollY1 - 7.5F) / (pageLinkScrollY2 - pageLinkScrollY1 - 15.0F);

			if (currentPageLinkScroll < 0.0F) {
				currentPageLinkScroll = 0.0F;
			}

			if (currentPageLinkScroll > 1.0F) {
				currentPageLinkScroll = 1.0F;
			}
			updateSearch();
		} else if (isScrollingPage) {
			currentPageScroll = (mouseY - pageScrollY1 - 7.5F) / (pageScrollY2 - pageScrollY1 - 15.0F);

			if (currentPageScroll < 0.0F) {
				currentPageScroll = 0.0F;
			}

			if (currentPageScroll > 1.0F) {
				currentPageScroll = 1.0F;
			}
			updatePageScrolling();
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
		// GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		// GL11.glDisable(GL11.GL_LIGHTING);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		// RenderHelper.enableGUIStandardItemLighting();

		// Draw the wiki tabs.
		mc.getTextureManager().bindTexture(Textures.GUI_WIKI);
		drawTexturedModalRect(guiLeft + 33, guiTop, 33, 0, xSize - 33, ySize);
		List<IWikiTab> visibleTabs = getVisibleTabs();
		for (int i = 0; i < visibleTabs.size(); i++) {
			drawTexturedModalRect(guiLeft, guiTop + 4 + i * 35, 0, currentTab == visibleTabs.get(i) ? 0 : 35, 33, 35);
		}

		// Draw the change tabpage tab.
		if (hasMultipleTabPages()) {
			drawTexturedModalRect(guiLeft, guiTop + 214, 0, 70, 33, 22);
		}

		// draw the pagelink scrollbar
		if (needsPageLinkScrollBars()) {
			drawTexturedModalRect(guiLeft + PAGE_LINK_SCROLL_X - 1,
					guiTop + PAGE_LINK_SCROLL_Y + currentTab.getSearchBarAndScrollStartY() - 1, PAGE_SCROLL_X - 1,
					PAGE_SCROLL_Y - 1, 14, PAGE_LINK_SCROLL_HEIGHT - currentTab.getSearchBarAndScrollStartY() - 1);
			drawTexturedModalRect(guiLeft + PAGE_LINK_SCROLL_X - 1,
					guiTop + PAGE_LINK_SCROLL_Y + PAGE_LINK_SCROLL_HEIGHT - 2, PAGE_SCROLL_X - 1,
					PAGE_SCROLL_Y + PAGE_SCROLL_HEIGHT - 2, 14, 1);
		} else {
			drawVerticalLine(guiLeft + PAGE_LINK_SCROLL_X + 13,
					guiTop + PAGE_LINK_SCROLL_Y + currentTab.getSearchBarAndScrollStartY(),
					PAGE_LINK_SCROLL_HEIGHT + 20, 0xFF888888);
		}

		// Draw the text field.
		searchField.drawTextBox();

		drawWikiPage(mouseX, mouseY);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		GL11.glColor4d(1, 1, 1, 1);
		// Draw the scroll bar widgets.

		mc.getTextureManager().bindTexture(scrollbarTexture);
		if (needsPageLinkScrollBars()) {
			drawTexturedModalRect(PAGE_LINK_SCROLL_X,
					PAGE_LINK_SCROLL_Y + currentTab.getSearchBarAndScrollStartY()
							+ (int) ((PAGE_LINK_SCROLL_HEIGHT - currentTab.getSearchBarAndScrollStartY() - 17)
									* currentPageLinkScroll),
					232 + (needsPageLinkScrollBars() ? 0 : 12), 0, 12, 15);
		}
		drawTexturedModalRect(PAGE_SCROLL_X,
				PAGE_SCROLL_Y + (int) ((PAGE_SCROLL_Y + PAGE_SCROLL_HEIGHT - PAGE_SCROLL_Y - 17) * currentPageScroll),
				232 + (needsPageScrollBars() ? 0 : 12), 0, 12, 15);

		GL11.glEnable(GL11.GL_LIGHTING);

		// draw the tab page browse text if necessary
		if (hasMultipleTabPages()) {
			fontRendererObj.drawString(currentTabPage + 1 + "/" + getTotalTabPages(), 10, 221, 0xFF000000);
		}

		// Draw the wiki page stacks.
		for (LocatedStack locatedStack : locatedStacks) {
			locatedStack.renderBackground(this, mouseX, mouseY);
		}

		// draw the wikipage
		currentTab.renderForeground(this, mouseX, mouseY);

		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft, guiTop, 0);
		for (LocatedString locatedString : locatedStrings) {
			if (locatedString.getY() > MIN_TEXT_Y
					&& locatedString.getReservedSpace().height + locatedString.getY() <= MAX_TEXT_Y) {
				locatedString.renderForeground(this, mouseX, mouseY);
			}
		}
		GL11.glPopMatrix();

		// Draw the wiki page images.
		GL11.glColor4d(1, 1, 1, 1);
		GL11.glPushMatrix();
		GL11.glScaled(TEXT_SCALE, TEXT_SCALE, 1);
		for (IWidget texture : locatedTextures) {
			texture.renderForeground(this, mouseX, mouseY);
		}

		GL11.glPopMatrix();

		// Draw wiki tab images.
		List<IReservedSpace> reservedSpaces = currentTab.getReservedSpaces();
		if (reservedSpaces != null) {
			for (IReservedSpace space : reservedSpaces) {
				if (space instanceof LocatedTexture) {
					((LocatedTexture) space).renderForeground(this, mouseX, mouseY);
				}
			}
		}

		// render the wiki links
		for (IPageLink link : visibleWikiPages) {
			link.renderForeground(this, mouseX, mouseY);
		}

		drawTooltips(mouseX, mouseY);

		// GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		// RenderHelper.disableStandardItemLighting();
		// GL11.glDisable(GL11.GL_LIGHTING);
		// GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private void drawTooltips(int x, int y) {
		GlStateManager.enableLighting();

		List<IWikiTab> visibleTabs = getVisibleTabs();
		for (int i = 0; i < visibleTabs.size(); i++) {
			if (x <= 33 + guiLeft && x >= 1 + guiLeft && y >= 4 + guiTop + i * 35 && y <= 39 + guiTop + i * 35) {
				drawCreativeTabHoveringText(I18n.format(visibleTabs.get(i).getName()), x - guiLeft, y - guiTop);
			}
		}
		if (hasMultipleTabPages() && x < 33 + guiLeft && x >= 1 + guiLeft && y >= 214 + guiTop && y <= 236 + guiTop) {
			drawHoveringText(Arrays.asList(new String[] { I18n.format(ModMain.MODID + ".tooltip.tabPageBrowse.next"),
					I18n.format(ModMain.MODID + ".tooltip.tabPageBrowse.previous") }), x - guiLeft, y - guiTop);
		}
		/*
		 * if(curSection == EnumWikiSection.ENTITIES) { for(int i = 0; i <
		 * shownEntityList.size(); i++) { if(x >= guiLeft + 41 && x <= guiLeft +
		 * 76 && y >= guiTop + 75 + i * 36 && y <= guiTop + 110 + i * 36) {
		 * drawCreativeTabHoveringText(shownEntityList.get(i).getEntityName(), x
		 * - guiLeft, y - guiTop); } } }
		 */
	}

	private void updateWikiPage(Object... metadata) {
		Object o = metadata.length > 0 ? metadata[0] : null;
		ItemStack pageStack = o instanceof ItemStack ? (ItemStack) o : null;
		Entity pageEntity = o instanceof Entity ? (Entity) o : null;
		PageChangeEvent pageChangeEvent = new PageChangeEvent(currentFile, pageStack, pageEntity);
		MinecraftForge.EVENT_BUS.post(pageChangeEvent);
		currentFile = pageChangeEvent.currentFile;
		// System.out.println("Page: " + currentFile);
		fileInfo = pageChangeEvent.pageText;
		if (fileInfo == null) {
			String modid = currentModIdPage;
			if (currentFile.contains(":")) {
				String[] splitted = currentFile.split(":", 2);
				modid = splitted[0];
				System.out.println("Change modid: " + modid);
				currentFile = splitted[1];
			} else {
				if (pageStack != null) {
					modid = WikiUtils.getOwningModId(pageStack);
				} else if (pageEntity != null) {
					modid = Util.getModIdForEntity(pageEntity.getClass());
				} else if (o instanceof String) {
					modid = (String) o;
				} else {
					ItemStack tabItem = currentTab.renderTabIcon(this);
					if (tabItem != null && tabItem.getItem() != null) {
						modid = WikiUtils.getOwningModId(tabItem);
					}
					if (ConfigHandler.debugMode) {
						WikiLog.info("Tracked down the mod owner of the page \"" + currentFile
								+ "\" by getting the mod owner of the tab ItemStack. This is not recommended. Please prefix page links with <modid>:, so for example: pneumaticcraft:menu/baseConcepts");
					}
				}
			}

			currentModIdPage = modid;
			fileInfo = InfoSupplier.getInfo(modid, currentFile, false);
		}
		List<IReservedSpace> reservedSpaces = currentTab.getReservedSpaces();
		if (reservedSpaces == null)
			reservedSpaces = new ArrayList<IReservedSpace>();
		reservedSpaces.add(new ReservedSpace(new Rectangle(0, 0, 200, Integer.MAX_VALUE)));
		InfoSupplier.analyseInfo(fontRendererObj, fileInfo, reservedSpaces, locatedStrings, locatedStacks,
				locatedTextures);
		((ContainerBlockWiki) inventorySlots).updateStacks(locatedStacks, visibleWikiPages);
		currentPageTranslation = 0;
		currentPageScroll = 0;
	}

	private void drawWikiPage(int mouseX, int mouseY) {
		currentTab.renderBackground(this, mouseX, mouseY);
		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft, guiTop, 0);

		// Draw the wiki page images.
		GL11.glColor4d(1, 1, 1, 1);
		GL11.glPushMatrix();
		GL11.glScaled(TEXT_SCALE, TEXT_SCALE, 1);
		for (IWidget texture : locatedTextures) {
			texture.renderBackground(this, mouseX, mouseY);
		}

		GL11.glPopMatrix();

		for (LocatedString locatedString : locatedStrings) {
			if (locatedString.getY() > MIN_TEXT_Y
					&& locatedString.getReservedSpace().height + locatedString.getY() <= MAX_TEXT_Y) {
				locatedString.renderBackground(this, mouseX, mouseY);
			}
		}
		GL11.glColor4d(1, 1, 1, 1);

		// Draw wiki tab images.
		List<IReservedSpace> reservedSpaces = currentTab.getReservedSpaces();
		if (reservedSpaces != null) {
			for (IReservedSpace space : reservedSpaces) {
				if (space instanceof LocatedTexture) {
					((LocatedTexture) space).renderBackground(this, mouseX, mouseY);
				}
			}
		}

		GL11.glPushMatrix();
		GL11.glTranslated(0, 4, 0);
		List<IWikiTab> visibleTabs = getVisibleTabs();

		RenderHelper.enableGUIStandardItemLighting();
		for (IWikiTab tab : visibleTabs) {
			ItemStack drawingStack = tab.renderTabIcon(this);
			if (drawingStack != null) {
				if (drawingStack.getItem() instanceof ItemBlock) {
					renderRotatingBlockIntoGUI(this, drawingStack, 11, 23, 1.5F);
				} else {
					boolean oldSetting = mc.gameSettings.fancyGraphics;
					mc.gameSettings.fancyGraphics = true;
					renderRotatingBlockIntoGUI(this, drawingStack, 12, 20, 1.2F);
					mc.gameSettings.fancyGraphics = oldSetting;
				}
			}
			GL11.glTranslated(0, 35, 0);
		}
		GL11.glPopMatrix();

		// render the wiki links
		for (IPageLink link : visibleWikiPages) {
			link.renderBackground(this, mouseX, mouseY);
		}
		GL11.glPopMatrix();

	}

	private List<IWikiTab> getVisibleTabs() {
		List<IWikiTab> tabs = new ArrayList<IWikiTab>();
		for (int i = currentTabPage * 6; i < currentTabPage * 6 + 6 && i < wikiTabs.size(); i++) {
			tabs.add(wikiTabs.get(i));
		}
		return tabs;
	}

	private IWikiTab getTabForPage(String page) {
		// System.out.println("page: " + page);
		if (page == null)
			return null; // When there isn't a valid page just stay on the same
							// page.
		// page = ItemsWiki.getPageFromName(page);
		if (currentTab != null) {// give the current tab the highest priority.
			System.out.println("Looking for owner of page: " + page);
			List<IPageLink> links = currentTab.getPages(null);
			if (links != null) {
				for (IPageLink link : links) {
					// System.out.println("checked: " + link.getLinkAddress());
					if (page.equals(link.getLinkAddress()))
						return currentTab;
				}
			}
		}
		for (int i = wikiTabs.size() - 1; i >= 0; i--) {
			IWikiTab tab = wikiTabs.get(i);
			List<IPageLink> links = tab.getPages(null);
			if (links != null) {
				for (IPageLink link : links) {
					// System.out.println("checked: " + link.getLinkAddress());
					if (page.equals(link.getLinkAddress()))
						return tab;
				}
			}
		}
		return null;
	}

	private int getPageNumberForTab(IWikiTab tab) {
		int index = wikiTabs.indexOf(tab);
		if (index == -1) {
			return 0;
		} else {
			return index / 6;
		}
	}

	private boolean hasMultipleTabPages() {
		return wikiTabs.size() > 6;
	}

	private int getTotalTabPages() {
		return wikiTabs.size() / 6 + 1;
	}

	/**
	 * This method was copied from Equivalent Exchange 3's RenderUtils.java
	 * class,
	 * https://github.com/pahimar/Equivalent-Exchange-3/blob/master/src/main/java/com/pahimar/ee3/client/renderer/RenderUtils.java
	 * 
	 * @param fontRenderer
	 * @param stack
	 * @param x
	 * @param y
	 * @param zLevel
	 * @param scale
	 */
	/*
	 * public void renderRotatingBlockIntoGUI(GuiWiki gui, ItemStack stack, int
	 * x, int y, float scale){
	 * 
	 * RenderBlocks renderBlocks = new RenderBlocks();
	 * 
	 * Block block = Block.blocksList[stack.itemID];
	 * FMLClientHandler.instance().getClient().renderEngine.bindTexture(
	 * TextureMap.locationBlocksTexture); GL11.glPushMatrix();
	 * GL11.glTranslatef(x - 2, y + 3, -3.0F + gui.zLevel); GL11.glScalef(10.0F,
	 * 10.0F, 10.0F); GL11.glTranslatef(1.0F, 0.5F, 1.0F); GL11.glScalef(1.0F *
	 * scale, 1.0F * scale, -1.0F); GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
	 * GL11.glRotatef(-TickHandler.ticksExisted, 0.0F, 1.0F, 0.0F);
	 * 
	 * int var10 = Item.itemsList[stack.itemID].getColorFromItemStack(stack, 0);
	 * float var16 = (var10 >> 16 & 255) / 255.0F; float var12 = (var10 >> 8 &
	 * 255) / 255.0F; float var13 = (var10 & 255) / 255.0F;
	 * 
	 * GL11.glColor4f(var16, var12, var13, 1.0F);
	 * 
	 * GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F); renderBlocks.useInventoryTint =
	 * true; renderBlocks.renderBlockAsItem(block, stack.getItemDamage(), 1.0F);
	 * renderBlocks.useInventoryTint = true; GL11.glPopMatrix(); }
	 */

	private static RenderEntityItem renderItem;
	private static EntityItem entityItem;

	public void renderRotatingBlockIntoGUI(GuiWiki gui, ItemStack stack, int x, int y, float scale) {
		if (entityItem == null) {
			entityItem = new EntityItem(gui.mc.world);
			renderItem = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(),
					Minecraft.getMinecraft().getRenderItem()) {
				@Override
				public boolean shouldBob() {
					return false;
				}
			};
		}
		entityItem.setEntityItemStack(stack);
		if (stack.getItem().getClass().toString().equalsIgnoreCase(ItemBlockDemonCrystal.class.toString())) {
			// System.out.println(stack.getItem().getClass());
			GL11.glPushMatrix();
			GL11.glTranslated(x - 32, y - 25, 50);
			GL11.glScaled(20 * scale, 15 * scale, -15 * scale);
			GL11.glRotated(30, 0, 1, 0);
			// GL11.glRotated(30, 1, 0, 0);
			// GL11.glTranslated(0.1, 0.1, gui.zLevel);
			// GL11.glRotated(-TickHandler.ticksExisted, 0, 0, 0);
			renderItem.doRender(entityItem, 0.0, 0.0, 0, 0, 0);
			GL11.glPopMatrix();
			return;
		}
		if (!Block.getBlockFromItem(stack.getItem()).getClass().toString()
				.equalsIgnoreCase(BlockTeleposer.class.toString())
				&& !stack.getItem().getClass().toString().equalsIgnoreCase(ItemBlockEnum.class.toString())
				&& !Block.getBlockFromItem(stack.getItem()).getClass().toString()
						.equalsIgnoreCase(BlockMimic.class.toString())
				&& Block.getBlockFromItem(stack.getItem()).hasTileEntity()
				&& !stack.getItem().getClass().toString().equalsIgnoreCase(ItemBlockBloodTank.class.toString())) {
			if (Block.getBlockFromItem(stack.getItem()).getClass().toString()
					.equalsIgnoreCase(BlockAltar.class.toString())) {
				GL11.glPushMatrix();
				GL11.glTranslated(x + 1, y + 22, 20);
				GL11.glScaled(40 * scale, 40 * scale, -40 * scale);
				GL11.glRotated(180, 1, 0, 0);
				GL11.glRotated(30, 1, 0, 0);
				GL11.glTranslated(0.1, 0.1, gui.zLevel);
				GL11.glRotated(-TickHandler.ticksExisted, 0, 1, 0);
				renderItem.doRender(entityItem, 0.0, 0.0, 0, 0, 0);
				GL11.glPopMatrix();
				// } else if
				// (stack.getItem().getClass().toString().equalsIgnoreCase(ritualm.class.toString()))
				// {
				// GL11.glPushMatrix();
				// GL11.glTranslated(x + 1, y + 13, 20);
				// GL11.glScaled(40 * scale, 40 * scale, -40 * scale);
				// GL11.glRotated(180, 1, 0, 0);
				// GL11.glRotated(30, 1, 0, 0);
				// GL11.glTranslated(0.1, 0.1, gui.zLevel);
				// GL11.glRotated(-TickHandler.ticksExisted, 0, 1, 0);
				// renderItem.doRender(entityItem, 0.0, 0.0, 0, 0, 0);
				// GL11.glPopMatrix();
			} else {
				GL11.glPushMatrix();
				GL11.glTranslated(x + 1, y + 30, 20);
				GL11.glScaled(40 * scale, 40 * scale, -40 * scale);
				GL11.glRotated(180, 1, 0, 0);
				GL11.glRotated(30, 1, 0, 0);
				GL11.glTranslated(0.1, 0.1, gui.zLevel);
				GL11.glRotated(-TickHandler.ticksExisted, 0, 1, 0);
				renderItem.doRender(entityItem, 0.0, 0.0, 0, 0, 0);
				GL11.glPopMatrix();
			}
		} else if (Block.getBlockFromItem(stack.getItem()) == Blocks.AIR) {
			GL11.glPushMatrix();
			GL11.glTranslated(x + 1, y + 17, 20);
			GL11.glScaled(40 * scale, 40 * scale, -40 * scale);
			GL11.glRotated(180, 1, 0, 0);
			GL11.glRotated(30, 1, 0, 0);
			GL11.glTranslated(0.1, 0.1, gui.zLevel);
			GL11.glRotated(-TickHandler.ticksExisted, 0, 1, 0);
			renderItem.doRender(entityItem, 0.0, 0.0, 0, 0, 0);
			GL11.glPopMatrix();
		} else if (stack.getItem().getClass().toString().equalsIgnoreCase(ItemBlockEnum.class.toString())
				&& !Block.getBlockFromItem(stack.getItem()).getClass().toString()
						.equalsIgnoreCase(BlockMimic.class.toString())
				&& !Block.getBlockFromItem(stack.getItem()).isFullBlock(null)) {
			// System.out.println(((ItemBlockEnum)stack.getItem()).getBlock().getClass());
			if (((ItemBlockEnum) stack.getItem()).getBlock().getClass().toString()
					.equalsIgnoreCase(BlockDemonStairsBase.class.toString())) {
				GL11.glPushMatrix();
				GL11.glTranslated(x + 1, y + 13, 20);
				GL11.glScaled(40 * scale, 40 * scale, -40 * scale);
				GL11.glRotated(180, 1, 0, 0);
				GL11.glRotated(30, 1, 0, 0);
				GL11.glTranslated(0.1, 0.1, gui.zLevel);
				GL11.glRotated(-TickHandler.ticksExisted, 0, 1, 0);
				renderItem.doRender(entityItem, 0.0, 0.0, 0, 0, 0);
				GL11.glPopMatrix();
			} else {
				GL11.glPushMatrix();
				GL11.glTranslated(x + 1, y + 30, 20);
				GL11.glScaled(40 * scale, 40 * scale, -40 * scale);
				GL11.glRotated(180, 1, 0, 0);
				GL11.glRotated(30, 1, 0, 0);
				GL11.glTranslated(0.1, 0.1, gui.zLevel);
				GL11.glRotated(-TickHandler.ticksExisted, 0, 1, 0);
				renderItem.doRender(entityItem, 0.0, 0.0, 0, 0, 0);
				GL11.glPopMatrix();
			}
		} else {
			GL11.glPushMatrix();
			GL11.glTranslated(x + 1, y + 13, 20);
			GL11.glScaled(40 * scale, 40 * scale, -40 * scale);
			GL11.glRotated(180, 1, 0, 0);
			GL11.glRotated(30, 1, 0, 0);
			GL11.glTranslated(0.1, 0.1, gui.zLevel);
			GL11.glRotated(-TickHandler.ticksExisted, 0, 1, 0);
			renderItem.doRender(entityItem, 0.0, 0.0, 0, 0, 0);
			GL11.glPopMatrix();
		}
	}
}
