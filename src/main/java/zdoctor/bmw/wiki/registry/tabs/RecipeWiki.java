package zdoctor.bmw.wiki.registry.tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.security.ntlm.Client;

import WayofTime.bloodmagic.api.Constants.Mod;
import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.ShapelessBloodOrbRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import embedded.igwmod.WikiUtils;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.IPageLink;
import embedded.igwmod.gui.LocatedStack;
import embedded.igwmod.gui.tabs.BlockAndItemWikiTab;
import embedded.igwmod.lib.WikiLog;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.NonNullList;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.client.ClientProxy;
import zdoctor.bmw.recipeintegrator.compact.AutoRecipe;
import zdoctor.bmw.recipeintegrator.compact.AutoRecipe.RecipeType;
import zdoctor.bmw.recipeintegrator.compact.SimpleArrayRecipe;

public class RecipeWiki extends BlockAndItemWikiTab {
	public static final Map<String, String> PageRegistry = new HashMap<>();
	public static final String wikiName = "wikitab." + ModMain.MODID + ".recipes.name";
	private static RecipeWiki TAB = null;

	public static void preInit() {
		WikiRegistry.registerWikiTab(new RecipeWiki());
	}

	public RecipeWiki() {
		if (TAB == null)
			TAB = this;
	}

	public static RecipeWiki getTAB() {
		return TAB;
	}

	@Override
	public String getName() {
		return wikiName;
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return new ItemStack(Blocks.CRAFTING_TABLE);
	}

	@Override
	public void refreshWiki() {
		ClientProxy.RecipeMap.clear();
		// Altar
		AltarRecipeRegistry.getRecipes().values().forEach(recipe -> {
			try {
				if (recipe.getOutput() != null && !recipe.getOutput().isEmpty()) {
					if (recipe.getOutput().getUnlocalizedName() != null) {
						String blockCode = WikiUtils.getNameFromStack(recipe.getOutput());
						if (!ClientProxy.RecipeMap.containsKey(blockCode) && !recipe.isFillable()) {
							List<AutoRecipe> list = new ArrayList<>();
							list.add(new AutoRecipe(recipe));
							ClientProxy.RecipeMap.put(blockCode, list);
						} 
					} else
						WikiLog.error("Item has no unlocalized name: " + recipe.getOutput().getItem());
				} else
					WikiLog.error("Outpt is null: " + recipe);
			} catch (Throwable e) {
				WikiLog.error("Wiki failed to add recipe handling support for " + recipe.getOutput());
				e.printStackTrace();
			}
		});

		// Forge Recipes
		TartaricForgeRecipeRegistry.getRecipeList().forEach(recipe -> {
			try {
				if (recipe.getRecipeOutput() != null && !recipe.getRecipeOutput().isEmpty()) {
					if (recipe.getRecipeOutput().getUnlocalizedName() != null) {
						String blockCode = WikiUtils.getNameFromStack(recipe.getRecipeOutput());
						// if (!ForgeRecipes.containsKey(blockCode))
						// ForgeRecipes.put(blockCode, recipe);
						if (!ClientProxy.RecipeMap.containsKey(blockCode)) {
							List<AutoRecipe> list = new ArrayList<>();
							list.add(new AutoRecipe(recipe));
							ClientProxy.RecipeMap.put(blockCode, list);
						} else {
							System.out.println("adding vairant of " + blockCode);
							ClientProxy.RecipeMap.get(blockCode).add(new AutoRecipe(recipe));
						}
					} else
						WikiLog.error("Item has no unlocalized name: " + recipe.getRecipeOutput().getItem());
				}
			} catch (Throwable e) {
				WikiLog.error("Wiki failed to add recipe handling support for " + recipe.getRecipeOutput());
				e.printStackTrace();
			}
		});
		// Array / Binding Recipes
		AlchemyArrayRecipeRegistry.getRecipes().values().forEach(array -> {
			if (array != null) {
				SimpleArrayRecipe recipe = new SimpleArrayRecipe(array);
				try {
					if (recipe.getOutput() != null && !recipe.getOutput().isEmpty()) {
						if (recipe.getOutput().getUnlocalizedName() != null) {
							String blockCode = WikiUtils.getNameFromStack(recipe.getOutput());
							// if (!ArrayRecipes.containsKey(blockCode)) {
							// ArrayRecipes.put(blockCode, array);
							// RecipeMap.put(blockCode, new AutoRecipe(recipe));
							// }
							if (!ClientProxy.RecipeMap.containsKey(blockCode)) {
								List<AutoRecipe> list = new ArrayList<>();
								list.add(new AutoRecipe(recipe));
								ClientProxy.RecipeMap.put(blockCode, list);
							} else {
								System.out.println("adding vairant of " + blockCode);
								ClientProxy.RecipeMap.get(blockCode).add(new AutoRecipe(recipe));
							}
						} else
							WikiLog.error("Item has no unlocalized name: " + recipe.getOutput().getItem());
					}
				} catch (Throwable e) {
					WikiLog.error("Wiki failed to add recipe handling support for " + recipe.getOutput());
					e.printStackTrace();
				}
			} else
				WikiLog.error("Binding recipe is null: " + array);
		});
		// Furnace Recipes
		FurnaceRecipes.instance().getSmeltingList().entrySet().forEach(entry -> {
			if (entry.getValue() != null && entry.getValue().getItem() != null) {
				String blockCode = WikiUtils.getNameFromStack(entry.getValue());
				if (!ClientProxy.RecipeMap.containsKey(blockCode)) {
					List<AutoRecipe> list = new ArrayList<>();
					list.add(new AutoRecipe(entry, entry.getValue(), RecipeType.FURNACE));
					ClientProxy.RecipeMap.put(blockCode, list);
				} else {
					System.out.println("adding vairant of " + blockCode);
					ClientProxy.RecipeMap.get(blockCode)
							.add(new AutoRecipe(entry, entry.getValue(), RecipeType.FURNACE));
				}
			}
		});

		ClientProxy.GameRecipes.forEach(recipe -> {
			try {
				if (recipe.getRecipeOutput() != null && !recipe.getRecipeOutput().isEmpty()) {
					if (recipe.getRecipeOutput().getUnlocalizedName() != null) {
						String blockCode = WikiUtils.getNameFromStack(recipe.getRecipeOutput());
						if (WikiUtils.getOwningModId(recipe.getRecipeOutput()).equalsIgnoreCase(Mod.MODID))
							if (!ClientProxy.RecipeMap.containsKey(blockCode)) {
								List<AutoRecipe> list = new ArrayList<>();
								if(recipe instanceof ShapedBloodOrbRecipe || recipe instanceof ShapelessBloodOrbRecipe)
									list.add(new AutoRecipe(recipe,recipe.getRecipeOutput(), RecipeType.ORB));
								else if(recipe.getRecipeOutput().getHasSubtypes())
									list.add(new AutoRecipe(recipe, recipe.getRecipeOutput(), RecipeType.METACRAFT));
								else
									list.add(new AutoRecipe(recipe));
								ClientProxy.RecipeMap.put(blockCode, list);
								System.out.println("Registered: " + blockCode);
							} else {
								System.out.println("adding vairant of " + blockCode);
								ClientProxy.RecipeMap.get(blockCode).add(new AutoRecipe(recipe));
							}
					} else
						WikiLog.error("Item has no unlocalized name: " + recipe.getRecipeOutput().getItem());
				}
			} catch (Throwable e) {
				WikiLog.error("Wiki failed to add recipe handling support for " + recipe.getRecipeOutput());
				e.printStackTrace();
			}

		});
		
	}

