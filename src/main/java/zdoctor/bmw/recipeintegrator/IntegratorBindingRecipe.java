package zdoctor.bmw.recipeintegrator;

import java.util.List;

import WayofTime.bloodmagic.registry.ModItems;
import embedded.igwmod.TextureSupplier;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.IReservedSpace;
import embedded.igwmod.gui.IWidget;
import embedded.igwmod.gui.LocatedStack;
import embedded.igwmod.gui.LocatedString;
import embedded.igwmod.gui.LocatedTexture;
import embedded.igwmod.lib.WikiLog;
import net.minecraft.item.ItemStack;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.client.ClientProxy;
import zdoctor.bmw.recipeintegrator.compact.BaseIntegratorRecipe;
import zdoctor.bmw.recipeintegrator.compact.SimpleArrayRecipe;

public class IntegratorBindingRecipe extends BaseIntegratorRecipe {
	public static final int INPUT_X_OFFSET = 1;
	public static final int INPUT_Y_OFFSET = 6;
	public static final int CATALYST_X_OFFSET = INPUT_X_OFFSET + 29;
	public static final int CATALYST_Y_OFFSET = INPUT_Y_OFFSET - 2;
	private static final int RESULT_STACK_X_OFFSET = 74;
	private static final int RESULT_STACK_Y_OFFSET = INPUT_Y_OFFSET;

	@Override
	public String getCommandKey() {
		return "binding";
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
			WikiLog.warning("Manual Altar Recipe not supported yet");
		}
	}

	@Override
	public void addAutomaticCraftingRecipe(String code, List<LocatedStack> locatedStacks, List<IWidget> locatedTextures,
			List<LocatedString> locatedStrings, int x, int y) throws IllegalArgumentException {

		String key = code.substring(4);
		SimpleArrayRecipe recipe = new SimpleArrayRecipe(ClientProxy.ArrayRecipes.get(key));
		locatedStacks.add(new LocatedStack(recipe.getInput().get(0), x + INPUT_X_OFFSET, y + INPUT_Y_OFFSET));
		locatedStacks.add(new LocatedStack(recipe.getCatalyst(), x + CATALYST_X_OFFSET, y + CATALYST_Y_OFFSET));
		locatedStacks.add(new LocatedStack(recipe.getOutput(), x + RESULT_STACK_X_OFFSET, y + RESULT_STACK_Y_OFFSET));
		locatedStacks.add(new LocatedStack(new ItemStack(ModItems.ARCANE_ASHES), x + CATALYST_X_OFFSET + 20,
				y + CATALYST_Y_OFFSET));

	}
}
