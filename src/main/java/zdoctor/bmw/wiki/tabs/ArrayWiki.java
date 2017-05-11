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
		addSectionHeader("Items");
		pageEntries.add("ItemArcaneAshe");
		addSectionHeader("BoundItems");
		pageEntries.add("ItemLivingArmour");
		pageEntries.add("ItemBoundBlade");
		pageEntries.add("ItemBoundPicaxe");
		pageEntries.add("ItemBoundAxe");
		pageEntries.add("ItemBoundShovel");
		addSectionHeader("Sigils");
		pageEntries.add("SigilVoid");
		pageEntries.add("SigilPhantomBridge");
		pageEntries.add("SigilTeleposition");
		pageEntries.add("SigilTransposition");
		pageEntries.add("SigilElementalAffinity");
		pageEntries.add("SigilMagnetism");
		pageEntries.add("SigilWater");
		pageEntries.add("SigilCompression");
		pageEntries.add("SigilDivination");
		pageEntries.add("SigilFrost");
		pageEntries.add("SigilSuppression");
		pageEntries.add("SigilLava");
		pageEntries.add("SigilClaw");
		pageEntries.add("SigilGreenGrove");
		pageEntries.add("SigilAir");
		pageEntries.add("SigilEnderSeverance");
		pageEntries.add("SigilBloodLight");
		pageEntries.add("SigilHaste");
		pageEntries.add("SigilSeer");
		pageEntries.add("SigilBounce");
		pageEntries.add("SigilFastMiner");
		pageEntries.add("SigilHolding");
		pageEntries.add("SigilWhirlwind");
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
		return I18n.format("array." + ModMain.MODID +"." + pageEntry + ".page");
	}

	@Override
	protected String getPageLocation(String pageEntry) {
		String entry = BloodMagicWiki.getPageByPageEntry(pageEntry);
		if(entry == null)
			return Constants.Mod.MODID.toLowerCase() + "/array/" + pageEntry;
		else
			return entry;
	}

}
