package zdoctor.bmw.wiki.events;

import WayofTime.bloodmagic.api.Constants;
import igwmod.WikiUtils;
import igwmod.api.BlockWikiEvent;
import igwmod.api.ItemWikiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zdoctor.bmw.ModMain;

public class WikiEvents {
	public static void postInit() {
		MinecraftForge.EVENT_BUS.register(new Events());
	}
	private static class Events {
		@SubscribeEvent
		public void wikiEvent(ItemWikiEvent e) {
			String modId = WikiUtils.getOwningModId(e.itemStack);
			System.out.println("Item From: " + modId);
			if(modId.equalsIgnoreCase(Constants.Mod.MODID)) {
				System.out.println("Redirecting: " + WikiUtils.getNameFromStack(e.itemStack));
				e.pageOpened = ModMain.MODID.toLowerCase() + ":" + WikiUtils.getNameFromStack(e.itemStack);
			}
		}
		@SubscribeEvent
		public void wikiEvent(BlockWikiEvent e) {
			String modId = WikiUtils.getOwningModId(e.itemStackPicked);
			System.out.println("Item From: " + modId);
			if(modId.equalsIgnoreCase(Constants.Mod.MODID)) {
				System.out.println("Redirecting: " + WikiUtils.getNameFromStack(e.itemStackPicked));
				e.pageOpened = ModMain.MODID.toLowerCase() + ":" + WikiUtils.getNameFromStack(e.itemStackPicked);
			}
		}
	}
}
