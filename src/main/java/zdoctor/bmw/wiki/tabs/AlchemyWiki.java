package zdoctor.bmw.wiki.tabs;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModBlocks;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import zdoctor.bmw.ModMain;

public class AlchemyWiki extends BaseWikiTab {
	public static void preInit() {
		WikiRegistry.registerWikiTab(new AlchemyWiki());
	}

	public AlchemyWiki() {
		addSectionHeader("Blocks");
		pageEntries.add("BlockAlchemyTable");
		addSectionHeader("Items");
		pageEntries.add("LengthCatalyst");
		pageEntries.add("PowerCatalyst");
		addSectionHeader("Flasks");
		pageEntries.add("FlaskLame");
		pageEntries.add("FlaskRegen");
		pageEntries.add("FlaskHealth");
		pageEntries.add("FlaskHealthBoost");
		pageEntries.add("FlaskNight");
		pageEntries.add("FlaskFire");
		pageEntries.add("FlaskWater");
		pageEntries.add("FlaskSlow");
		pageEntries.add("FlaskSpeed");
		pageEntries.add("FlaskPosion");
		pageEntries.add("FlaskBlind");
		pageEntries.add("FlaskWeak");
		pageEntries.add("FlaskStrength");
		pageEntries.add("FlaskJump");
		pageEntries.add("FlaskHaste");
		pageEntries.add("FlaskInvisible");
		pageEntries.add("FlaskSaturation");
		pageEntries.add("FlaskBounce");
		pageEntries.add("FlaskCling");
		pageEntries.add("FlaskDeaf");
		addSectionHeader("Recipes");
		pageEntries.add("String");
		pageEntries.add("Flint");
		pageEntries.add("Leather");
		pageEntries.add("ExplosivePowder");
		pageEntries.add("Bread");
		pageEntries.add("Grass");
		pageEntries.add("Clay");
		pageEntries.add("Obsidian");
		pageEntries.add("Sulfur");
		pageEntries.add("Saltpeter");
		pageEntries.add("GunPowder");
		pageEntries.add("CoalSand");
		pageEntries.add("BasicCuttingFluid");
		pageEntries.add("IronSand");
		pageEntries.add("GoldSand");
		pageEntries.add("Redstone");
		pageEntries.add("Gravel");
		pageEntries.add("Sand");
		pageEntries.add("PlantOil");
		pageEntries.add("Neurotoxin");
		pageEntries.add("Antispectic");
		pageEntries.add("DraftAngelus");
		pageEntries.add("Holding");
		
	}

	@Override
	public String getName() {
		return "wikitab." + ModMain.MODID + ".alchemy.name";
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return new ItemStack(ModBlocks.ALCHEMY_TABLE);
	}

	@Override
	protected String getPageName(String pageEntry) {
		return I18n.format("alchemy." + ModMain.MODID + "." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		String entry = BloodMagicWiki.getPageByPageEntry(pageEntry);
		if(entry == null)
			return Constants.Mod.MODID.toLowerCase() + "/alchemy/" + pageEntry;
		else
			return entry;
	}
}
