package zdoctor.bmw.wiki.registry.tabs;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.registry.ModItems;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import zdoctor.bmw.ModMain;

public class BloodMagicWiki extends BaseWikiTab {
	public static void preInit() {
		WikiRegistry.registerWikiTab(new BloodMagicWiki());
	}

	public BloodMagicWiki() {
		refreshWiki();
	}

	@Override
	public String getName() {
		return "wikitab." + ModMain.MODID + ".name";
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return OrbRegistry.getOrbStack(ModItems.ORB_WEAK);
	}

	@Override
	protected String getPageName(String pageEntry) {
		return I18n.format("wiki." + ModMain.MODID + "." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		return Constants.Mod.MODID.toLowerCase() + "/" + pageEntry;
	}
	
	public static String getPageByPageEntry(String pageEntry) {
//		System.out.println("Test: " + pageEntry);
		if(pageEntry.matches("\\bitem\\w*"))
			return Constants.Mod.MODID.toLowerCase() + "/item/" + pageEntry.replaceFirst("(\\bitem)(\\w*)", "$2");
		else if(pageEntry.matches("\\bblock\\w*"))
			return Constants.Mod.MODID.toLowerCase() + "/block/" + pageEntry.replaceFirst("(\\bblock)(\\w*)", "$2");
		else if(pageEntry.matches("\\bsigil\\w*"))
			return Constants.Mod.MODID.toLowerCase() + "/item/sigil/" + pageEntry.replaceFirst("(\\bsigil)(\\w*)", "$2");
		else if(pageEntry.matches("\\bflask\\w*"))
			return Constants.Mod.MODID.toLowerCase() + "/item/flask/" + pageEntry.replaceFirst("(\\bflask)(\\w*)", "$2");
		else if(pageEntry.matches("\\bref\\w*"))
			return Constants.Mod.MODID.toLowerCase() + "/" + pageEntry.replaceFirst("(\\bref)(\\w*)", "$2");
		return null;
	}

	@Override
	public void refreshWiki() {
		addSectionHeader("GettingStarted");
		pageEntries.add("Intro");
		skipLine();
		pageEntries.add("GettingStarted1");
		pageEntries.add("GettingStarted2");
		pageEntries.add("GettingStarted3");
		pageEntries.add("GettingStarted4");
		pageEntries.add("GettingStarted5");
		pageEntries.add("GettingStarted6");
		pageEntries.add("GettingStarted7");
		pageEntries.add("GettingStarted8");
		skipLine();
		pageEntries.add("Acknowledgments");
	}
}
