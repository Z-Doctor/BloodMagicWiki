package zdoctor.bmw.wiki.tabs;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModBlocks;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import zdoctor.bmw.ModMain;

public class RitualWiki extends BaseWikiTab {
	public static void preInit() {
		WikiRegistry.registerWikiTab(new RitualWiki());
	}

	public RitualWiki() {
		addSectionHeader("RitualTypes");
		pageEntries.add("RefImperfectRituals");
		pageEntries.add("RefRegularRituals");
		addSectionHeader("Items");
		pageEntries.add("ItemActivationCrystals");
		pageEntries.add("ItemScribeTools");
		pageEntries.add("ItemRitualDiviner");
		addSectionHeader("Rituals");
		pageEntries.add("WaterRitual");
		pageEntries.add("LavaRitual");
		pageEntries.add("GreenGroveRitual");
		pageEntries.add("JumpRitual");
		pageEntries.add("WellOfSufferingRitual");
		pageEntries.add("FeatheredKnifeRitual");
		pageEntries.add("RegenerationRitual");
		pageEntries.add("HarvestRitual");
		pageEntries.add("MagneticRitual");
		pageEntries.add("CrushingRitual");
		pageEntries.add("FullStomachRitual");
		pageEntries.add("InterdictionRitual");
		pageEntries.add("ContainmentRitual");
		pageEntries.add("SpeedRitual");
		pageEntries.add("SuppressionRitual");
		pageEntries.add("ExpulsionRitual");
		pageEntries.add("ZephyrRitual");
		pageEntries.add("UpgradeRemoveRitual");
		pageEntries.add("ArmourEvolveRitual");
		pageEntries.add("AnimalGrowthRitual");
		pageEntries.add("ForsakenSoulRitual");
		pageEntries.add("CrystalHarvestRitual");
		pageEntries.add("CobblestoneRitual");
		pageEntries.add("PlacerRitual");
		pageEntries.add("FellingRitual");
		pageEntries.add("PumpRitual");
		pageEntries.add("AltarBuilderRitual");
		pageEntries.add("PortalRitual");

	}

	@Override
	public String getName() {
		return "wikitab." + ModMain.MODID + ".ritual.name";
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return new ItemStack(ModBlocks.RITUAL_STONE);
	}

	@Override
	protected String getPageName(String pageEntry) {
		return I18n.format("ritual." + ModMain.MODID + "." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		String entry = BloodMagicWiki.getPageByPageEntry(pageEntry);
		if(entry == null)
			return Constants.Mod.MODID.toLowerCase() + "/ritual/" + pageEntry;
		else
			return entry;
	}
}
