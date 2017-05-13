package zdoctor.bmw.recipeintegrator;

import java.util.List;

import embedded.igwmod.InfoSupplier;
import embedded.igwmod.gui.IReservedSpace;
import embedded.igwmod.gui.IWidget;
import embedded.igwmod.gui.LocatedStack;
import embedded.igwmod.gui.LocatedString;
import zdoctor.bmw.client.ClientProxy;
import zdoctor.bmw.recipeintegrator.compact.AutoRecipe;
import zdoctor.bmw.recipeintegrator.compact.AutoRecipe.RecipeType;
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
		if (arguments[arguments.length-1].startsWith("key=")) {
			String key = arguments[arguments.length-1].replaceFirst("key=", "");
//			ItemStack stack = WikiUtils.getStackFromName(key);

			String code = "";
			for (String arg : arguments) {
				code += arg + ",";
			}
			code = getType(key) + "{" + code.substring(0, code.length()-1) + "}";
			System.out.println("End Code: " + code);
			InfoSupplier.decomposeTemplate(code, reservedSpaces, locatedStrings, locatedStacks, locatedTextures);
			// locatedTextures.add(new
			// LocatedTexture(TextureSupplier.getTexture(ModMain.MODID + ":" +
			// getTextureName(key)), x,
			// y, (int) (137 / GuiWiki.TEXT_SCALE), (int) (58 /
			// GuiWiki.TEXT_SCALE)));

			// addAutomaticCraftingRecipe(key, locatedStacks, locatedTextures,
			// locatedStrings,
			// (int) (x * GuiWiki.TEXT_SCALE), (int) (y * GuiWiki.TEXT_SCALE));
		} else
			throw new IllegalArgumentException("Recipes must at least end with key=");

	}

	@Override
	public void addAutomaticCraftingRecipe(String string, List<LocatedStack> locatedStacks,
			List<IWidget> locatedTextures, List<LocatedString> locatedStrings, int i, int j) {
		// TODO Auto-generated method stub

	}

	private static String getType(String key) {
		String type = RecipeType.COMMENT.toString();
		if (ClientProxy.RecipeMap.containsKey(key)) {
//			System.out.println("Key found: " + RecipeWiki.RecipeMap.get(key).getRecipeType());
//			for (AutoRecipe auto : ClientProxy.RecipeMap.get(key)) {
//				auto.getRecipeType()
//			}
			type = ClientProxy.RecipeMap.get(key).get(0).getRecipeType().toString();
		}
		return type;

	}

}
