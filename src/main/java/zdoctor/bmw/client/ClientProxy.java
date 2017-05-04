package zdoctor.bmw.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry.AlchemyArrayRecipe;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import embedded.igwmod.ConfigHandler;
import embedded.igwmod.InfoSupplier;
import embedded.igwmod.TickHandler;
import embedded.igwmod.WikiUtils;
import embedded.igwmod.api.WikiRegistry;
import embedded.igwmod.gui.tabs.BlockAndItemWikiTab;
import embedded.igwmod.gui.tabs.EntityWikiTab;
import embedded.igwmod.gui.tabs.IGWWikiTab;
import embedded.igwmod.lib.Constants;
import embedded.igwmod.lib.IGWLog;
import embedded.igwmod.lib.Paths;
import embedded.igwmod.recipeintegration.IntegratorComment;
import embedded.igwmod.recipeintegration.IntegratorCraftingRecipe;
import embedded.igwmod.recipeintegration.IntegratorFurnace;
import embedded.igwmod.recipeintegration.IntegratorImage;
import embedded.igwmod.recipeintegration.IntegratorStack;
import embedded.igwmod.render.TooltipOverlayHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.registry.GameData;
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
	public static final List<TartaricForgeRecipe> gemRecipes = TartaricForgeRecipeRegistry.getRecipeList();
	public static final List<IRecipe> GameRecipes = CraftingManager.getInstance().getRecipeList();
	public static final List<IRecipe> BloodMagicRecipes = new ArrayList<>();
	public static final Set<AltarRecipe> AltarRecipes = AltarRecipeRegistry.getRecipes().values();
	public static final Set<AlchemyArrayRecipe> BindingRecipes = AlchemyArrayRecipeRegistry.getRecipes().values();
	
	
//	BMRecipes = Gamere
	
	
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		// igw start
		MinecraftForge.EVENT_BUS.register(new TickHandler());

		MinecraftForge.EVENT_BUS.register(new TooltipOverlayHandler());

		// Not being used, as it doesn't really add anything...
		// MinecraftForge.EVENT_BUS.register(new HighlightHandler());

		openInterfaceKey = new KeyBinding("igwmod.keys.wiki", Constants.DEFAULT_KEYBIND_OPEN_GUI,
				"igwmod.keys.category");// TODO blend keybinding category in
										// normal
		ClientRegistry.registerKeyBinding(openInterfaceKey);
		MinecraftForge.EVENT_BUS.register(this);// subscribe to key events.

		ConfigHandler.init(e.getSuggestedConfigurationFile());

//		WikiRegistry.registerWikiTab(new IGWWikiTab());
//		WikiRegistry.registerWikiTab(new BlockAndItemWikiTab());
//		WikiRegistry.registerWikiTab(new EntityWikiTab());

		
		// igw end
		BloodMagicWiki.preInit();
		RitualWiki.preInit();
	}

	public void init(FMLInitializationEvent e) {
		super.init(e);
		GameRecipes.forEach(recipe -> {
			if(recipe.getRecipeOutput().getItem().getRegistryName().getResourceDomain().equalsIgnoreCase(WayofTime.bloodmagic.api.Constants.Mod.MODID)) {
				BloodMagicRecipes.add(recipe);
			}
		});
	}

	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		postInit();

