package zdoctor.bmw.wiki;

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
		pageEntries.add("waterRitual");
		pageEntries.add("lavaRitual");
		pageEntries.add("greenGroveRitual");
		pageEntries.add("jumpRitual");
		pageEntries.add("wellOfSufferingRitual");
		pageEntries.add("featheredKnifeRitual");
		pageEntries.add("regenerationRitual");
		pageEntries.add("harvestRitual");
		pageEntries.add("magneticRitual");
		pageEntries.add("crushingRitual");
		pageEntries.add("fullStomachRitual");
		pageEntries.add("interdictionRitual");
		pageEntries.add("containmentRitual");
		pageEntries.add("speedRitual");
		pageEntries.add("suppressionRitual");
		pageEntries.add("expulsionRitual");
		pageEntries.add("zephyrRitual");
		pageEntries.add("upgradeRemoveRitual");
		pageEntries.add("armourEvolveRitual");
		pageEntries.add("animalGrowthRitual");
		pageEntries.add("forsakenSoulRitual");
		pageEntries.add("crystalHarvestRitual");
		pageEntries.add("cobblestoneRitual");
		pageEntries.add("placerRitual");
		pageEntries.add("fellingRitual");
		pageEntries.add("pumpRitual");
		pageEntries.add("altarBuilderRitual");
		pageEntries.add("portalRitual");

	}

	@Override
	public String getName() {
		return "wikitab.bloodmagic.ritual.name";
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return new ItemStack(ModBlocks.RITUAL_STONE);
	}

	@Override
	protected String getPageName(String pageEntry) {
		return I18n.format("ritual.BloodMagic." + pageEntry);
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		return ModMain.MODID.toLowerCase() + ":bloodmagic/ritual" + pageEntry;
	}
}
