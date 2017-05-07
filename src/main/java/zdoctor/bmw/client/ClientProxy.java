package zdoctor.bmw.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry.AlchemyArrayRecipe;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import embedded.igwmod.ConfigHandler;
import embedded.igwmod.WikiUtils;
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
import net.minecraft.item.crafting.FurnaceRecipes;
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
import zdoctor.bmw.recipeintegrator.IntegratorHellfireRecipe;
import zdoctor.bmw.recipeintegrator.compact.SimpleArrayRecipe;
import zdoctor.bmw.wiki.events.EventRegistry;
import zdoctor.bmw.wiki.tabs.AltarWiki;
import zdoctor.bmw.wiki.tabs.ArrayWiki;
import zdoctor.bmw.wiki.tabs.BloodBaubleWiki;
import zdoctor.bmw.wiki.tabs.BloodMagicWiki;
import zdoctor.bmw.wiki.tabs.ItemsWiki;
import zdoctor.bmw.wiki.tabs.RitualWiki;

public class ClientProxy extends CommonProxy {
	public static final IForgeRegistry<Item> ItemRegistry = GameRegistry.findRegistry(Item.class);

	public static final List<IRecipe> GameRecipes = CraftingManager.getInstance().getRecipeList();

	public static final Map<String, IRecipe> MappedRecipes = new HashMap<>();
	public static final Map<String, IRecipe> BloodMagicRecipes = new HashMap<>();
	public static final Map<String, TartaricForgeRecipe> ForgeRecipes = new HashMap<>();
	public static final Map<String, AltarRecipe> AltarRecipes = new HashMap<>();;
	public static final Map<String, AlchemyArrayRecipe> ArrayRecipes = new HashMap<>();

	public static KeyBinding openInterfaceKey;

	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		openInterfaceKey = new KeyBinding(ModMain.MODID + ".keys.wiki", ModMain.DEFAULT_KEYBIND_OPEN_GUI,
				ModMain.MODID + ".keys.category");

		ClientRegistry.registerKeyBinding(openInterfaceKey);
		ConfigHandler.init(e.getSuggestedConfigurationFile());

		BloodMagicWiki.preInit();
		AltarWiki.preInit();
		RitualWiki.preInit();
		ArrayWiki.preInit();
		ItemsWiki.preInit();
//		BloodBaubleWiki.preInit();
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
	}

	private void addDefaultKeys() {
		GameRecipes.forEach(recipe -> {
			try {
				if (recipe.getRecipeOutput() != null && !recipe.getRecipeOutput().isEmpty()) {
					if (recipe.getRecipeOutput().getUnlocalizedName() != null) {
						String blockCode = WikiUtils.getNameFromStack(recipe.getRecipeOutput());
						if (!MappedRecipes.containsKey(blockCode)) {
							MappedRecipes.put(blockCode, recipe);
//							System.out.println("Registered: " + blockCode);
						}

						if (WikiUtils.getOwningModId(recipe.getRecipeOutput()).equalsIgnoreCase(Constants.Mod.MODID)) {
							if (!BloodMagicRecipes.containsKey(blockCode)) {
								BloodMagicRecipes.put(blockCode, recipe);
//								System.out.println("Registered: " + blockCode);
							}
						}
					} else
						WikiLog.error("Item has no unlocalized name: " + recipe.getRecipeOutput().getItem());
				}
			} catch (Throwable e) {
				WikiLog.error("Wiki failed to add recipe handling support for " + recipe.getRecipeOutput());
				e.printStackTrace();
			}

		});
		// Forge Recipes
		TartaricForgeRecipeRegistry.getRecipeList().forEach(recipe -> {
			try {
				if (recipe.getRecipeOutput() != null && !recipe.getRecipeOutput().isEmpty()) {
					if (recipe.getRecipeOutput().getUnlocalizedName() != null) {
						String blockCode = WikiUtils.getNameFromStack(recipe.getRecipeOutput());
						if (!ForgeRecipes.containsKey(blockCode))
							ForgeRecipes.put(blockCode, recipe);
					} else
						WikiLog.error("Item has no unlocalized name: " + recipe.getRecipeOutput().getItem());
				}
			} catch (Throwable e) {
				WikiLog.error("Wiki failed to add recipe handling support for " + recipe.getRecipeOutput());
				e.printStackTrace();
			}
		});
		// Altar Recipes
		AltarRecipeRegistry.getRecipes().values().forEach(recipe -> {
			try {
				if (recipe.getOutput() != null && !recipe.getOutput().isEmpty()) {
					if (recipe.getOutput().getUnlocalizedName() != null) {
						String blockCode = WikiUtils.getNameFromStack(recipe.getOutput());
						if (!AltarRecipes.containsKey(blockCode) && !recipe.isFillable()) {
							AltarRecipes.put(blockCode, recipe);
//							System.out.println("Added: " + blockCode);
						}
					} else
						WikiLog.error("Item has no unlocalized name: " + recipe.getOutput().getItem());
				} else
					WikiLog.error("Outpt is null: " + recipe);
			} catch (Throwable e) {
				WikiLog.error("Wiki failed to add recipe handling support for " + recipe.getOutput());
				e.printStackTrace();
			}
		});
		// Binding Recipes
		AlchemyArrayRecipeRegistry.getRecipes().values().forEach(array -> {
			if (array != null) {
				SimpleArrayRecipe recipe = new SimpleArrayRecipe(array);
				try {
					if (recipe.getOutput() != null && !recipe.getOutput().isEmpty()) {
						if (recipe.getOutput().getUnlocalizedName() != null) {
							String blockCode = WikiUtils.getNameFromStack(recipe.getOutput());
							if (!ArrayRecipes.containsKey(blockCode))
								ArrayRecipes.put(blockCode, array);
						} else
							WikiLog.error("Item has no unlocalized name: " + recipe.getOutput().getItem());
					}
				} catch (Throwable e) {
					WikiLog.error("Wiki failed to add recipe handling support for " + recipe.getOutput());
					e.printStackTrace();
				}
			} else
				WikiLog.error("Binding recipe is null: " + array);
		});
		// Furnace Recipes
		FurnaceRecipes.instance().getSmeltingList().entrySet().forEach(entry -> {
			if (entry.getValue() != null && entry.getValue().getItem() != null) {
				String blockCode = WikiUtils.getNameFromStack(entry.getValue());
				if (!IntegratorFurnace.autoMappedFurnaceRecipes.containsKey(blockCode))
					IntegratorFurnace.autoMappedFurnaceRecipes.put(blockCode, entry.getKey());
			}
		});

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
