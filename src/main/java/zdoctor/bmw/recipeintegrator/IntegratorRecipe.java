package zdoctor.bmw.recipeintegrator;

import java.util.List;

import embedded.igwmod.InfoSupplier;
import embedded.igwmod.gui.IReservedSpace;
import embedded.igwmod.gui.IWidget;
import embedded.igwmod.gui.LocatedStack;
import embedded.igwmod.gui.LocatedString;
import zdoctor.bmw.client.ClientProxy;
import zdoctor.bmw.recipeintegrator.compact.BaseIntegratorRecipe;

public class IntegratorRecipe extends BaseIntegratorRecipe {
	public static final int STACKS_X_OFFSET = 21;
	public static final int STACKS_Y_OFFSET = 1;
	public static final int RESULT_STACK_X_OFFSET = 115;
	public static final int RESULT_STACK_Y_OFFSET = STACKS_Y_OFFSET + 30;

	@Override
	public String getCommandKey() {
		return "recipe";
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

		if (arguments[2].startsWith("key=")) {
			if (arguments.length != 3)
				throw new IllegalArgumentException(
						"An RecipeRetrievalEvent crafting code can only have 3 parameters: x, y and the key!");
			String key = arguments[2].replaceFirst("key=", "");
//			ItemStack stack = WikiUtils.getStackFromName(key);

			String code = getType(key) + "{" + x + "," + y + ",key=" + key + "}";
			System.out.println("Code: " + code);
			InfoSupplier.decomposeTemplate(code, reservedSpaces, locatedStrings, locatedStacks, locatedTextures);
			// locatedTextures.add(new
			// LocatedTexture(TextureSupplier.getTexture(ModMain.MODID + ":" +
			// getTextureName(key)), x,
			// y, (int) (137 / GuiWiki.TEXT_SCALE), (int) (58 /
			// GuiWiki.TEXT_SCALE)));

			// addAutomaticCraftingRecipe(key, locatedStacks, locatedTextures,
			// locatedStrings,
			// (int) (x * GuiWiki.TEXT_SCALE), (int) (y * GuiWiki.TEXT_SCALE));
		}

	}

	@Override
	public void addAutomaticCraftingRecipe(String string, List<LocatedStack> locatedStacks,
			List<IWidget> locatedTextures, List<LocatedString> locatedStrings, int i, int j) {
		// TODO Auto-generated method stub

	}

	private static String getType(String key) {
		String type = "comment";
		if (ClientProxy.RecipeMap.containsKey(key)) {
//			System.out.println("Key found: " + RecipeWiki.RecipeMap.get(key).getRecipeType());
			switch (ClientProxy.RecipeMap.get(key).get(0).getRecipeType()) {
			case ALTAR:
				type = "altar";
				break;
			case ORB:
				type = "orbcrafting";
				break;
			case ARRAY:
				type = "array";
				break;
			case CRAFT:
				type = "crafting";
				break;
			case FORGE:
				type = "hellfire";
				break;
			case FURNACE:
				type = "furnace";
				break;
			}
		}
		return type;

	}

}
