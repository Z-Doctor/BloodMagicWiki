package zdoctor.bmw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import embedded.igwmod.lib.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import zdoctor.bmw.common.CommonProxy;

@Mod(modid = ModMain.MODID, name = ModMain.NAME, version = ModMain.VERSION, dependencies = ModMain.DEPENDENCIES)
public class ModMain {
	public static final String MODID = "bloodmagicwiki";
	public static final String NAME = "Blood Magic Wiki";
	public static final String VERSION = "1.0";
	public static final String DEPENDENCIES = "required-after:bloodmagic";

	@SidedProxy(clientSide = "zdoctor.bmw.client.ClientProxy", serverSide = "zdoctor.bmw.server.ServerProxy")
	public static CommonProxy proxy;
	// @SidedProxy(clientSide = "tweaked.igwmod.IGWClientProxy", serverSide =
	// "tweaked.igwmod.IGWServerProxy")
	// public static IProxy igwProxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		// igwProxy.preInit(e);
		proxy.preInit(e);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		// igwProxy.postInit();
		proxy.postInit(e);
	}

	// IGW Start
	@EventHandler
	public void processIMCRequests(FMLInterModComms.IMCEvent event) {
		proxy.processIMC(event);
	}

	/**
	 * This method is used to reject connection when the server has server info
	 * available for IGW-mod. Unless the properties.txt explicitly says it's
	 * okay to connect without IGW-Mod, by setting "optional=true".
	 * 
	 * @param installedMods
	 * @param side
	 * @return
	 */
	@NetworkCheckHandler
	public boolean onConnectRequest(Map<String, String> installedMods, Side side) {
		if (side == Side.SERVER || proxy == null)
			return true;
		File serverFolder = new File(proxy.getSaveLocation() + "\\igwmod\\");
		if (serverFolder.exists() || new File(proxy.getSaveLocation() + "\\igwmodServer\\").exists()) {// TODO
																										// remove
																										// legacy
			String str = proxy.getSaveLocation() + "\\igwmod\\properties.txt";
			File file = new File(str);
			if (!file.exists()) {
				file = new File(proxy.getSaveLocation() + "\\igwmodServer\\properties.txt");// TODO
																							// remove
																							// legacy
			}
			if (file.exists()) {
				try {
					FileInputStream stream = new FileInputStream(file);
					BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
					List<String> textList = new ArrayList<String>();
					String line = br.readLine();
					while (line != null) {
						textList.add(line);
						line = br.readLine();
					}
					br.close();

					if (textList != null) {
						for (String s : textList) {
							String[] entry = s.split("=");
							if (entry[0].equals("optional")) {
								if (Boolean.parseBoolean(entry[1]))
									return true;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			String version = installedMods.get(MODID);
			if (version.equals("${version}"))
				return true;
			return version != null && version.equals(Constants.fullVersionString());
		} else {
			return true;
		}

	}
}
