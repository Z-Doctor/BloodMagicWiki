package zdoctor.bmw.client;

import java.util.Iterator;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import igwmod.api.WikiRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zdoctor.bmw.common.CommonProxy;
import zdoctor.bmw.recipeintegrator.IntegratorAltarRecipe;
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
		Iterator<TartaricForgeRecipe> gemRecipes = TartaricForgeRecipeRegistry.getRecipeList().iterator();
		while (gemRecipes.hasNext()) {
			TartaricForgeRecipe recipe = gemRecipes.next();
			String key = recipe.getRecipeOutput().getUnlocalizedName().replace("item.", "item/").replace("tile.",
					"block/");
			if (!IntegratorHellfireRecipe.autoMappedRecipes.containsKey(key)) {
				IntegratorHellfireRecipe.autoMappedRecipes.put(key, recipe);
				System.out.println(key);
			}
		}

		WikiRegistry.registerRecipeIntegrator(new IntegratorHellfireRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorAltarRecipe());
	}
}
