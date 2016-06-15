package zdoctor.bmw.recipe;

import java.util.ArrayList;
import java.util.Iterator;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class HellfireRecipe implements IRecipe {

	public static int recipeHeight = 2;
	public static int recipeWidth = 2;
	public ArrayList<ItemStack> recipeItems = new ArrayList<ItemStack>();
	TartaricForgeRecipe recipe;

	public HellfireRecipe(TartaricForgeRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.recipe.getRecipeOutput();
	}

	@Override
	public int getRecipeSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean matches(InventoryCrafting arg0, World arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public TartaricForgeRecipe getRecipe() {
		return this.recipe;
	}

}
