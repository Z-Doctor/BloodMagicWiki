package zdoctor.bmw.wiki.registry.tabs;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import zdoctor.bmw.ModMain;

public class MiscWiki extends BaseWikiTab {
	public static void preInit() {
		WikiRegistry.registerWikiTab(new MiscWiki());
	}

	public MiscWiki() {
		refreshWiki();
	}

	@Override
	public String getName() {
		return "wikitab." + ModMain.MODID + ".misc.name";
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return new ItemStack(ModItems.EXPERIENCE_TOME);
	}

	@Override
	protected String getPageName(String pageEntry) {
		return I18n.format("misc." + ModMain.MODID + "." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		String entry = BloodMagicWiki.getPageByPageEntry(pageEntry);
		if(entry == null)
			return Constants.Mod.MODID.toLowerCase() + "/misc/" + pageEntry;
		else
			return entry;
	}

	@Override
	public void refreshWiki() {
		addSectionHeader("Items");
		pageEntries.add("ItemRitualTinkerer");
		pageEntries.add("ItemExperienceTome");
		pageEntries.add("ItemUpgradeTome");
		pageEntries.add("ItemFilter");
		addSectionHeader("Blocks");
		pageEntries.add("BlockMimics");
		pageEntries.add("BlockWillBlocks");
		pageEntries.add("BlockDemonLight");
		pageEntries.add("BlockInversion");
		addSectionHeader("Misc");
		pageEntries.add("SoulFray");
	}
}
