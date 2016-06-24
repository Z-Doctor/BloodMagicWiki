package zdoctor.bmw;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tweaked.igwmod.IProxy;
import zdoctor.bmw.common.CommonProxy;

@Mod(modid = ModMain.MODID, name = ModMain.NAME, version = ModMain.VERSION, acceptedMinecraftVersions = "[1.9.4]")
public class ModMain {
	public static final String MODID = "ZDoctorBMW";
	public static final String NAME = "Blood Magic Wiki";
	public static final String VERSION = "1.0";
	@SidedProxy(clientSide = "zdoctor.bmw.client.ClientProxy", serverSide = "zdoctor.bmw.common.CommonProxy")
	public static CommonProxy proxy;
	@SidedProxy(clientSide = "tweaked.igwmod.IGWClientProxy", serverSide = "tweaked.igwmod.IGWServerProxy")
	public static IProxy igwProxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		igwProxy.preInit(e);
		proxy.preInit(e);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		igwProxy.postInit();
		proxy.postInit(e);
	}
}
