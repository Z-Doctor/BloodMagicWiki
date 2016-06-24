package zdoctor.bmw.wiki.events;

import WayofTime.bloodmagic.api.Constants;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tweaked.igwmod.WikiUtils;
import tweaked.igwmod.api.BlockWikiEvent;
import tweaked.igwmod.api.EntityWikiEvent;
import tweaked.igwmod.api.ItemWikiEvent;
import zdoctor.bmw.ModMain;

public class WikiEvents {
	public static void postInit() {
		MinecraftForge.EVENT_BUS.register(new Events());
	}

	private static class Events {
		@SubscribeEvent(receiveCanceled=false)
		public void wikiEvent(ItemWikiEvent e) {
			String modId = WikiUtils.getOwningModId(e.itemStack);
			if (modId.equalsIgnoreCase(Constants.Mod.MODID)) {
				e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/"
						+ WikiUtils.getNameFromStack(e.itemStack).replace("BloodMagic.", "").replaceFirst("\\..*", "");
				if (e.pageOpened.toLowerCase().contains("routing"))
					e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/block/routing";
				else if(e.pageOpened.toLowerCase().contains("orb")) 
					e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/item/BloodOrb";
				else if(e.pageOpened.toLowerCase().contains("soulgem") || e.pageOpened.toLowerCase().contains("monstersoul")) 
					e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/item/SoulGems";
 			} else if(e.isCancelable())
 				e.setCanceled(true);
 			else
				e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/Intro";
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
				;
			} else
				e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/Intro";
		}

		@SubscribeEvent
		public void wikiEvent(EntityWikiEvent e) {
			e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/Intro";
		}
	}
}
