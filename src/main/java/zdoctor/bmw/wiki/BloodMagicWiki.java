package zdoctor.bmw.wiki;

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
		pageEntries.add("GettingStarted1");
		pageEntries.add("GettingStarted2");
		pageEntries.add("GettingStarted3");
		pageEntries.add("AltarUpgrade");
		pageEntries.add("Items");
		pageEntries.add("Blocks");
		pageEntries.add("Rituals");
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
