package zdoctor.bmw.client.events;

import embedded.igwmod.TickHandler;
import embedded.igwmod.render.TooltipOverlayHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import zdoctor.bmw.client.ClientProxy;

public class ClientEvents {
	public static void postInit() {
		MinecraftForge.EVENT_BUS.register(new Events());
		
		MinecraftForge.EVENT_BUS.register(new TickHandler());
		MinecraftForge.EVENT_BUS.register(new TooltipOverlayHandler());
	}

	public static class Events {
		@SubscribeEvent
		public void onKeyBind(KeyInputEvent event) {
			if (ClientProxy.openInterfaceKey.isPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
				TickHandler.openWikiGui();
			}
		}
		
	}
}
