package zdoctor.bmw.wiki.tabs;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
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
		addSectionHeader("Items");
		pageEntries.add("ItemSoulGem");
		pageEntries.add("ItemSentinentArmour");
		pageEntries.add("ItemSentinentSword");
		pageEntries.add("ItemSentinentBow");
		pageEntries.add("ItemSentinentPicaxe");
		pageEntries.add("ItemSentinentAxe");
		pageEntries.add("ItemSentinentShovel");
		pageEntries.add("ItemNodeRouter");
		addSectionHeader("Blocks");
		pageEntries.add("BlockForge");
		pageEntries.add("BlockDemonCrucible");
		pageEntries.add("BlockDemonCrystal");
		pageEntries.add("BlockDemonPylon");
		pageEntries.add("BlockWillCluster");
		pageEntries.add("BlockNodes");
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
		return I18n.format("wiki." + ModMain.MODID + "." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		String entry = BloodMagicWiki.getPageByPageEntry(pageEntry);
		if(entry == null)
			return Constants.Mod.MODID.toLowerCase() + "/forge/" + pageEntry;
		else
			return entry;
	}

}
