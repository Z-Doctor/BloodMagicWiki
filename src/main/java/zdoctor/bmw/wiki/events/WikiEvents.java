package zdoctor.bmw.wiki.events;

import WayofTime.bloodmagic.api.Constants;
import igwmod.WikiUtils;
import igwmod.api.BlockWikiEvent;
import igwmod.api.EntityWikiEvent;
import igwmod.api.ItemWikiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.wiki.BloodMagicWiki;

public class WikiEvents {
	public static void postInit() {
		MinecraftForge.EVENT_BUS.register(new Events());
	}

	private static class Events {
		@SubscribeEvent(receiveCanceled = false)
		public void wikiEvent(ItemWikiEvent e) {
			System.out.println(e.pageOpened);
			String modId = WikiUtils.getOwningModId(e.itemStack);
			if (modId.equalsIgnoreCase(Constants.Mod.MODID)) {
				e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/"
						+ WikiUtils.getNameFromStack(e.itemStack).replace("BloodMagic.", "").replaceFirst("\\..*", "");
				e.pageOpened = BloodMagicWiki.getItemPage(e.pageOpened);
			} 
//			else if (e.isCancelable())
//				e.setCanceled(true);
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
			} 
//			else
//				e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/Intro";
		}

		@SubscribeEvent
		public void wikiEvent(EntityWikiEvent e) {
			e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/Intro";
		}
	}
}
