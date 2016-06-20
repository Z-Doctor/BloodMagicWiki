package zdoctor.bmw.client;

import igwmod.api.WikiRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zdoctor.bmw.common.CommonProxy;
import zdoctor.bmw.recipeintegrator.IntegratorAltarRecipe;
import zdoctor.bmw.recipeintegrator.IntegratorBMCraftingRecipe;
import zdoctor.bmw.recipeintegrator.IntegratorBindingRecipe;
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
		IntegratorHellfireRecipe.mapRecipes();
		IntegratorBMCraftingRecipe.mapRecipes();
		IntegratorAltarRecipe.mapRecipes();
		IntegratorBindingRecipe.mapRecipes();

		WikiRegistry.registerRecipeIntegrator(new IntegratorHellfireRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorBMCraftingRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorAltarRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorBindingRecipe());
	}
}
