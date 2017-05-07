package zdoctor.bmw.wiki.tabs;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.registry.ModItems;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import zdoctor.bmw.ModMain;

public class ArrayWiki extends BaseWikiTab {
	public static void preInit() {
		WikiRegistry.registerWikiTab(new ArrayWiki());
	}

	public ArrayWiki() {
		pageEntries.add("ArcaneAshe");
		pageEntries.add("SentinentEquipment");
		
		
	}

	@Override
	public String getName() {
		return "wikitab." + ModMain.MODID + ".array.name";
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return new ItemStack(ModItems.ARCANE_ASHES);
	}

	@Override
	protected String getPageName(String pageEntry) {
		return I18n.format("wiki." + ModMain.MODID +"." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		if(pageEntry.contains("array"))
			return Constants.Mod.MODID.toLowerCase() + "/array/" + pageEntry;
		return Constants.Mod.MODID.toLowerCase() + "/item/" + pageEntry;
	}

}
