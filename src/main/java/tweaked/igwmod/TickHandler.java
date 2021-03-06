package tweaked.igwmod;

import WayofTime.bloodmagic.api.Constants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tweaked.igwmod.gui.GuiWiki;
import tweaked.igwmod.lib.Util;
import zdoctor.bmw.ModMain;

public class TickHandler {
	private static int ticksHovered;
	private static Entity lastEntityHovered;
	private static BlockPos coordHovered;
	public static int ticksExisted;
	private static final int MIN_TICKS_HOVER = 50;

	@SubscribeEvent
	public void tick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			EntityPlayer player = event.player;
			if (player == FMLClientHandler.instance().getClient().thePlayer) {
				ticksExisted++;
				RayTraceResult lookedObject = FMLClientHandler.instance().getClient().objectMouseOver;
				if (lookedObject != null) {
					if (lookedObject.typeOfHit == RayTraceResult.Type.ENTITY) {
						if (lastEntityHovered == lookedObject.entityHit) {
							ticksHovered++;
							coordHovered = null;
						} else {
							lastEntityHovered = lookedObject.entityHit;
							ticksHovered = 0;
							coordHovered = null;
						}
					} else if (lookedObject.getBlockPos() != null) {
						if (coordHovered != null && lookedObject.getBlockPos().equals(new BlockPos(coordHovered))) {
							ticksHovered++;
							lastEntityHovered = null;
						} else {
							if (!event.player.worldObj.isAirBlock(lookedObject.getBlockPos())) {
								ticksHovered = 0;
								lastEntityHovered = null;
								coordHovered = lookedObject.getBlockPos();
							}
						}
					}
				}
			}
		}
	}

	public static boolean showTooltip() {
		return ticksHovered > MIN_TICKS_HOVER;
	}

	public static void openWikiGui() {
		// if(showTooltip()) {
		// ConfigHandler.disableTooltip();
		if (lastEntityHovered != null) {
			GuiWiki gui = new GuiWiki();
			FMLCommonHandler.instance().showGuiScreen(gui);
			gui.setCurrentFile(lastEntityHovered);
			return;
		} else if (coordHovered != null) {
			World world = FMLClientHandler.instance().getClient().theWorld;
			if (world != null) {
				if (!world.isAirBlock(coordHovered)) {
					GuiWiki gui = new GuiWiki();
					FMLCommonHandler.instance().showGuiScreen(gui);
					gui.setCurrentFile(world, coordHovered);
					return;
				}
			}

		}
		GuiWiki wiki = new GuiWiki();
		FMLCommonHandler.instance().showGuiScreen(wiki);
		wiki.setCurrentFile(ModMain.MODID.toLowerCase() + ":bloodmagic/Intro");
	}

	public static String getCurrentObjectName() {
		if (lastEntityHovered != null) {
			String modId = Util.getModIdForEntity(lastEntityHovered.getClass());
			if (modId.equalsIgnoreCase(Constants.Mod.MODID))
				return lastEntityHovered.getName();
			return null;
		} else {
			try {
				World world = FMLClientHandler.instance().getClient().theWorld;
				IBlockState blockState = world.getBlockState(coordHovered);
				if (blockState != null) {
					ItemStack idPicked = blockState.getBlock().getPickBlock(blockState,
							FMLClientHandler.instance().getClient().objectMouseOver, world, coordHovered,
							FMLClientHandler.instance().getClientPlayerEntity());
					if (idPicked != null) {
						String modId = WikiUtils.getOwningModId(idPicked);
						if (modId.equalsIgnoreCase(Constants.Mod.MODID))
							return new ItemStack(blockState.getBlock(), 1,
									blockState.getBlock().getMetaFromState(blockState)).getDisplayName();
					}
					return null;
				}
			} catch (Throwable e) {
			}
			return TextFormatting.RED + "<ERROR>";
		}

	}

}
