package embedded.igwmod.render;

import org.lwjgl.input.Keyboard;

import WayofTime.bloodmagic.api.Constants;
import embedded.igwmod.TickHandler;
import embedded.igwmod.WikiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import zdoctor.bmw.ModMain;
import zdoctor.bmw.client.ClientProxy;

public class TooltipOverlayHandler {

	@SubscribeEvent
	public void tickEnd(TickEvent.RenderTickEvent event) {
		// && TickHandler.showTooltip() && ConfigHandler.shouldShowTooltip
		if (event.phase == TickEvent.Phase.END && FMLClientHandler.instance().getClient().inGameHasFocus
				&& ModMain.proxy.getPlayer().world != null) {
			// System.out.println("Tooltip");
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				Minecraft mc = FMLClientHandler.instance().getClient();
				ScaledResolution sr = new ScaledResolution(mc);
				FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;
				Object obj = TickHandler.getCurrentObject();
				String objectName = null;

				if (obj instanceof ItemStack) {
					ItemStack stack = (ItemStack) obj;
					if (WikiUtils.getOwningModId(stack).equalsIgnoreCase(Constants.Mod.MODID)) {
						objectName = stack.getDisplayName();
					}
				} else if (obj instanceof Entity) {
					Entity entity = (Entity) obj;
					objectName = entity.getName();
				}
				if (objectName != null) {
					// String objectName = TickHandler.getCurrentObjectName();
					String moreInfo = "'" + Keyboard.getKeyName(ClientProxy.openInterfaceKey.getKeyCode())
							+ "' for more info";

					fontRenderer.drawString(objectName,
							sr.getScaledWidth() / 2 - fontRenderer.getStringWidth(objectName) / 2,
							sr.getScaledHeight() / 2 - 20, 0xFFFFFFFF);
					fontRenderer.drawString(moreInfo,
							sr.getScaledWidth() / 2 - fontRenderer.getStringWidth(moreInfo) / 2,
							sr.getScaledHeight() / 2 - 10, 0xFFFFFFFF);
				}
			}
		}
	}
}
