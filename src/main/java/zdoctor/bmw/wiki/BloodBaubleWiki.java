package zdoctor.bmw.wiki;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.registry.ModItems;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import zdoctor.bmw.ModMain;

public class BloodBaubleWiki extends BaseWikiTab {
	public static void preInit() {
		WikiRegistry.registerWikiTab(new BloodBaubleWiki());
	}

	public BloodBaubleWiki() {
	}

	@Override
	public String getName() {
		return "wikitab.bloodmagic.name";
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return OrbRegistry.getOrbStack(ModItems.ORB_WEAK);
	}

	@Override
	protected String getPageName(String pageEntry) {
		return I18n.format("wiki.bloodmagic." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		return ModMain.MODID + ":" + Constants.Mod.MODID.toLowerCase() + "/" + pageEntry;
	}

}
