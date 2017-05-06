package zdoctor.bmw.wiki;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModItems;
import embedded.igwmod.WikiUtils;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.IPageLink;
import embedded.igwmod.gui.LocatedStack;
import embedded.igwmod.gui.tabs.BlockAndItemWikiTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.api.BloodMagicItem;
import zdoctor.bmw.client.ClientProxy;

public class ItemsWiki extends BlockAndItemWikiTab {
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
		return new ItemStack(ModItems.SOUL_SNARE);
	}

	@Override
	public List<IPageLink> getPages(int[] indexes) {
		NonNullList<ItemStack> entries = NonNullList.<ItemStack>create();
		ClientProxy.ItemRegistry.getValues().forEach(item -> {
			ItemStack stack = new ItemStack(item);
			if (WikiUtils.getOwningModId(stack).equalsIgnoreCase(Constants.Mod.MODID)
					|| item instanceof BloodMagicItem) {
				if (item.getCreativeTab() != null) {
					if(stack.getHasSubtypes()) {
						stack.getItem().getSubItems(stack.getItem(), CreativeTabs.SEARCH, entries);
					} else {
						entries.add(stack);
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
}
