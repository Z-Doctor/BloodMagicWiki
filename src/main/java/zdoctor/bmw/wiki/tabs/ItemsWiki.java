package zdoctor.bmw.wiki.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import embedded.igwmod.WikiUtils;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.IPageLink;
import embedded.igwmod.gui.LocatedStack;
import embedded.igwmod.gui.tabs.BlockAndItemWikiTab;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.api.BloodMagicItem;
import zdoctor.bmw.client.ClientProxy;

public class ItemsWiki extends BlockAndItemWikiTab {
	public static final Map<String, String> PageRegistry = new HashMap<>();

	public static void preInit() {
		WikiRegistry.registerWikiTab(new ItemsWiki());
	}

	public ItemsWiki() {
	}

	@Override
	public String getName() {
		return "wikitab." + ModMain.MODID + ".items.name";
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return new ItemStack(ModItems.SANGUINE_BOOK);
	}

	@Override
	public List<IPageLink> getPages(int[] indexes) {
		NonNullList<ItemStack> entries = NonNullList.<ItemStack>create();
		ClientProxy.ItemRegistry.getValues().forEach(item -> {
			ItemStack stack = new ItemStack(item);
			if (WikiUtils.getOwningModId(stack).equalsIgnoreCase(Constants.Mod.MODID)
					|| item instanceof BloodMagicItem) {
				if (item.getCreativeTab() != null) {
					String unloc = stack.getUnlocalizedName().toLowerCase();
					if (!unloc.endsWith("phantom")) {
						if (stack.getHasSubtypes() && !(stack.getUnlocalizedName().toLowerCase().contains("upgradetome")
								|| unloc.contains("downgradetome") || unloc.contains("bloodtank")
								|| unloc.contains("upgradetrainer") || unloc.contains("sacrificialdagger")
								|| unloc.matches("(.*)bricks(.*)") || unloc.matches("(.*)pillar[0-9](.*)")
								|| unloc.matches("(.*)pillarcap[0-9](.*)") || unloc.contains("stair")
								|| unloc.contains("wall") || unloc.contains(".extras."))) {
							if (unloc.contains(".activationcrystal.")) {
								entries.add(new ItemStack(stack.getItem(), 1, 0));
								entries.add(new ItemStack(stack.getItem(), 1, 1));
							} else
								stack.getItem().getSubItems(item, CreativeTabs.SEARCH, entries);
						} else {
							entries.add(stack);
						}
					}
				}
			}
		});
		List<IPageLink> pages = new ArrayList<IPageLink>();
		if (indexes == null) {
			for (int i = 0; i < entries.size(); i++) {
				pages.add(new LocatedStack(entries.get(i), 41 + i % 2 * 18, 75 + i / 2 * 18));
			}
		} else {
			for (int i = 0; i < indexes.length; i++) {
				pages.add(new LocatedStack(entries.get(indexes[i]), 41 + i % 2 * 18, 75 + i / 2 * 18));
			}
		}
		return pages;
	}

