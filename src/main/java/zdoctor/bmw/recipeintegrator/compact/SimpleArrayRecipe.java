package zdoctor.bmw.recipeintegrator.compact;

import java.util.List;
import java.util.Map;

import igwmod.gui.IReservedSpace;
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
