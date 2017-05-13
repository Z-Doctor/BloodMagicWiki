package zdoctor.bmw.wiki.registry.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import WayofTime.bloodmagic.api.Constants.Mod;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
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
		if(TAB == null)
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
		ClientProxy.GameRecipes.forEach(recipe -> {
			try {
				if (recipe.getRecipeOutput() != null && !recipe.getRecipeOutput().isEmpty()) {
					if (recipe.getRecipeOutput().getUnlocalizedName() != null) {
						String blockCode = WikiUtils.getNameFromStack(recipe.getRecipeOutput());
						if (!ClientProxy.RecipeMap.containsKey(blockCode)) {
							List<AutoRecipe> list = new ArrayList<>();
							list.add(new AutoRecipe(recipe));
							ClientProxy.RecipeMap.put(blockCode, list);
							System.out.println("Registered: " + blockCode);
						} else if (recipe != null) {
							System.out.println("adding vairant of " + blockCode);
							ClientProxy.RecipeMap.get(blockCode).add(new AutoRecipe(recipe));
						}

						// if
						// (WikiUtils.getOwningModId(recipe.getRecipeOutput()).equalsIgnoreCase(Constants.Mod.MODID))
						// {
						// if (!BloodMagicRecipes.containsKey(blockCode)) {
						// BloodMagicRecipes.put(blockCode, recipe);
						// // System.out.println("Registered: " +
						// // blockCode);
						// }
						// }
					} else
						WikiLog.error("Item has no unlocalized name: " + recipe.getRecipeOutput().getItem());
				}
			} catch (Throwable e) {
				WikiLog.error("Wiki failed to add recipe handling support for " + recipe.getRecipeOutput());
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
		// Altar Recipes
		// AltarRecipeRegistry.getRecipes().values().forEach(recipe -> {
		// try {
		// if (recipe.getOutput() != null && !recipe.getOutput().isEmpty()) {
		// if (recipe.getOutput().getUnlocalizedName() != null) {
		// String blockCode = WikiUtils.getNameFromStack(recipe.getOutput());
		//// if (!AltarRecipes.containsKey(blockCode) && !recipe.isFillable()) {
		//// AltarRecipes.put(blockCode, recipe);
		//// // System.out.println("Added: " + blockCode);
		//// }
		// if (!RecipeMap.containsKey(blockCode)) {
		// List<AutoRecipe> list = new ArrayList<>();
		// list.add(new AutoRecipe(recipe));
		// RecipeMap.put(blockCode, list);
		// } else {
		// List<AutoRecipe> list = RecipeMap.get(blockCode);
		// list.add(new AutoRecipe(recipe));
		// RecipeMap.put(blockCode, list);
		// }
		// } else
		// WikiLog.error("Item has no unlocalized name: " +
		// recipe.getOutput().getItem());
		// } else
		// WikiLog.error("Outpt is null: " + recipe);
		// } catch (Throwable e) {
		// WikiLog.error("Wiki failed to add recipe handling support for " +
		// recipe.getOutput());
		// e.printStackTrace();
		// }
		// });
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
				// if
				// (!IntegratorFurnace.autoMappedFurnaceRecipes.containsKey(blockCode))
				// IntegratorFurnace.autoMappedFurnaceRecipes.put(blockCode,
				// entry.getKey());
				if (!ClientProxy.RecipeMap.containsKey(blockCode)) {
					List<AutoRecipe> list = new ArrayList<>();
					list.add(new AutoRecipe(entry, entry.getValue(), RecipeType.FURNACE));
					ClientProxy.RecipeMap.put(blockCode, list);
				} else {
					System.out.println("adding vairant of " + blockCode);
					ClientProxy.RecipeMap.get(blockCode).add(new AutoRecipe(entry, entry.getValue(), RecipeType.FURNACE));
				}
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
				System.out.println("Stack empty");
		});

		//// ClientProxy.AltarRecipes.forEach((key, recipe) -> {
		// if (recipe != null) {
		// if (!recipe.isFillable())
		// if (!RecipeMap.containsKey(key) && !key.contains("bucketFilled")) {
		// System.out.println("System: " +
		// recipe.getOutput().getUnlocalizedName());
		// RecipeMap.put(key, new AutoRecipe(recipe));

		// }
		// }
		// });
		// ClientProxy.ForgeRecipes.forEach((key, recipe) -> {
		// if (recipe != null) {
		//// String key = recipe.getRecipeOutput().getUnlocalizedName();
		// RecipeMap.put(key, new AutoRecipe(recipe));
		// entries.add(recipe.getRecipeOutput());
		// }
		// });

		// ClientProxy.ArrayRecipes.forEach((key, recipe2) -> {
		// if (recipe2 != null) {
		// SimpleArrayRecipe recipe = new SimpleArrayRecipe(recipe2);
		// RecipeMap.put(key, new AutoRecipe(recipe));
		// entries.add(recipe.getOutput());
		// }
		// });

		// ClientProxy.MappedRecipes.forEach((key, recipe2) -> {
		// if (recipe2 != null) {
		// if (recipe2 instanceof ShapedBloodOrbRecipe) {
		// ShapedBloodOrbRecipe recipe = (ShapedBloodOrbRecipe) recipe2;
		//// String key = recipe.getRecipeOutput().getUnlocalizedName();
		// RecipeMap.put(key, new AutoRecipe(recipe));
		// entries.add(recipe.getRecipeOutput());
		// } else if(recipe2 instanceof ShapelessBloodOrbRecipe) {
		// ShapelessBloodOrbRecipe recipe = (ShapelessBloodOrbRecipe) recipe2;
		//// String key = recipe.getRecipeOutput().getUnlocalizedName();
		// RecipeMap.put(key, new AutoRecipe(recipe));
		// entries.add(recipe.getRecipeOutput());
		// } else {
		// RecipeMap.put(key, new AutoRecipe(recipe2));
		// entries.add(recipe2.getRecipeOutput());
		// }
		// }
		// });

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

	// public static List<String> generatePageFromStack(ItemStack
	// associatedStack, int x, int y) {
	// System.out.println("Retrieving recipe: " +
	// associatedStack.getUnlocalizedName());
	// NonNullList<String> strings = NonNullList.create();
	// String key = WikiUtils.getNameFromStack(associatedStack);
	// if (RecipeMap.containsKey(associatedStack.getUnlocalizedName())) {
	// AutoRecipe recipe = RecipeMap.get(associatedStack.getUnlocalizedName());
	// switch (recipe.getRecipeType()) {
	// case ALTAR:
	// strings.add(("[altar{" + x + "," + y + ",key=" + key + "}]"));
	// break;
	// case CRAFT:
	// strings.add(("[craft{" + x + "," + y + ",key=" + key + "}]"));
	// break;
	// case ARRAY:
	// strings.add(("[array{" + x + "," + y + ",key=" + key + "}]"));
	// break;
	// case ORB:
	// strings.add(("[orbcrafting{" + x + "," + y + ",key=" + key + "}]"));
	// break;
	// case FORGE:
	// strings.add(("[hellfire{" + x + "," + y + ",key=" + key + "}]"));
	// break;
	// }
	// } else
	// strings = null;
	// return strings;
	// }

	public static List<String> generatePageFromStack(ItemStack associatedStack) {
		System.out.println("Retrieving recipe: " + associatedStack.getUnlocalizedName());
		NonNullList<String> strings = NonNullList.create();
		int i = 0;
		int entry = 125;
		String key = WikiUtils.getNameFromStack(associatedStack);
		if (ClientProxy.RecipeMap.containsKey(key)) {
			List<AutoRecipe> list = ClientProxy.RecipeMap.get(key);
			for (AutoRecipe recipe : list) {
				switch (recipe.getRecipeType()) {
				case ALTAR:
					strings.add(("[altar{200," + (10 + entry * i++) + ",key=" + key + "}]"));
					break;
				case CRAFT:
					strings.add(("[crafting{200," + (10 + entry * i++) + ",key=" + key + "}]"));
					break;
				case ARRAY:
					strings.add(("[array{200," + (10 + entry * i++) + ",key=" + key + "}]"));
					break;
				case ORB:
					strings.add(("[orbcrafting{200," + (10 + entry * i++) + ",key=" + key + "}]"));
					break;
				case FORGE:
					strings.add(("[hellfire{200," + (10 + entry * i++) + ",key=" + key + "}]"));
					break;
				case FURNACE:
					strings.add(("[furnace{200," + (10 + entry * i++) + ",key=" + key + "}]"));
					break;
				default:
					strings.add("Missing Recipe");
					break;
				}
			}
		} else
			strings = null;
		return strings;
	}

}
