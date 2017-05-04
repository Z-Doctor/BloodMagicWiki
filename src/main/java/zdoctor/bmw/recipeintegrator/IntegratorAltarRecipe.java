package zdoctor.bmw.recipeintegrator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.util.helper.NumeralHelper;
import embedded.igwmod.TextureSupplier;
import embedded.igwmod.api.IRecipeIntegrator;
import embedded.igwmod.gui.GuiWiki;
import embedded.igwmod.gui.IReservedSpace;
import embedded.igwmod.gui.IWidget;
import embedded.igwmod.gui.LocatedStack;
import embedded.igwmod.gui.LocatedString;
import embedded.igwmod.gui.LocatedTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.client.ClientProxy;

public class IntegratorAltarRecipe implements IRecipeIntegrator {

	public static Map<String, AltarRecipe> autoMappedRecipes = new HashMap<String, AltarRecipe>();
	public static final int STACKS_X_OFFSET = 21;
	public static final int STACKS_Y_OFFSET = 1;
	private static final int RESULT_STACK_X_OFFSET = 115;
	private static final int RESULT_STACK_Y_OFFSET = STACKS_Y_OFFSET + 30;

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
		AltarRecipe recipe = autoMappedRecipes.get(key);
		if (recipe != null) {
			Iterator<ItemStackWrapper> req = recipe.getInput().iterator();
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					if (req.hasNext()) {
						ItemStack ingredientStack = req.next().toStack().copy();
						if (ingredientStack != null)
							locatedStacks.add(new LocatedStack(ingredientStack, x + STACKS_X_OFFSET + j * 18,
									y + STACKS_Y_OFFSET + i * 18));
					} else
						break;
				}

				locatedStacks.add(
						new LocatedStack(recipe.getOutput(), x + RESULT_STACK_X_OFFSET, y + RESULT_STACK_Y_OFFSET));
				locatedStrings.add(new LocatedString(
						I18n.format("bmw.gui.altar.requiredTier.recipe") + ": "
								+ NumeralHelper.toRoman(recipe.getMinTier().toInt()),
						x * 2 + 148, y * 2 + 40, 0xFF00FF00, false) {
					@Override
					public void renderBackground(GuiWiki gui, int mouseX, int mouseY) {
						GL11.glPushMatrix();
						GL11.glScaled(GuiWiki.TEXT_SCALE, GuiWiki.TEXT_SCALE, 1);
						gui.getFontRenderer().drawString(getName(), getX(), getY(), 0x858585);
						GL11.glPopMatrix();
					}
				});
				locatedStrings.add(
						new LocatedString(I18n.format("bmw.gui.altar.requiredLP.recipe") + ": " + recipe.getSyphon(),
								x * 2 + 148, y * 2 + 50, 0x858585, false) {
							@Override
							public void renderBackground(GuiWiki gui, int mouseX, int mouseY) {
								GL11.glPushMatrix();
								GL11.glScaled(GuiWiki.TEXT_SCALE, GuiWiki.TEXT_SCALE, 1);
								gui.getFontRenderer().drawString(getName(), getX(), getY(), 0x858585);
								GL11.glPopMatrix();
							}
						});
			}
		} else
			System.out.println("Not Found: " + key);
	}

	public static void mapRecipes() {
		Iterator<AltarRecipe> altarRecipes = ClientProxy.AltarRecipes.iterator();
		while (altarRecipes.hasNext()) {
			AltarRecipe recipe = altarRecipes.next();
			String key = recipe.getOutput().getUnlocalizedName().replace("item.", "item/").replace("tile.", "block/");
			if (!autoMappedRecipes.containsKey(key) && !recipe.isFillable()) {
				autoMappedRecipes.put(key, recipe);
//				 System.out.println("Altar: " + key);
			}
		}
	}

}
