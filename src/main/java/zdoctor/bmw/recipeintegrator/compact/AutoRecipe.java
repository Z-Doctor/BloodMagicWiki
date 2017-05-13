package zdoctor.bmw.recipeintegrator.compact;

import java.util.Map.Entry;

import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.ShapelessBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import zdoctor.bmw.recipeintegrator.compact.AutoRecipe.RecipeType;

public class AutoRecipe {

	private RecipeType recipeType;
	private Object recipe;
	private ItemStack output;
	
	public AutoRecipe(Object recipe, ItemStack output, RecipeType type) {
		this.recipe = recipe;
		this.recipeType = type;
		this.output = output;
	}

	public AutoRecipe(IRecipe recipe) {
		this.recipe = recipe;
		this.output = recipe.getRecipeOutput();
		if(recipe instanceof ShapelessBloodOrbRecipe || recipe instanceof ShapedBloodOrbRecipe)
			this.recipeType = RecipeType.ORB;
		else
			this.recipeType = RecipeType.CRAFT;
	}

	public AutoRecipe(AltarRecipe recipe) {
		this.recipe = recipe;
		this.recipeType = RecipeType.ALTAR;
		this.output = recipe.getOutput();
	}



	public AutoRecipe(TartaricForgeRecipe recipe) {
		this.recipe = recipe;
		this.recipeType = RecipeType.FORGE;
		this.output = recipe.getRecipeOutput();
	}



	public AutoRecipe(SimpleArrayRecipe recipe) {
		this.recipe = recipe;
		this.recipeType = RecipeType.ARRAY;
		this.output = recipe.getOutput();
	}



	public AutoRecipe(ShapedBloodOrbRecipe recipe) {
		this.recipe = recipe;
		this.recipeType = RecipeType.ORB;
		this.output = recipe.getRecipeOutput();
	}



	public AutoRecipe(ShapelessBloodOrbRecipe recipe) {
		this.recipe = recipe;
		this.recipeType = RecipeType.ORB;
		this.output = recipe.getRecipeOutput();
	}

	public AutoRecipe(Entry<ItemStack, ItemStack> entry) {
		// TODO Auto-generated constructor stub
	}

	public RecipeType getRecipeType() {
		return recipeType;
	}

	public <T> T getRecipe(Class<T> class1) {
		if(class1.isInstance(recipe)) {
//			System.out.println("Recipe is instance");
			return (T) recipe;
		}
		return null;
	}
	
	public ItemStack getOutput() {
		return output;
	}

	public static enum RecipeType {
		CRAFT, ORB, FORGE, ARRAY, ALTAR, FURNACE;
	}

}
