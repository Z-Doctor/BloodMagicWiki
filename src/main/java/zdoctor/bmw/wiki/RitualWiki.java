package zdoctor.bmw.wiki;

import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import tweaked.igwmod.api.WikiRegistry;
import tweaked.igwmod.gui.GuiWiki;
import tweaked.igwmod.gui.tabs.BaseWikiTab;
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
		return new ItemStack(ModBlocks.ritualStone);
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
