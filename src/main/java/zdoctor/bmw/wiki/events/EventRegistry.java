package zdoctor.bmw.wiki.events;

import WayofTime.bloodmagic.api.Constants;
import embedded.igwmod.WikiUtils;
import embedded.igwmod.api.ItemWikiEvent;
import embedded.igwmod.gui.BrowseHistory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.wiki.tabs.ItemsWiki;

public class EventRegistry {
	public static void postInit() {
		MinecraftForge.EVENT_BUS.register(new Events());
	}

	private static class Events {
		@SubscribeEvent(receiveCanceled = false)
		public void wikiEvent(ItemWikiEvent e) {
			System.out.println("Item Wiki Event: " + e.pageOpened);
			e.pageOpened = ItemsWiki.getPageFromName(e.pageOpened);
			// String modId = WikiUtils.getOwningModId(e.itemStack);
			// if (modId.equalsIgnoreCase(Constants.Mod.MODID)) {
			// e.pageOpened = modId + ":bloodmagic/" +
			// WikiUtils.getNameFromStack(e.itemStack);
			// e.pageOpened = BloodMagicWiki.getItemPage(e.pageOpened);
			// } else
			// e.pageOpened = BrowseHistory.current() == null ?
			// ModMain.MODID.toLowerCase() + ":bloodmagic/intro" :
			// BrowseHistory.current().link;
		}

		@SubscribeEvent
		public void wikiEvent(BlockWikiEvent e) {
			System.out.println("Block Wiki Event: " + e.pageOpened);
			e.pageOpened = ItemsWiki.getPageFromName(e.pageOpened);
			// String modId = WikiUtils.getOwningModId(e.itemStackPicked);
			// if (modId.equalsIgnoreCase(Constants.Mod.MODID)) {
			// if (e.pageOpened.toLowerCase().contains("ritual") &&
			// e.pageOpened.toLowerCase().contains("stone"))
			// e.pageOpened = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/ritualStone";
			// else if (e.pageOpened.toLowerCase().contains("routing"))
			// e.pageOpened = ModMain.MODID.toLowerCase() +
			// ":bloodmagic5/block/routing";
			// else
			// e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/"
			// +
			// WikiUtils.getNameFromStack(e.itemStackPicked).replace("BloodMagic.",
			// "")
			// .replace("BloodMagic.", "").replaceFirst("\\..*", "");
			// } else
			// e.pageOpened = BrowseHistory.current() == null ?
			// ModMain.MODID.toLowerCase() + ":bloodmagic/intro" :
			// BrowseHistory.current().link;
		}

		@SubscribeEvent
		public void wikiEvent(EntityWikiEvent e) {
			e.pageOpened = BrowseHistory.current() == null ? ModMain.MODID.toLowerCase() + ":bloodmagic/intro"
					: BrowseHistory.current().link;
			// e.pageOpened = ModMain.MODID.toLowerCase() + ":bloodmagic/intro";
		}

		@SubscribeEvent
		public void pageChanged(PageChangeEvent e) {
			// System.out.println("Changing to: " + e.currentFile);
			// String unloc = e.currentFile.toLowerCase();
			// if (unloc.contains(Constants.Mod.MODID.toLowerCase())) {
			// if (unloc.contains(".orb.") || unloc.contains("bloodorb"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/bloodorbs";
			// else if (unloc.contains(".sacrificial.") ||
			// unloc.contains("sacrificialdagger"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/sacrificialdagger";
			// else if (unloc.contains("daggerofsacrifice"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/daggerofsacrifice";
			// else if (unloc.contains("soulgem"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/soulgem";
			// else if (unloc.contains(".basecomponent."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/basecomponents";
			// else if (unloc.contains(".scribe."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/scribetools";
			// else if (unloc.contains("ritualdiviner"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/diviners";
			// else if (unloc.contains("activationcrystal"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/activationcrystal";
			// else if (unloc.contains(".slate.") || unloc.contains(".sigil."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/sigils";
			// else if (unloc.contains(".bound."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/bound";
			// else if (unloc.contains(".focus.") ||
			// unloc.contains("teleposer"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/foci";
			// else if (unloc.contains(".bloodshard."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/bloodshard";
			// else if (unloc.contains(".livingarmour.") ||
			// unloc.contains("upgradetome")
			// || unloc.contains("downgradetome") ||
			// unloc.contains("upgradettrainer"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/livingarmour";
			// else if (unloc.contains(".sentientarmour."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/sentientarmour";
			// else if (unloc.contains(".pack."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/item/bloodpac";
			// else if (unloc.contains("inversionpillar"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/inversionpillar";
			// else if (unloc.contains("/crystal."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/crystal";
			// else if (unloc.contains("stone.ritual") ||
			// unloc.contains(".ritualstone."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/ritualstones";
			// else if (unloc.contains("rune"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/runes";
			// else if (unloc.contains("demoncrystal"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/demoncrystals";
			// else if (unloc.contains("routing"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/routing";
			// else if (unloc.contains("bloodstone"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/bloodstonebrick";
			// else if (unloc.contains(".path."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/paths";
			// else if (unloc.contains(".mimic."))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/mimics";
			// else if (unloc.matches("(.*)bricks[0-9](.*)") ||
			// unloc.matches("(.*)wall[0-9](.*)")
			// || unloc.matches("(.*)stairs[0-9](.*)") ||
			// unloc.matches("(.*)pillar[0-9](.*)")
			// || unloc.contains("extras") ||
			// unloc.matches("(.*)pillarcap[0-9](.*)"))
			// e.currentFile = ModMain.MODID.toLowerCase() +
			// ":bloodmagic/block/willblocks";
			// else {
			// e.currentFile = e.currentFile.replaceAll("\\.", "/");
			// if
			// (e.currentFile.toLowerCase().matches(ModMain.MODID.toLowerCase())
			// || !e.currentFile.matches(":")) {
			// e.currentFile = e.currentFile.replaceAll("block/" +
			// Constants.Mod.MODID,
			// Constants.Mod.MODID.toLowerCase() + "/block");
			// e.currentFile = e.currentFile.replaceAll("item/" +
			// Constants.Mod.MODID,
			// Constants.Mod.MODID.toLowerCase() + "/item");
			// // e.currentFile = ModMain.MODID.toLowerCase() + ":" +
			// // e.currentFile;
			// }
			// }
			// return;
			// }
		}

		// @SubscribeEvent
		// public void onPlayerJoin(TickEvent.PlayerTickEvent event){
		// if(event.player.world.isRemote && event.player ==
		// FMLClientHandler.instance().getClientPlayerEntity()) {
		//// event.player.(ITextComponent.Serializer.jsonToComponent("[\"" +
		// TextFormatting.GOLD + "The mod " + supportingMod + " is supporting
		// In-Game Wiki mod. " + TextFormatting.GOLD + "However, In-Game Wiki
		// isn't installed! " + "[\"," + "{\"text\":\"Download
		// Latest\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/igwmod_download\"}},"
		// + "\"]\"]"));
		// GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		// chat.printChatMessage(ITextComponent.Serializer.jsonToComponent("[\""
		// + TextFormatting.GOLD + "The mod " + supportingMod + " is supporting
		// In-Game Wiki mod. " + TextFormatting.GOLD + "However, In-Game Wiki
		// isn't installed! " + "[\"," + "{\"text\":\"Download
		// Latest\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/igwmod_download\"}},"
		// + "\"]\"]"));
		// FMLCommonHandler.instance().bus().unregister(this);
		// }
		// }
	}
}
