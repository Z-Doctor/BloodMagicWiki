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
		pageEntries.add("BloodOrbs");
		pageEntries.add("Runes");
		pageEntries.add("IncenseAltar");
		
		pageEntries.add("Tier1");
		pageEntries.add("Tier2");
		pageEntries.add("Tier3");
		pageEntries.add("Tier4");
		pageEntries.add("Tier5");
		pageEntries.add("Tier6");
		pageEntries.add("SanguineBook");
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
		return I18n.format("wiki." + ModMain.MODID + "." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		if(pageEntry.contains("incensealtar"))
			return Constants.Mod.MODID.toLowerCase() + "/block/" + pageEntry;
		else if(pageEntry.contains("sanguinebook"))
			return Constants.Mod.MODID.toLowerCase() + "/item/" + pageEntry;
		return Constants.Mod.MODID.toLowerCase() + "/" + pageEntry;
	}

}
