package zdoctor.bmw.client;

import java.util.Iterator;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import igwmod.api.WikiRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zdoctor.bmw.common.CommonProxy;
import zdoctor.bmw.recipeintegrator.IntegratorHellfireRecipe;
import zdoctor.bmw.wiki.BloodMagicWiki;
import zdoctor.bmw.wiki.events.WikiEvents;

public class ClientProxy extends CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		BloodMagicWiki.preInit();
	}

	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		WikiEvents.postInit();
		Iterator<TartaricForgeRecipe> recipes = TartaricForgeRecipeRegistry.getRecipeList().iterator();
		while (recipes.hasNext()) {
			TartaricForgeRecipe recipe = recipes.next();
			String key = recipe.getRecipeOutput().getUnlocalizedName().replace("item.", "item/").replace("tile.", "block/");
			if (!IntegratorHellfireRecipe.autoMappedRecipes.containsKey(key)) {
				TartaricForgeRecipe value = recipe;
				IntegratorHellfireRecipe.autoMappedRecipes.put(key, value);
				System.out.println(key);
			}
		}
		
		WikiRegistry.registerRecipeIntegrator(new IntegratorHellfireRecipe());
		// Iterator<String> recipes =
		// IntegratorCraftingRecipe.autoMappedRecipes.keySet().iterator();
		// Iterator<String> recipes =
		// IntegratorFurnace.autoMappedFurnaceRecipes.keySet().iterator();
		// while(recipes.hasNext()) {
		// String name = recipes.next();
		// if(name.contains("BloodMagic"))
		// System.out.println(name);
		// }
	}
}
