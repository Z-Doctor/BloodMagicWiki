package zdoctor.bmw.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import embedded.igwmod.ConfigHandler;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.lib.WikiLog;
import embedded.igwmod.recipeintegration.IntegratorComment;
import embedded.igwmod.recipeintegration.IntegratorCraftingRecipe;
import embedded.igwmod.recipeintegration.IntegratorFurnace;
import embedded.igwmod.recipeintegration.IntegratorImage;
import embedded.igwmod.recipeintegration.IntegratorStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.client.events.ClientEvents;
import zdoctor.bmw.common.CommonProxy;
import zdoctor.bmw.recipeintegrator.IntegratorAltarRecipe;
import zdoctor.bmw.recipeintegrator.IntegratorArrayRecipe;
import zdoctor.bmw.recipeintegrator.IntegratorBloodOrbCraftingRecipe;
import zdoctor.bmw.recipeintegrator.IntegratorCraftingMeta;
import zdoctor.bmw.recipeintegrator.IntegratorHellfireRecipe;
import zdoctor.bmw.recipeintegrator.IntegratorRecipe;
import zdoctor.bmw.recipeintegrator.compact.AutoRecipe;
import zdoctor.bmw.wiki.registry.EventRegistry;
import zdoctor.bmw.wiki.registry.tabs.AlchemyWiki;
import zdoctor.bmw.wiki.registry.tabs.AltarWiki;
import zdoctor.bmw.wiki.registry.tabs.ArrayWiki;
import zdoctor.bmw.wiki.registry.tabs.BloodMagicWiki;
import zdoctor.bmw.wiki.registry.tabs.ForgeWiki;
import zdoctor.bmw.wiki.registry.tabs.ItemsWiki;
import zdoctor.bmw.wiki.registry.tabs.MiscWiki;
import zdoctor.bmw.wiki.registry.tabs.RecipeWiki;
import zdoctor.bmw.wiki.registry.tabs.RitualWiki;

public class ClientProxy extends CommonProxy {
	public static final IForgeRegistry<Item> ItemRegistry = GameRegistry.findRegistry(Item.class);

	public static final List<IRecipe> GameRecipes = CraftingManager.getInstance().getRecipeList();

	// public static final Map<String, IRecipe> MappedRecipes = new HashMap<>();
	// public static final Map<String, IRecipe> BloodMagicRecipes = new
	// HashMap<>();
	// public static final Map<String, TartaricForgeRecipe> ForgeRecipes = new
	// HashMap<>();
	// public static final Map<String, AltarRecipe> AltarRecipes = new
	// HashMap<>();;
	// public static final Map<String, AlchemyArrayRecipe> ArrayRecipes = new
	// HashMap<>();

	public static final Map<String, List<AutoRecipe>> RecipeMap = new HashMap<>();

	public static KeyBinding openInterfaceKey;

	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		openInterfaceKey = new KeyBinding(ModMain.MODID + ".keys.wiki", ModMain.DEFAULT_KEYBIND_OPEN_GUI,
				ModMain.MODID + ".keys.category");

		ClientRegistry.registerKeyBinding(openInterfaceKey);
		ConfigHandler.init(e.getSuggestedConfigurationFile());

		BloodMagicWiki.preInit();
		AltarWiki.preInit();
		ForgeWiki.preInit();
		AlchemyWiki.preInit();
		RitualWiki.preInit();
		ArrayWiki.preInit();
		MiscWiki.preInit();
		ItemsWiki.preInit();
		RecipeWiki.preInit();
		// BloodBaubleWiki.preInit();
	}

	public void init(FMLInitializationEvent e) {
		super.init(e);

	}

	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		addDefaultKeys();
		ClientEvents.postInit();

		WikiRegistry.registerRecipeIntegrator(new IntegratorImage());
		WikiRegistry.registerRecipeIntegrator(new IntegratorCraftingRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorFurnace());
		WikiRegistry.registerRecipeIntegrator(new IntegratorStack());
		WikiRegistry.registerRecipeIntegrator(new IntegratorComment());

		EventRegistry.postInit();

		WikiRegistry.registerRecipeIntegrator(new IntegratorHellfireRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorBloodOrbCraftingRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorAltarRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorArrayRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorCraftingMeta());
	}

	private void addDefaultKeys() {
		RecipeWiki.getTAB().refreshWiki();
	}

	@Override
	public void processIMC(IMCEvent event) {
		List<FMLInterModComms.IMCMessage> messages = event.getMessages();
		for (FMLInterModComms.IMCMessage message : messages) {
			try {
				Class clazz = Class.forName(message.key);
				try {
					Method method = clazz.getMethod(message.getStringValue());
					if (method == null) {
						WikiLog.error("Couldn't find the \"" + message.key
								+ "\" method. Make sure it's there and marked with the 'static' modifier!");
					} else {
						try {
							method.invoke(null);
							WikiLog.info("Successfully gave " + message.getSender()
									+ " a nudge! Happy to be doing business!");
						} catch (IllegalAccessException e) {
							WikiLog.error(message.getSender()
									+ " tried to register to IGW. Failed because the method can NOT be accessed: "
									+ message.getStringValue());
						} catch (IllegalArgumentException e) {
							WikiLog.error(message.getSender()
									+ " tried to register to IGW. Failed because the method has arguments or it isn't static: "
									+ message.getStringValue());
						} catch (InvocationTargetException e) {
							WikiLog.error(message.getSender()
									+ " tried to register to IGW. Failed because the method threw an exception: "
									+ message.getStringValue());
							e.printStackTrace();
						}
					}
				} catch (NoSuchMethodException e) {
					WikiLog.error(message.getSender()
							+ " tried to register to IGW. Failed because the method can NOT be found: "
							+ message.getStringValue());
				} catch (SecurityException e) {
					WikiLog.error(message.getSender()
							+ " tried to register to IGW. Failed because the method can NOT be accessed: "
							+ message.getStringValue());
				}
			} catch (ClassNotFoundException e) {
				WikiLog.error(message.getSender()
						+ " tried to register to IGW. Failed because the class can NOT be found: " + message.key);
			}

		}
	}

	@Override
	public String getSaveLocation() {
		String mcDataLocation = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
		return mcDataLocation.substring(0, mcDataLocation.length() - 2);
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}
}
