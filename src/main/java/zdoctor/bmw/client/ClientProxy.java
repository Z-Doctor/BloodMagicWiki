package zdoctor.bmw.client;

import WayofTime.bloodmagic.api.Constants;
import igwmod.api.WikiRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zdoctor.bmw.common.CommonProxy;
import zdoctor.bmw.recipeintegrator.IntegratorAltarRecipe;
import zdoctor.bmw.recipeintegrator.IntegratorArrayCraftingRecipe;
import zdoctor.bmw.recipeintegrator.IntegratorBMCraftingRecipe;
import zdoctor.bmw.recipeintegrator.IntegratorBindingRecipe;
import zdoctor.bmw.recipeintegrator.IntegratorHellfireRecipe;
import zdoctor.bmw.wiki.BloodMagicWiki;
import zdoctor.bmw.wiki.RitualWiki;
import zdoctor.bmw.wiki.events.WikiEvents;

public class ClientProxy extends CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		BloodMagicWiki.preInit();
		RitualWiki.preInit();
	}

	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		
		GameRegistry.findRegistry(Item.class).getEntries().forEach(map -> {
			if(map.getKey().getResourceDomain().equalsIgnoreCase(Constants.Mod.MODID))
				System.out.println(map.getKey());
		});
		
		WikiEvents.postInit();
		IntegratorHellfireRecipe.mapRecipes();
		IntegratorBMCraftingRecipe.mapRecipes();
		IntegratorAltarRecipe.mapRecipes();
		IntegratorBindingRecipe.mapRecipes();
		IntegratorArrayCraftingRecipe.mapRecipes();

		WikiRegistry.registerRecipeIntegrator(new IntegratorHellfireRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorBMCraftingRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorAltarRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorBindingRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorArrayCraftingRecipe());
	}
}
