package zdoctor.bmw.recipeintegrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;

import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.compat.jei.alchemyArray.AlchemyArrayCraftingRecipeJEI;
import WayofTime.bloodmagic.registry.ModItems;
import net.minecraft.item.ItemStack;
import tweaked.igwmod.TextureSupplier;
import tweaked.igwmod.api.IRecipeIntegrator;
import tweaked.igwmod.gui.GuiWiki;
import tweaked.igwmod.gui.IReservedSpace;
import tweaked.igwmod.gui.IWidget;
import tweaked.igwmod.gui.LocatedStack;
import tweaked.igwmod.gui.LocatedString;
import tweaked.igwmod.gui.LocatedTexture;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.recipeintegrator.compact.SimpleArrayRecipe;

public class IntegratorArrayCraftingRecipe implements IRecipeIntegrator {

	public static Map<String, SimpleArrayRecipe> autoMappedRecipes = new HashMap<String, SimpleArrayRecipe>();
	public static final int INPUT_X_OFFSET = 1;
	public static final int INPUT_Y_OFFSET = 6;
	public static final int CATALYST_X_OFFSET = INPUT_X_OFFSET + 29;
	public static final int CATALYST_Y_OFFSET = INPUT_Y_OFFSET - 2;
	private static final int RESULT_STACK_X_OFFSET = 74;
	private static final int RESULT_STACK_Y_OFFSET = INPUT_Y_OFFSET;

	@Override
	public String getCommandKey() {
		return "array";
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
		locatedTextures.add(new LocatedTexture(TextureSupplier.getTexture(ModMain.MODID + ":textures/gui/binding.png"),
				x, y, (int) (96 / GuiWiki.TEXT_SCALE), (int) (28 / GuiWiki.TEXT_SCALE)));

		if (arguments[2].startsWith("key=")) {
			if (arguments.length != 3)
				throw new IllegalArgumentException(
						"An RecipeRetrievalEvent crafting code can only have 3 parameters: x, y and the key!");
			addAutomaticCraftingRecipe(arguments[2], locatedStacks, locatedTextures, locatedStrings,
					(int) (x * GuiWiki.TEXT_SCALE), (int) (y * GuiWiki.TEXT_SCALE));
		} else {
			System.out.println("Manual Altar Recipe not supporte");
		}
	}

	private void addAutomaticCraftingRecipe(String code, List<LocatedStack> locatedStacks,
			List<IWidget> locatedTextures, List<LocatedString> locatedStrings, int x, int y)
			throws IllegalArgumentException {

		String key = code.substring(4);
		SimpleArrayRecipe recipe = autoMappedRecipes.get(key);
		if (recipe != null) {
			locatedStacks.add(new LocatedStack(recipe.getInput().get(0), x + INPUT_X_OFFSET, y + INPUT_Y_OFFSET));
			locatedStacks.add(new LocatedStack(recipe.getCatalyst(), x + CATALYST_X_OFFSET, y + CATALYST_Y_OFFSET));
			locatedStacks
					.add(new LocatedStack(recipe.getOutput(), x + RESULT_STACK_X_OFFSET, y + RESULT_STACK_Y_OFFSET));
			locatedStacks.add(new LocatedStack(new ItemStack(ModItems.arcaneAshes), x + CATALYST_X_OFFSET + 20,
					y + CATALYST_Y_OFFSET));
		} else
			System.out.println("Not Found: " + key);

	}

	public static void mapRecipes() {
		Map<List<ItemStack>, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> alchemyArrayRecipeMap = AlchemyArrayRecipeRegistry
				.getRecipes();

		ArrayList<AlchemyArrayCraftingRecipeJEI> recipes = new ArrayList<AlchemyArrayCraftingRecipeJEI>();

		for (Map.Entry<List<ItemStack>, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> itemStackAlchemyArrayRecipeEntry : alchemyArrayRecipeMap
				.entrySet()) {
			List<ItemStack> input = itemStackAlchemyArrayRecipeEntry.getValue().getInput();
			BiMap<ItemStackWrapper, AlchemyArrayEffect> catalystMap = itemStackAlchemyArrayRecipeEntry
					.getValue().catalystMap;

			for (Map.Entry<ItemStackWrapper, AlchemyArrayEffect> entry : catalystMap.entrySet()) {
				ItemStack catalyst = entry.getKey().toStack();
				if (AlchemyArrayRecipeRegistry.getAlchemyArrayEffect(input,
						catalyst) instanceof AlchemyArrayEffectCrafting) {
					ItemStack output = ((AlchemyArrayEffectCrafting) itemStackAlchemyArrayRecipeEntry.getValue()
							.getAlchemyArrayEffectForCatalyst(catalyst)).getOutputStack();

					SimpleArrayRecipe recipe = new SimpleArrayRecipe(input, catalyst, output);
					String key = recipe.getOutput().getUnlocalizedName().replace("item.", "item/").replace("tile.",
							"block/");

					if (!autoMappedRecipes.containsKey(key)) {
						autoMappedRecipes.put(key, recipe);
						// System.out.println("Arrary: " + key);
					}
				}
			}
		}
	}

}
