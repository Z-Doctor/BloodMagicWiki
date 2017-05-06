package zdoctor.bmw.recipeintegrator.compact;

import java.util.List;
import java.util.Map;

import com.sun.istack.internal.NotNull;

import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry.AlchemyArrayRecipe;
import net.minecraft.item.ItemStack;

public class SimpleArrayRecipe {
	protected List<ItemStack> input;
	protected ItemStack catalyst;
	protected ItemStack output;

	public SimpleArrayRecipe(List<ItemStack> input, ItemStack catalyst, ItemStack output) {
		this.input = input;
		this.catalyst = catalyst;
		this.output = output;
	}

	public SimpleArrayRecipe(@NotNull AlchemyArrayRecipe recipe) {
		for (Map.Entry<ItemStackWrapper, AlchemyArrayEffect> effectEntry : recipe.getCatalystMap().entrySet()) {
			if (effectEntry.getValue() instanceof AlchemyArrayEffectCrafting) {
				AlchemyArrayEffectCrafting craftingEffect = (AlchemyArrayEffectCrafting) effectEntry.getValue();
				this.output = craftingEffect.getOutputStack();
				ItemStack[] recipeArray = AlchemyArrayRecipeRegistry.getRecipeForOutputStack(output);
				this.input = recipe.getInput();
				this.catalyst = recipeArray[1];
			}
		}
	}

	public ItemStack getOutput() {
		return this.output;
	}

	public List<ItemStack> getInput() {
		return this.input;
	}

	public ItemStack getCatalyst() {
		return this.catalyst;
	}

}
