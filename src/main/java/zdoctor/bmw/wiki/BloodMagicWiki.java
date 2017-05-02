package zdoctor.bmw.wiki;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.registry.ModItems;
import igwmod.api.WikiRegistry;
import igwmod.gui.GuiWiki;
import igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
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
		pageEntries.add("Index");
		pageEntries.add("Acknowledgments");
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

	public static String getItemPage(String pageOpened) {
		if (pageOpened.toLowerCase().contains("routing")) {
			pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/block/Routing";
		} else if (pageOpened.toLowerCase().contains("item/orb")) {
			pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/item/BloodOrb";
		} else if (pageOpened.toLowerCase().contains("soulgem") || pageOpened.toLowerCase().contains("monstersoul")) {
			pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/item/SoulGems";
		}
		return pageOpened;
	}
}
