package zdoctor.bmw.wiki.registry.tabs;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModBlocks;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import zdoctor.bmw.ModMain;

public class ForgeWiki extends BaseWikiTab {
	public static void preInit() {
		WikiRegistry.registerWikiTab(new ForgeWiki());
	}

	public ForgeWiki() {
		refreshWiki();
	}

	@Override
	public String getName() {
		return "wikitab.forge.name";
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return new ItemStack(ModBlocks.SOUL_FORGE);
	}

	@Override
	protected String getPageName(String pageEntry) {
		return I18n.format("forge." + ModMain.MODID + "." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		String entry = BloodMagicWiki.getPageByPageEntry(pageEntry);
		if(entry == null)
			return Constants.Mod.MODID.toLowerCase() + "/forge/" + pageEntry;
		else
			return entry;
	}

	@Override
	public void refreshWiki() {
		addSectionHeader("Items");
		pageEntries.add("ItemSoulGem");
		pageEntries.add("ItemSentientArmour");
		pageEntries.add("ItemSentientSword");
		pageEntries.add("ItemSentientBow");
		pageEntries.add("ItemSentientPicaxe");
		pageEntries.add("ItemSentientAxe");
		pageEntries.add("ItemSentientShovel");
		pageEntries.add("ItemNodeRouter");
		pageEntries.add("ItemWillGauge");
		addSectionHeader("Blocks");
		pageEntries.add("BlockSoulForge");
		pageEntries.add("BlockDemonCrucible");
		pageEntries.add("BlockDemonPylon");
		pageEntries.add("BlockWillCluster");
		pageEntries.add("BlockNodes");
	}

}