//		GameRegistry.findRegistry(Item.class).getEntries().forEach(map -> {
//			if (map.getKey().getResourceDomain().equalsIgnoreCase(WayofTime.bloodmagic.api.Constants.Mod.MODID))
//				System.out.println(map.getKey());
//		});
		
		WikiRegistry.registerRecipeIntegrator(new IntegratorImage());
		WikiRegistry.registerRecipeIntegrator(new IntegratorCraftingRecipe());
		WikiRegistry.registerRecipeIntegrator(new IntegratorFurnace());
		WikiRegistry.registerRecipeIntegrator(new IntegratorStack());
		WikiRegistry.registerRecipeIntegrator(new IntegratorComment());

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

	// IGW start
	public static KeyBinding openInterfaceKey;

	@SubscribeEvent
	public void onKeyBind(KeyInputEvent event) {
		if (openInterfaceKey.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
			TickHandler.openWikiGui();
		}
	}

	@Override
	public void postInit() {
		addDefaultKeys();
	}

	private void addDefaultKeys() {
		// Register all basic items that have (default) pages to the item and
		// blocks page.
		// List<ItemStack> stackList = new ArrayList<ItemStack>();
		NonNullList<ItemStack> allCreativeStacks = NonNullList.<ItemStack>create();

		Iterator iterator = Item.REGISTRY.iterator();
		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();

			if (item != null && item.getCreativeTab() != null) {
				try {
					item.getSubItems(item, (CreativeTabs) null, allCreativeStacks);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}

		for (ItemStack stack : allCreativeStacks) {
			if (stack != null && stack.getItem() != null
					&& GameData.getItemRegistry().getNameForObject(stack.getItem()) != null) {
				String modid = Paths.MOD_ID.toLowerCase();
				ResourceLocation id = Item.REGISTRY.getNameForObject(stack.getItem());
				if (id != null && id.getResourceDomain() != null)
					modid = id.getResourceDomain().toLowerCase();
				if (stack.getUnlocalizedName() != null) {
					List<String> info = InfoSupplier.getInfo(modid, WikiUtils.getNameFromStack(stack), true);
					if (info != null)
						WikiRegistry.registerBlockAndItemPageEntry(stack);
				}
			}
		}

		// Register all entities that have (default) pages to the entity page.
		for (ResourceLocation entry : EntityList.getEntityNameList()) {
			String modid = entry.getResourceDomain(); // Util.getModIdForEntity(entry.());
			if (InfoSupplier.getInfo(modid, "entity/" + entry.getResourcePath(), true) != null)
				WikiRegistry.registerEntityPageEntry(EntityList.getClass(entry));
		}

		// Add automatically generated crafting recipe key mappings.
		for (IRecipe recipe : CraftingManager.getInstance().getRecipeList()) {
			if (recipe.getRecipeOutput() != null && recipe.getRecipeOutput().getItem() != null) {
				try {
					if (recipe.getRecipeOutput().getUnlocalizedName() == null) {
						IGWLog.error("Item has no unlocalized name: " + recipe.getRecipeOutput().getItem());
					} else {
						String blockCode = WikiUtils.getNameFromStack(recipe.getRecipeOutput());
						if (!IntegratorCraftingRecipe.autoMappedRecipes.containsKey(blockCode))
							IntegratorCraftingRecipe.autoMappedRecipes.put(blockCode, recipe);
					}
				} catch (Throwable e) {
					IGWLog.error("IGW-Mod failed to add recipe handling support for " + recipe.getRecipeOutput());
					e.printStackTrace();
				}
			}
		}

		// Add automatically generated furnace recipe key mappings.
		for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
			if (entry.getValue() != null && entry.getValue().getItem() != null) {
				String blockCode = WikiUtils.getNameFromStack(entry.getValue());
				if (!IntegratorFurnace.autoMappedFurnaceRecipes.containsKey(blockCode))
					IntegratorFurnace.autoMappedFurnaceRecipes.put(blockCode, entry.getKey());
			}
		}

		IGWLog.info("Registered " + WikiRegistry.getItemAndBlockPageEntries().size() + " Block & Item page entries.");
		IGWLog.info("Registered " + WikiRegistry.getEntityPageEntries().size() + " Entity page entries.");
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
						IGWLog.error("Couldn't find the \"" + message.key
								+ "\" method. Make sure it's there and marked with the 'static' modifier!");
					} else {
						try {
							method.invoke(null);
							IGWLog.info("Successfully gave " + message.getSender()
									+ " a nudge! Happy to be doing business!");
						} catch (IllegalAccessException e) {
							IGWLog.error(message.getSender()
									+ " tried to register to IGW. Failed because the method can NOT be accessed: "
									+ message.getStringValue());
						} catch (IllegalArgumentException e) {
							IGWLog.error(message.getSender()
									+ " tried to register to IGW. Failed because the method has arguments or it isn't static: "
									+ message.getStringValue());
						} catch (InvocationTargetException e) {
							IGWLog.error(message.getSender()
									+ " tried to register to IGW. Failed because the method threw an exception: "
									+ message.getStringValue());
							e.printStackTrace();
						}
					}
				} catch (NoSuchMethodException e) {
					IGWLog.error(message.getSender()
							+ " tried to register to IGW. Failed because the method can NOT be found: "
							+ message.getStringValue());
				} catch (SecurityException e) {
					IGWLog.error(message.getSender()
							+ " tried to register to IGW. Failed because the method can NOT be accessed: "
							+ message.getStringValue());
				}
			} catch (ClassNotFoundException e) {
				IGWLog.error(message.getSender()
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
