package zdoctor.bmw.wiki.registry;

import WayofTime.bloodmagic.api.Constants;
import embedded.igwmod.WikiUtils;
import embedded.igwmod.api.ItemWikiEvent;
import embedded.igwmod.gui.BrowseHistory;
import embedded.igwmod.gui.GuiWiki;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.wiki.events.BlockWikiEvent;
import zdoctor.bmw.wiki.events.EntityWikiEvent;
import zdoctor.bmw.wiki.events.PageChangeEvent;
import zdoctor.bmw.wiki.registry.tabs.ItemsWiki;
import zdoctor.bmw.wiki.registry.tabs.RecipeWiki;

public class EventRegistry {
	public static void postInit() {
		MinecraftForge.EVENT_BUS.register(new Events());
	}

	private static class Events {
		@SubscribeEvent(receiveCanceled = false)
		public void wikiEvent(ItemWikiEvent e) {
//			System.out.println("Item Wiki Event: " + e.pageOpened);
			if(GuiWiki.getCurrentTab().getName().equalsIgnoreCase(RecipeWiki.wikiName)) {
				e.pageOpened = "$Recipe";
			} else
				e.pageOpened = ItemsWiki.getPageFromName(e.pageOpened);
		}

		@SubscribeEvent
		public void wikiEvent(BlockWikiEvent e) {
//			System.out.println("Block Wiki Event: " + e.pageOpened);
			e.pageOpened = ItemsWiki.getPageFromName(e.pageOpened);
		}

		@SubscribeEvent
		public void wikiEvent(EntityWikiEvent e) {
			e.pageOpened = BrowseHistory.current() == null ? ModMain.MODID.toLowerCase() + ":bloodmagic/intro"
					: BrowseHistory.current().link;
		}

		@SubscribeEvent
		public void pageChanged(PageChangeEvent e) {
			if(e.currentFile.equalsIgnoreCase("$Recipe")) {
				e.pageText = RecipeWiki.generatePageFromStack(e.associatedStack);
				if(e.pageText == null)
					e.currentFile = ItemsWiki.getPageFromName(WikiUtils.getNameFromStack(e.associatedStack));
			}
		}

		// @SubscribeEvent
		// public void onPlayerJoin(TickEvent.PlayerTickEvent event){
		// if(event.player.world.isRemote && event.player ==
		// FMLClientHandler.instance().getClientPlayerEntity()) {
		//// event.player.(ITextComponent.Serializer.jsonToComponent("[\"" +
		// TextFormatting.GOLD + "The mod " + supportingMod + " is supporting
		// In-Game Wiki mod. " + TextFormatting.GOLD + "However, In-Game Wiki
		// isn't installed! " + "[\"," + "{\"text\":\"Download
		// Latest\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/igwmod_download\"}},"
		// + "\"]\"]"));
		// GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		// chat.printChatMessage(ITextComponent.Serializer.jsonToComponent("[\""
		// + TextFormatting.GOLD + "The mod " + supportingMod + " is supporting
		// In-Game Wiki mod. " + TextFormatting.GOLD + "However, In-Game Wiki
		// isn't installed! " + "[\"," + "{\"text\":\"Download
		// Latest\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/igwmod_download\"}},"
		// + "\"]\"]"));
		// FMLCommonHandler.instance().bus().unregister(this);
		// }
		// }
	}
}