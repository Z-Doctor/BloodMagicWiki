package zdoctor.bmw.recipeintegrator;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.soul.IDemonWillGem;
import WayofTime.bloodmagic.compat.jei.forge.TartaricForgeRecipeJEI;
import igwmod.TextureSupplier;
import igwmod.api.IRecipeIntegrator;
import igwmod.gui.GuiWiki;
import igwmod.gui.IReservedSpace;
import igwmod.gui.IWidget;
import igwmod.gui.LocatedStack;
import igwmod.gui.LocatedString;
import igwmod.gui.LocatedTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import zdoctor.bmw.ModMain;

public class IntegratorHellfireRecipe implements IRecipeIntegrator {

	public static Map<String, TartaricForgeRecipe> autoMappedRecipes = new HashMap<String, TartaricForgeRecipe>();
	public static final int STACKS_X_OFFSET = 1;
	public static final int STACKS_Y_OFFSET = 1;
	private static final int RESULT_STACK_X_OFFSET = 74;
	private static final int RESULT_STACK_Y_OFFSET = STACKS_Y_OFFSET + 13;
	private static final int GEM_STACK_X_OFFSET = 43;
	private static final int GEM_STACK_Y_OFFSET = STACKS_Y_OFFSET;

	@Override
	public String getCommandKey() {
		return "hellfire";
	}

	@Override
	public void onCommandInvoke(String[] arguments, List<IReservedSpace> reservedSpaces,
			List<LocatedString> locatedStrings, List<LocatedStack> locatedStacks, List<IWidget> locatedTextures)
			throws IllegalArgumentException {
		if (arguments.length < 3)
			throw new IllegalArgumentException("Code needs at least 3 arguments!");
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
		locatedTextures
				.add(new LocatedTexture(TextureSupplier.getTexture(ModMain.MODID + ":textures/gui/soulForge.png"), x, y,
						(int) (96 / GuiWiki.TEXT_SCALE), (int) (36 / GuiWiki.TEXT_SCALE)));

		if (arguments[2].startsWith("key=")) {
			if (arguments.length != 3)
				throw new IllegalArgumentException(
						"An RecipeRetrievalEvent crafting code can only have 3 parameters: x, y and the key!");
			addAutomaticCraftingRecipe(arguments[2], locatedStacks, locatedTextures, locatedStrings,
					(int) (x * GuiWiki.TEXT_SCALE), (int) (y * GuiWiki.TEXT_SCALE));
		} else {
			addManualCraftingRecipe(arguments, locatedStacks, locatedTextures, (int) (x * GuiWiki.TEXT_SCALE),
					(int) (y * GuiWiki.TEXT_SCALE));
		}
	}

	private void addAutomaticCraftingRecipe(String code, List<LocatedStack> locatedStacks,
			List<IWidget> locatedTextures, List<LocatedString> locatedStrings, int x, int y)
			throws IllegalArgumentException {
		String key = code.substring(4);
		final TartaricForgeRecipe recipe = autoMappedRecipes.get(key);
		if (recipe != null) {
			Iterator<Object> req = recipe.getInput().iterator();
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					if (req.hasNext()) {
						Object obj = req.next();
						ItemStack ingredientStack = null;
						if (obj instanceof ItemStack) {
							ingredientStack = ((ItemStack) obj).copy();

						} else if (obj instanceof List) {
							Iterator<ItemStack> stackList = ((List<ItemStack>) obj).iterator();
							while (stackList.hasNext()) {
								ingredientStack = stackList.next();
							}
						}
						if (ingredientStack != null)
							locatedStacks.add(new LocatedStack(ingredientStack, x + STACKS_X_OFFSET + j * 18,
									y + STACKS_Y_OFFSET + i * 18));
					} else
						break;
				}
				TartaricForgeRecipeJEI jeiRecipe = new TartaricForgeRecipeJEI(recipe);
				ItemStack gem = jeiRecipe.getValidGems().get(0);
				if (gem.getItem() instanceof IDemonWill) {
					IDemonWill item = (IDemonWill) gem.getItem();
					item.setWill(gem, recipe.getMinimumSouls());
				} else if (gem.getItem() instanceof IDemonWillGem) {
					IDemonWillGem item = (IDemonWillGem) gem.getItem();
					item.setWill(EnumDemonWillType.DEFAULT, gem, recipe.getMinimumSouls());
				}

				locatedStacks.add(new LocatedStack(gem, x + GEM_STACK_X_OFFSET, y + GEM_STACK_Y_OFFSET));
				locatedStacks.add(new LocatedStack(recipe.getRecipeOutput(), x + RESULT_STACK_X_OFFSET,
						y + RESULT_STACK_Y_OFFSET));
				locatedStrings
						.add(new LocatedString(
								I18n.format("intergrator." + ModMain.MODID.toLowerCase() + ".minimumSouls.forge") + ": "
										+ recipe.getMinimumSouls(),
								x * 2 + 125, y * 2 - 1, Color.gray.getRGB(), false));
				locatedStrings
						.add(new LocatedString(
								I18n.format("intergrator." + ModMain.MODID.toLowerCase() + ".minimumDrain.forge") + ": "
										+ recipe.getSoulsDrained(),
								x * 2 + 125, y * 2 + 8, Color.gray.getRGB(), false));
			}
		} else
			System.out.println("Not Found: " + key);
	}

	private void addManualCraftingRecipe(String[] codeParts, List<LocatedStack> locatedStacks,
			List<IWidget> locatedTextures, int x, int y) throws IllegalArgumentException {
		String[] ingredients = new String[codeParts.length - 3];
		// TODO - Add Manual Support
		// for (int i = 3; i < codeParts.length; i++)
		// ingredients[i - 3] = codeParts[i];
		// // ingredients[codeParts.length - 2] = lastTwoArguments[0];
		// String result = codeParts[2];
		// Map<String, ItemStack> ingredientMap = new HashMap<String,
		// ItemStack>();
		// for (int i = 3; i < ingredients.length; i++) {
		// String[] ingredient = ingredients[i].split("=");
		// ingredientMap.put(ingredient[0],
		// WikiUtils.getStackFromName(ingredient[1]));
		// }
		// for (int i = 0; i < 3; i++) {
		// for (int j = 0; j < 3; j++) {
		// ItemStack ingredientStack =
		// ingredientMap.get(ingredients[i].substring(j, j + 1));
		// if (ingredientStack != null) {
		// locatedStacks.add(new LocatedStack(ingredientStack, x +
		// STACKS_X_OFFSET + j * 18,
		// y + STACKS_Y_OFFSET + i * 18));
		// }
		// }
		// }
		// ItemStack resultStack = WikiUtils.getStackFromName(result);
		// if (resultStack != null) {
		// locatedStacks.add(new LocatedStack(resultStack, x +
		// RESULT_STACK_X_OFFSET, y + RESULT_STACK_Y_OFFSET));
		// }
	}

	public static void mapRecipes() {
		Iterator<TartaricForgeRecipe> gemRecipes = TartaricForgeRecipeRegistry.getRecipeList().iterator();
		while (gemRecipes.hasNext()) {
			TartaricForgeRecipe recipe = gemRecipes.next();
			String key = recipe.getRecipeOutput().getUnlocalizedName().replace("item.", "item/").replace("tile.",
					"block/");
			if (!autoMappedRecipes.containsKey(key)) {
				autoMappedRecipes.put(key, recipe);
				// System.out.println("Forge: " + key);
			}
		}
	}

}
