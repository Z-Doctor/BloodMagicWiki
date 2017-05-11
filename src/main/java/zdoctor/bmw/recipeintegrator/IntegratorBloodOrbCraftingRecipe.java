package zdoctor.bmw.recipeintegrator;

import java.util.Iterator;
import java.util.List;

import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import embedded.igwmod.gui.IWidget;
import embedded.igwmod.gui.LocatedStack;
import embedded.igwmod.gui.LocatedString;
import embedded.igwmod.lib.WikiLog;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.client.ClientProxy;
import zdoctor.bmw.recipeintegrator.compact.BaseIntegratorRecipe;

public class IntegratorBloodOrbCraftingRecipe extends BaseIntegratorRecipe {
	public static final int STACKS_X_OFFSET = 1;
    public static final int STACKS_Y_OFFSET = 1;
    public static final int RESULT_STACK_X_OFFSET = 95;
    public static final int RESULT_STACK_Y_OFFSET = STACKS_Y_OFFSET + 18;
    
	@Override
	public String getCommandKey() {
		return "orbcrafting";
	}

	@Override
	public void addAutomaticCraftingRecipe(String code, List<LocatedStack> locatedStacks, List<IWidget> locatedTextures,
			List<LocatedString> locatedStrings, int x, int y) throws IllegalArgumentException {
		String key = code.substring(4);
		IRecipe mappedRecipe = ClientProxy.MappedRecipes.get(key);
		if (mappedRecipe != null) {
			if (mappedRecipe instanceof ShapedBloodOrbRecipe) {
				ShapedBloodOrbRecipe recipe = (ShapedBloodOrbRecipe) mappedRecipe;
				Object[] itemList = recipe.getInput();
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						Object obj = itemList[i * 3 + j];
						ItemStack ingredientStack = null;
						if (obj instanceof ItemStack) {
							ingredientStack = ((ItemStack) obj).copy();

						} else if (obj instanceof List) {
							Iterator<ItemStack> stackList = ((List<ItemStack>) obj).iterator();
							while (stackList.hasNext()) {
								ingredientStack = stackList.next();
							}
						} else if (obj instanceof Integer)
							ingredientStack = OrbRegistry.getOrbStack(OrbRegistry.getOrb(recipe.getTier() - 1));
						if (ingredientStack != null) {
							locatedStacks.add(new LocatedStack(ingredientStack, x + STACKS_X_OFFSET + j * 18,
									y + STACKS_Y_OFFSET + i * 18));
						}
					}
				}
				locatedStacks.add(new LocatedStack(recipe.func_77571_b(), x + RESULT_STACK_X_OFFSET,
						y + RESULT_STACK_Y_OFFSET));
//				locatedStacks.add(new LocatedStack(recipe.getRecipeOutput(), x + RESULT_STACK_X_OFFSET,
//						y + RESULT_STACK_Y_OFFSET));
				locatedStrings.add(new LocatedString(I18n.format(ModMain.MODID + ".gui.crafting.shapedorb"), x * 2 + 120, y * 2,
						0xFF000000, false));
				locatedStrings.add(
						new LocatedString("Tier: " + recipe.getTier(), x * 2 + 120, y * 2 + 10, 0xFF000000, false));
			} else
				System.out.println("ShapedBloodOrbRecipe items only!");
		} else
			WikiLog.warning("Mapped Recipe found null item with key: " + key);
	}
}
