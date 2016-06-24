package zdoctor.bmw.wiki;

import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.registry.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import tweaked.igwmod.InfoSupplier;
import tweaked.igwmod.api.WikiRegistry;
import tweaked.igwmod.gui.GuiWiki;
import tweaked.igwmod.gui.tabs.BaseWikiTab;
import zdoctor.bmw.ModMain;

public class BloodMagicWiki extends BaseWikiTab {
	public static void preInit() {
		WikiRegistry.registerWikiTab(new BloodMagicWiki());
	}
	
	public BloodMagicWiki() {
		pageEntries.add("Intro");
		pageEntries.add("GettingStarted1");
		pageEntries.add("GettingStarted2");
		pageEntries.add("GettingStarted3");
		pageEntries.add("GettingStarted4");
		pageEntries.add("GettingStarted5");
		pageEntries.add("GettingStarted6");
		pageEntries.add("GettingStarted7");
		pageEntries.add("GettingStarted8");
		pageEntries.add("GettingStarted9");
		pageEntries.add("Index");
		pageEntries.add("Acknowledgments");
	}

	@Override
	public String getName() {
		return "wikitab.bloodmagic.name";
	}
	
	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return OrbRegistry.getOrbStack(ModItems.orbWeak);
	}

	@Override
	protected String getPageName(String pageEntry) {
		return I18n.format("wiki.bloodmagic." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		return ModMain.MODID.toLowerCase() + ":bloodmagic/" + pageEntry;
	}
}
