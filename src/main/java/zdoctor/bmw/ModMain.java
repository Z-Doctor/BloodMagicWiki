package zdoctor.bmw;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zdoctor.bmw.common.CommonProxy;

@Mod(modid = ModMain.MODID, name = ModMain.NAME, version = ModMain.VERSION, acceptedMinecraftVersions = "[1.9.4]")
public class ModMain {
	public static final String MODID = "ZDoctorBMW";
	public static final String NAME = "Blood Magic Wiki";
	public static final String VERSION = "0.1";
	@net.minecraftforge.fml.common.SidedProxy(clientSide = "zdoctor.bmw.client.ClientProxy", serverSide = "zdoctor.bmw.common.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}
}