	@Override
	public List<IPageLink> getPages(int[] indexes) {
		NonNullList<ItemStack> entries = NonNullList.<ItemStack>create();
		ClientProxy.RecipeMap.forEach((key, recipe) -> {
			ItemStack stack = WikiUtils.getStackFromName(key);
			if (stack != null && !stack.isEmpty() && WikiUtils.getOwningModId(stack).equalsIgnoreCase(Mod.MODID))
				entries.add(stack);
			else if (stack == null || stack.isEmpty())
				System.out.println("Stack empty" + key + ": " + stack);
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

	public static List<String> generatePageFromStack(ItemStack associatedStack) {
		System.out.println("Retrieving recipe: " + associatedStack.getUnlocalizedName());
		NonNullList<String> strings = NonNullList.create();
//		int i = 0;
		int entry = 125;
		String key = WikiUtils.getNameFromStack(associatedStack);
		if (ClientProxy.RecipeMap.containsKey(key) && ClientProxy.RecipeMap.get(key).size() > 0) {
			AutoRecipe recipe = ClientProxy.RecipeMap.get(key).get(0);
//			for (AutoRecipe recipe : list) {
				switch (recipe.getRecipeType()) {
				case ALTAR:
					strings.add(("[" + RecipeType.ALTAR.toString() +"{200," + 10 + ",key=" + key + "}]"));
					break;
				case CRAFT:
					strings.add(("[" + RecipeType.CRAFT.toString() +"{200," + 10 + ",key=" + key + "}]"));
					break;
				case ARRAY:
					strings.add(("[" + RecipeType.ARRAY.toString() +"{200," + 10 + ",key=" + key + "}]"));
					break;
				case ORB:
					strings.add(("[" + RecipeType.ORB.toString() +"{200," + 10 + ",key=" + key + "}]"));
					break;
				case FORGE:
					strings.add(("[" + RecipeType.FORGE.toString() +"{200," + 10 + ",key=" + key + "}]"));
					break;
				case FURNACE:
					strings.add(("[" + RecipeType.FURNACE.toString() +"{200," + 10 + ",key=" + key + "}]"));
					break;
				case METACRAFT:
					if(ClientProxy.RecipeMap.get(key).size() == 1) {
						strings.add(("[" + RecipeType.CRAFT.toString() +"{200," + 10 + ",key=" + key + "}]"));
						break;
					}
					for(int i=0; i < ClientProxy.RecipeMap.get(key).size(); i++) {
						strings.add(("[" + RecipeType.METACRAFT.toString() +"{200," + (10 + entry * i) + "," + i +",key=" + key + "}]"));
					}
					break;
				case COMMENT:
					strings.add(("[" + RecipeType.COMMENT.toString() +"]"));
					break;
				default:
					strings.add("Missing Recipe");
					break;
				}
//			}
		} else {
			System.out.println("Size: " + ClientProxy.RecipeMap.get(key).size());
			strings = null;
		}
		return strings;
	}

}
