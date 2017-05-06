package embedded.igwmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy{
    public void preInit(FMLPreInitializationEvent event);

    public void postInit(FMLPostInitializationEvent event);

    public void processIMC(FMLInterModComms.IMCEvent event);

    public String getSaveLocation();

    public EntityPlayer getPlayer();
}
