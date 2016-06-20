package zdoctor.bmw.recipeintegrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.BiMap;

import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry.AlchemyArrayRecipe;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeJEI;
import WayofTime.bloodmagic.util.helper.NumeralHelper;
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
import zdoctor.bmw.recipeintegrator.compact.SimpleBindingRecipe;

public class IntegratorBindingRecipe implements IRecipeIntegrator {

	public static Map<String, SimpleBindingRecipe> autoMappedRecipes = new HashMap<String, SimpleBindingRecipe>();
	public static final int INPUT_X_OFFSET = 21;
	public static final int INPUT_Y_OFFSET = 1;
	private static final int RESULT_STACK_X_OFFSET = 115;
	private static final int RESULT_STACK_Y_OFFSET = INPUT_Y_OFFSET + 30;

	@Override
	public String getCommandKey() {
		return "altar";
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
		locatedTextures.add(new LocatedTexture(TextureSupplier.getTexture(ModMain.MODID + ":textures/gui/altar.png"), x,
				y, (int) (137 / GuiWiki.TEXT_SCALE), (int) (58 / GuiWiki.TEXT_SCALE)));

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
		SimpleBindingRecipe recipe = autoMappedRecipes.get(key);
		if (recipe != null) {
			Iterator<ItemStack> req = recipe.getInput().iterator();
			if (req.hasNext()) {
				ItemStack ingredientStack = req.next().copy();
				if (ingredientStack != null)
					locatedStacks.add(new LocatedStack(ingredientStack, x + INPUT_X_OFFSET * 18, y + INPUT_Y_OFFSET * 18));
			}

			locatedStacks
					.add(new LocatedStack(recipe.getOutput(), x + RESULT_STACK_X_OFFSET, y + RESULT_STACK_Y_OFFSET));
		} else
			System.out.println("Not Found: " + key);

	}

	public static void mapRecipes() {
		Map<List<ItemStack>, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> alchemyArrayRecipeMap = AlchemyArrayRecipeRegistry
				.getRecipes();

		for (Map.Entry<List<ItemStack>, AlchemyArrayRecipeRegistry.AlchemyArrayRecipe> itemStackAlchemyArrayRecipeEntry : alchemyArrayRecipeMap
				.entrySet()) {
			List<ItemStack> input = itemStackAlchemyArrayRecipeEntry.getValue().getInput();
			BiMap<ItemStackWrapper, AlchemyArrayEffect> catalystMap = itemStackAlchemyArrayRecipeEntry
					.getValue().catalystMap;

			for (Map.Entry<ItemStackWrapper, AlchemyArrayEffect> entry : catalystMap.entrySet()) {
				ItemStack catalyst = entry.getKey().toStack();
				if (AlchemyArrayRecipeRegistry.getAlchemyArrayEffect(input,
						catalyst) instanceof AlchemyArrayEffectBinding) {
					ItemStack output = ((AlchemyArrayEffectBinding) itemStackAlchemyArrayRecipeEntry.getValue()
							.getAlchemyArrayEffectForCatalyst(catalyst)).getOutputStack();

					SimpleBindingRecipe recipe = new SimpleBindingRecipe(input, catalyst, output);
					String key = recipe.getOutput().getUnlocalizedName().replace("item.", "item/").replace("tile.",
							"block/");

					if (!autoMappedRecipes.containsKey(key)) {
						autoMappedRecipes.put(key, recipe);
						System.out.println("Binding: " + key);
					}
				}
			}
		}
	}

}
