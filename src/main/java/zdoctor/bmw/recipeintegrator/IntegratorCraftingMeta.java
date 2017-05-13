package zdoctor.bmw.recipeintegrator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import embedded.igwmod.TextureSupplier;
import embedded.igwmod.WikiUtils;
import embedded.igwmod.api.IRecipeIntegrator;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.IReservedSpace;
import embedded.igwmod.gui.IWidget;
import embedded.igwmod.gui.LocatedStack;
import embedded.igwmod.gui.LocatedString;
import embedded.igwmod.gui.LocatedTexture;
import embedded.igwmod.lib.Paths;
import embedded.igwmod.lib.WikiLog;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.client.ClientProxy;
import zdoctor.bmw.wiki.events.CraftingRetrievalEvent;

public class IntegratorCraftingMeta implements IRecipeIntegrator {
	public static final int STACKS_X_OFFSET = 1;
	public static final int STACKS_Y_OFFSET = 1;
	private static final int RESULT_STACK_X_OFFSET = 95;
	private static final int RESULT_STACK_Y_OFFSET = STACKS_Y_OFFSET + 18;

	@Override
	public String getCommandKey() {
		return "meta";
	}

	@Override
	public void onCommandInvoke(String[] arguments, List<IReservedSpace> reservedSpaces,
			List<LocatedString> locatedStrings, List<LocatedStack> locatedStacks, List<IWidget> locatedTextures)
			throws IllegalArgumentException {
		if (arguments.length < 4)
			throw new IllegalArgumentException("Code needs at least 4 arguments!");
		int x;
		try {
			x = Integer.parseInt(arguments[0]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"The first parameter (the x coordinate) contains an invalid number. Check for spaces or invalid characters!");
		}
		int y;
		try {
			y = Integer.parseInt(arguments[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"The second parameter (the y coordinate) contains an invalid number. Check for spaces or invalid characters!");
		}
		locatedTextures.add(
				new LocatedTexture(TextureSupplier.getTexture(Paths.MOD_ID_WITH_COLON + "textures/GuiCrafting.png"), x,
						y, (int) (116 / GuiWiki.TEXT_SCALE), (int) (54 / GuiWiki.TEXT_SCALE)));
		int meta;
		try {
			meta = Integer.parseInt(arguments[2]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"The third parameter (the meta) contains an invalid number. Check for spaces or invalid characters!");
		}

		if (arguments[3].startsWith("key=")) {
			if (arguments.length != 4)
				throw new IllegalArgumentException(
						"An RecipeRetrievalEvent crafting code can only have 3 parameters: x, y, meta and the key!");
//			ItemStack stack = new ItemStack( )
			addAutomaticCraftingRecipe(arguments[3], meta, locatedStacks, locatedTextures, locatedStrings,
					(int) (x * GuiWiki.TEXT_SCALE), (int) (y * GuiWiki.TEXT_SCALE));
		} else {
			addManualCraftingRecipe(arguments, locatedStacks, locatedTextures, (int) (x * GuiWiki.TEXT_SCALE),
					(int) (y * GuiWiki.TEXT_SCALE));
		}
	}

	/**
	 * Check RecipeRetrievalEvent to see what this method does.
	 * 
	 * @param y
	 * @param x
	 */
	private void addAutomaticCraftingRecipe(String code, int meta, List<LocatedStack> locatedStacks,
			List<IWidget> locatedTextures, List<LocatedString> locatedStrings, int x, int y)
			throws IllegalArgumentException {
		String key = code.substring(4);
		CraftingRetrievalEvent recipeEvent = new CraftingRetrievalEvent(key);
		IRecipe autoMappedRecipe = null;
		try {
			autoMappedRecipe = ClientProxy.RecipeMap.get(key).get(meta).getRecipe(IRecipe.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("Fire Event: looking for: " + key);
		if (autoMappedRecipe != null) {
			recipeEvent.recipe = autoMappedRecipe;
		} else {
			MinecraftForge.EVENT_BUS.post(recipeEvent);
		}
		if (recipeEvent.recipe instanceof ShapedRecipes) {
			ShapedRecipes recipe = (ShapedRecipes) recipeEvent.recipe;
			for (int i = 0; i < recipe.recipeHeight; i++) {
				for (int j = 0; j < recipe.recipeWidth; j++) {
					ItemStack ingredientStack = recipe.recipeItems[i * recipe.recipeWidth + j];
					if (ingredientStack != null) {
						locatedStacks.add(new LocatedStack(ingredientStack, x + STACKS_X_OFFSET + j * 18,
								y + STACKS_Y_OFFSET + i * 18));
					}
				}
			}
			locatedStacks.add(
					new LocatedStack(recipe.getRecipeOutput(), x + RESULT_STACK_X_OFFSET, y + RESULT_STACK_Y_OFFSET));
			locatedStrings.add(new LocatedString(I18n.format(ModMain.MODID + ".gui.crafting.shaped"), x * 2 + 120, y * 2 + 10,
					0xFF000000, false));
		} else if (recipeEvent.recipe instanceof ShapedOreRecipe) {
			ShapedOreRecipe recipe = (ShapedOreRecipe) recipeEvent.recipe;
			int recipeHeight = 0;
			int recipeWidth = 0;
			try {
				recipeHeight = ReflectionHelper.findField(ShapedOreRecipe.class, "height").getInt(recipe);
				recipeWidth = ReflectionHelper.findField(ShapedOreRecipe.class, "width").getInt(recipe);
			} catch (Exception e) {
				WikiLog.error(
						"Something went wrong while trying to get the width and height fields from ShapedOreRecipe!");
				e.printStackTrace();
			}
			for (int i = 0; i < recipeHeight; i++) {
				for (int j = 0; j < recipeWidth; j++) {
					Object ingredient = recipe.getInput()[i * recipeWidth + j];
					if (ingredient != null) {
						ItemStack ingredientStack = ingredient instanceof ItemStack ? (ItemStack) ingredient
								: ((List<ItemStack>) ingredient).get(0);
						if (ingredientStack != null) {
							locatedStacks.add(new LocatedStack(ingredientStack, x + STACKS_X_OFFSET + j * 18,
									y + STACKS_Y_OFFSET + i * 18));
						}
					}
				}
			}
			locatedStacks.add(
					new LocatedStack(recipe.getRecipeOutput(), x + RESULT_STACK_X_OFFSET, y + RESULT_STACK_Y_OFFSET));
			locatedStrings.add(new LocatedString(I18n.format(ModMain.MODID + ".gui.crafting.shaped"), x * 2 + 120, y * 2 + 10,
					0xFF000000, false));
		} else if (recipeEvent.recipe instanceof ShapelessRecipes) {
			ShapelessRecipes recipe = (ShapelessRecipes) recipeEvent.recipe;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (i * 3 + j < recipe.recipeItems.size()) {
						ItemStack ingredientStack = (ItemStack) recipe.recipeItems.get(i * 3 + j);
						if (ingredientStack != null) {
							locatedStacks.add(new LocatedStack(ingredientStack, x + STACKS_X_OFFSET + j * 18,
									y + STACKS_Y_OFFSET + i * 18));
						}
					}
				}
			}
			locatedStacks.add(
					new LocatedStack(recipe.getRecipeOutput(), x + RESULT_STACK_X_OFFSET, y + RESULT_STACK_Y_OFFSET));
			locatedStrings.add(new LocatedString(I18n.format(ModMain.MODID + ".gui.crafting.shapeless"), x * 2 + 120, y * 2 + 10,
					0xFF000000, false));
		} else if (recipeEvent.recipe instanceof ShapelessOreRecipe) {
			ShapelessOreRecipe recipe = (ShapelessOreRecipe) recipeEvent.recipe;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (i * 3 + j < recipe.getInput().size()) {
						Object ingredient = recipe.getInput().get(i * 3 + j);
						if (ingredient != null) {
							ItemStack ingredientStack = ingredient instanceof ItemStack ? (ItemStack) ingredient
									: ((List<ItemStack>) ingredient).get(0);
							if (ingredientStack != null) {
								locatedStacks.add(new LocatedStack(ingredientStack, x + STACKS_X_OFFSET + j * 18,
										y + STACKS_Y_OFFSET + i * 18));
							}
						}
					}
				}
			}
			locatedStacks.add(
					new LocatedStack(recipe.getRecipeOutput(), x + RESULT_STACK_X_OFFSET, y + RESULT_STACK_Y_OFFSET));
			locatedStrings.add(new LocatedString(I18n.format(ModMain.MODID + ".gui.crafting.shapeless"), x * 2 + 120, y * 2 + 10,
					0xFF000000, false));
		} else if (recipeEvent.recipe == null) {
			throw new IllegalArgumentException(
					"RecipeRetrievalEvent: For the given key, no subscriber returned a recipe! key = " + key);
		} else {
			throw new IllegalArgumentException(
					"RecipeRetrievalEvent: Don't pass anything other than ShapedRecipes, ShapedOreRecipes, ShapelessRecipes or ShapelessOreRecipe! key = "
							+ key);
		}
	}

	private void addManualCraftingRecipe(String[] codeParts, List<LocatedStack> locatedStacks,
			List<IWidget> locatedTextures, int x, int y) throws IllegalArgumentException {
		String[] ingredients = new String[codeParts.length - 3];
		for (int i = 3; i < codeParts.length; i++)
			ingredients[i - 3] = codeParts[i];
		// ingredients[codeParts.length - 2] = lastTwoArguments[0];
		String result = codeParts[2];
		Map<String, ItemStack> ingredientMap = new HashMap<String, ItemStack>();
		for (int i = 3; i < ingredients.length; i++) {
			String[] ingredient = ingredients[i].split("=");
			ingredientMap.put(ingredient[0], WikiUtils.getStackFromName(ingredient[1]));
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				ItemStack ingredientStack = ingredientMap.get(ingredients[i].substring(j, j + 1));
				if (ingredientStack != null) {
					locatedStacks.add(new LocatedStack(ingredientStack, x + STACKS_X_OFFSET + j * 18,
							y + STACKS_Y_OFFSET + i * 18));
				}
			}
		}
		ItemStack resultStack = WikiUtils.getStackFromName(result);
		if (resultStack != null) {
			locatedStacks.add(new LocatedStack(resultStack, x + RESULT_STACK_X_OFFSET, y + RESULT_STACK_Y_OFFSET));
		}
	}

}