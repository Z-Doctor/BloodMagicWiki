package zdoctor.bmw.recipeintegrator.compact;

import java.util.Iterator;
import java.util.List;

import WayofTime.bloodmagic.api.registry.OrbRegistry;
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
import net.minecraft.item.crafting.IRecipe;
import zdoctor.bmw.ModMain;

public abstract class BaseIntegratorRecipe implements IRecipeIntegrator {
	public abstract String getCommandKey();

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
				.add(new LocatedTexture(TextureSupplier.getTexture(ModMain.MODID + ":textures/gui/GuiCrafting.png"), x,
						y, (int) (116 / GuiWiki.TEXT_SCALE), (int) (54 / GuiWiki.TEXT_SCALE)));

		if (arguments[2].startsWith("key=")) {
			if (arguments.length != 3)
				throw new IllegalArgumentException(
						"An RecipeRetrievalEvent crafting code can only have 3 parameters: x, y and the key!");
			addAutomaticCraftingRecipe(arguments[2], locatedStacks, locatedTextures, locatedStrings,
					(int) (x * GuiWiki.TEXT_SCALE), (int) (y * GuiWiki.TEXT_SCALE));
		} else {
			System.out.println("Manual BMCrafting not supported, use crafting");
		}
	}

	public abstract void addAutomaticCraftingRecipe(String string, List<LocatedStack> locatedStacks,
			List<IWidget> locatedTextures, List<LocatedString> locatedStrings, int i, int j);

}
