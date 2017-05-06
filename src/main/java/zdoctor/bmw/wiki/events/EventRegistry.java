package zdoctor.bmw.wiki.events;

import WayofTime.bloodmagic.api.Constants;
import embedded.igwmod.WikiUtils;
import embedded.igwmod.api.ItemWikiEvent;
import embedded.igwmod.gui.BrowseHistory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.wiki.BloodMagicWiki;

public class EventRegistry {
	public static void postInit() {
		MinecraftForge.EVENT_BUS.register(new Events());
	}

	private static class Events {
		@SubscribeEvent(receiveCanceled = false)
		public void wikiEvent(ItemWikiEvent e) {
			String modId = WikiUtils.getOwningModId(e.itemStack);
			if (modId.equalsIgnoreCase(Constants.Mod.MODID)) {
				e.pageOpened = modId + ":bloodmagic/" + WikiUtils.getNameFromStack(e.itemStack);
				e.pageOpened = BloodMagicWiki.getItemPage(e.pageOpened);
			} else
				e.pageOpened = BrowseHistory.current() == null ? ModMain.MODID.toLowerCase() + ":bloodmagic/intro" : BrowseHistory.current().link;
		}

		@SubscribeEvent
		public void wikiEvent(BlockWikiEvent e) {
			String modId = WikiUtils.getOwningModId(e.itemStackPicked);
			if (modId.equalsIgnoreCase(Constants.Mod.MODID)) {
				if (e.pageOpened.toLowerCase().contains("ritual") && e.pageOpened.toLowerCase().contains("stone"))
					e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/block/ritualStone";
				else if (e.pageOpened.toLowerCase().contains("routing"))
					e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/block/routing";
				else
					e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/"
							+ WikiUtils.getNameFromStack(e.itemStackPicked).replace("BloodMagic.", "")
									.replace("BloodMagic.", "").replaceFirst("\\..*", "");
			} else
				e.pageOpened = BrowseHistory.current() == null ? ModMain.MODID.toLowerCase() + ":bloodmagic/intro" : BrowseHistory.current().link;
		}

		@SubscribeEvent
		public void wikiEvent(EntityWikiEvent e) {
			e.pageOpened = BrowseHistory.current() == null ? ModMain.MODID.toLowerCase() + ":bloodmagic/intro" : BrowseHistory.current().link;
			// e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/intro";
		}
		
//	    @SubscribeEvent
//	    public void onPlayerJoin(TickEvent.PlayerTickEvent event){
//	        if(event.player.world.isRemote && event.player == FMLClientHandler.instance().getClientPlayerEntity()) {
////	            event.player.(ITextComponent.Serializer.jsonToComponent("[\"" + TextFormatting.GOLD + "The mod " + supportingMod + " is supporting In-Game Wiki mod. " + TextFormatting.GOLD + "However, In-Game Wiki isn't installed! " + "[\"," + "{\"text\":\"Download Latest\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/igwmod_download\"}}," + "\"]\"]"));
//	        	GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
//	        	chat.printChatMessage(ITextComponent.Serializer.jsonToComponent("[\"" + TextFormatting.GOLD + "The mod " + supportingMod + " is supporting In-Game Wiki mod. " + TextFormatting.GOLD + "However, In-Game Wiki isn't installed! " + "[\"," + "{\"text\":\"Download Latest\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/igwmod_download\"}}," + "\"]\"]"));
//	        	FMLCommonHandler.instance().bus().unregister(this);
//	        }
//	    }
	}
}