	public static String getPageFromName(String pageOpened) {
		System.out.println("Page: " + pageOpened);
		pageOpened = pageOpened.toLowerCase();
		if (pageOpened.contains(".orb."))
			return Constants.Mod.MODID + "/item/bloodorbs";
		else if (pageOpened.contains(".sacrificialdagger."))
			return Constants.Mod.MODID + "/item/sacrificialdagger";
		else if (pageOpened.contains(".daggerofsacrifice"))
			return Constants.Mod.MODID + "/item/daggerofsacrifice";
		else if (pageOpened.contains(".soulgem."))
			return Constants.Mod.MODID + "/item/soulgem";
		else if (pageOpened.contains(".basecomponent."))
			return Constants.Mod.MODID + "/item/" + pageOpened.replaceFirst("(.*)basecomponent\\.", "");
		else if (pageOpened.contains(".scribe."))
			return Constants.Mod.MODID + "/item/scribetools";
		else if (pageOpened.contains(".ritualdiviner"))
			return Constants.Mod.MODID + "/item/diviners";
		else if (pageOpened.contains(".activationcrystal."))
			return Constants.Mod.MODID + "/item/activationcrystals";
		else if (pageOpened.contains(".soulsnare."))
			return Constants.Mod.MODID + "/item/soulsnare";
		else if (pageOpened.contains(".monstersoul."))
			return Constants.Mod.MODID + "/item/monstersoul";
		else if (pageOpened.contains(".sentientsword."))
			return Constants.Mod.MODID + "/item/sentientsword";
		else if (pageOpened.contains(".sentientbow."))
			return Constants.Mod.MODID + "/item/sentientbow";
		else if (pageOpened.contains(".slate."))
			return Constants.Mod.MODID + "/item/slate";
		else if (pageOpened.contains(".bound."))
			return Constants.Mod.MODID + "/item/bound";
		else if (pageOpened.contains(".livingarmour."))
			return Constants.Mod.MODID + "/item/livingarmour";
		else if (pageOpened.contains("upgrade") || pageOpened.contains("downgrade") || pageOpened.contains("trainer"))
			return Constants.Mod.MODID + "/item/livingupgrades";
		else if (pageOpened.contains(".focus.") || pageOpened.contains(".teleposer"))
			return Constants.Mod.MODID + "/item/foci";
		else if (pageOpened.contains(".bloodshard."))
			return Constants.Mod.MODID + "/item/bloodshard";
		else if (pageOpened.contains(".sentientarmour."))
			return Constants.Mod.MODID + "/item/sentientarmour";
		else if (pageOpened.contains(".pack."))
			return Constants.Mod.MODID + "/item/bloodpac";
		else if (pageOpened.contains("inversionpillar"))
			return Constants.Mod.MODID + "/block/inversionpillar";
		else if (pageOpened.contains(".crystal."))
			return Constants.Mod.MODID + "/block/crystalcluster";
		else if (pageOpened.contains("stone.ritual"))
			return Constants.Mod.MODID + "/block/" + pageOpened.replaceFirst("(.*)\\.stone\\.ritual\\.", "");
		else if (pageOpened.contains(".ritualstone."))
			return Constants.Mod.MODID + "/block/ritualstones";
		else if (pageOpened.contains(".rune."))
			return Constants.Mod.MODID + "/block/" + pageOpened.replaceFirst("(.*)\\.rune\\.", "");
		else if (pageOpened.contains(".demoncrystal."))
			return Constants.Mod.MODID + "/block/demoncrystals";
		else if (pageOpened.contains("routing"))
			return Constants.Mod.MODID + "/block/routing";
		else if (pageOpened.contains(".bloodstonebrick."))
			return Constants.Mod.MODID + "/block/bloodstonebrick";
		else if (pageOpened.contains(".path."))
			return Constants.Mod.MODID + "/block/paths";
		else if (pageOpened.contains(".mimic."))
			return Constants.Mod.MODID + "/block/mimics";
		else if (pageOpened.matches("(.*)bricks[0-9](.*)") || pageOpened.matches("(.*)wall[0-9](.*)")
				|| pageOpened.matches("(.*)stairs[0-9](.*)") || pageOpened.matches("(.*)pillar[0-9](.*)")
				|| pageOpened.contains("extras") || pageOpened.matches("(.*)pillarcap[0-9](.*)"))
			return Constants.Mod.MODID + "/block/willblocks";
		else {
			pageOpened = pageOpened.replaceAll("\\.", "/");
			if (pageOpened.toLowerCase().matches(ModMain.MODID.toLowerCase()) || !pageOpened.matches(":")) {
				pageOpened = pageOpened.replaceAll("block/" + Constants.Mod.MODID,
						Constants.Mod.MODID.toLowerCase() + "/block");
				pageOpened = pageOpened.replaceAll("item/" + Constants.Mod.MODID,
						Constants.Mod.MODID.toLowerCase() + "/item");
			}
		}
		return pageOpened;
	}
}
