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

public class AltarWiki extends BaseWikiTab {
	public static void preInit() {
		WikiRegistry.registerWikiTab(new AltarWiki());
	}

	public AltarWiki() {
		addSectionHeader("Items");
		pageEntries.add("ItemSacrificialDagger");
		pageEntries.add("ItemDaggerOfSacrifice");
		pageEntries.add("ItemBloodOrbs");
		pageEntries.add("ItemSanguineBook");
		addSectionHeader("Blocks");
		pageEntries.add("BlockAltar");
		pageEntries.add("BlockRunes");
		pageEntries.add("BlockIncenseAltar");
		pageEntries.add("BlockBloodTank");
		pageEntries.add("BlockPaths");
		pageEntries.add("BlockBloodStoneBrick");
		pageEntries.add("BlockCrystalCluster");
		addSectionHeader("UpgradingAltar");
		pageEntries.add("Tier1");
		pageEntries.add("Tier2");
		pageEntries.add("Tier3");
		pageEntries.add("Tier4");
		pageEntries.add("Tier5");
		pageEntries.add("Tier6");
		addSectionHeader("Runes");
		pageEntries.add("BlockBlank");
		pageEntries.add("BlockEfficiency");
		pageEntries.add("BlockSacrifice");
		pageEntries.add("BlockSelfsacrifice");
		pageEntries.add("BlockAugcapacity");
		pageEntries.add("BlockOrb");
		pageEntries.add("BlockAcceleration");
		pageEntries.add("BlockCharging");
		pageEntries.add("BlockSpeed");
		pageEntries.add("BlockDisplacement");
		pageEntries.add("BlockCapacity");
	}

	@Override
	public String getName() {
		return "wikitab.altar.name";
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return new ItemStack(ModBlocks.ALTAR);
	}

	@Override
	protected String getPageName(String pageEntry) {
		return I18n.format("altar." + ModMain.MODID + "." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		String entry = BloodMagicWiki.getPageByPageEntry(pageEntry);
		if(entry == null)
			return Constants.Mod.MODID.toLowerCase() + "/altar/" + pageEntry;
		else
			return entry;
	}

}
