package zdoctor.bmw.recipeintegrator.compact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.registry.ModItems;
import net.minecraft.item.ItemStack;

public class SimpleTartaricForgeRecipe {
	private TartaricForgeRecipe recipe;
	private ArrayList<ItemStack> validGems = new ArrayList<ItemStack>();

	public TartaricForgeRecipe getRecipe() {
		return recipe;
	}

	public ArrayList<ItemStack> getValidGems() {
		return validGems;
	}

	public SimpleTartaricForgeRecipe(TartaricForgeRecipe recipe) {
		this.recipe = recipe;

		for (DefaultWill will : DefaultWill.values())
			if (will.minSouls >= recipe.getMinimumSouls())
				this.validGems.add(will.willStack);
	}

	@Nonnull
	public List<Collection> getInputs() {
		ArrayList<Collection> ret = new ArrayList<Collection>();
		ret.add(recipe.getInput());
		ret.add(validGems);
		return ret;
	}

	@Nonnull
	public List<ItemStack> getOutputs() {
		return Collections.singletonList(recipe.getRecipeOutput());
	}

	public enum DefaultWill {
		SOUL(new ItemStack(ModItems.MONSTER_SOUL, 1, 0), 64), PETTY(new ItemStack(ModItems.SOUL_GEM, 1, 0), 64), LESSER(
				new ItemStack(ModItems.SOUL_GEM, 1, 1), 256), COMMON(new ItemStack(ModItems.SOUL_GEM, 1, 2),
						1024), GREATER(new ItemStack(ModItems.SOUL_GEM, 1, 3),
								4096), GRAND(new ItemStack(ModItems.SOUL_GEM, 1, 4), 16384);

		public final ItemStack willStack;
		public final double minSouls;

		DefaultWill(ItemStack willStack, double minSouls) {
			this.willStack = willStack;
			this.minSouls = minSouls;
		}
	}
}