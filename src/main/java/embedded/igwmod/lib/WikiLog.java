package embedded.igwmod.lib;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zdoctor.bmw.ModMain;

public class WikiLog{
    private static Logger logger = LogManager.getLogger(ModMain.MODID);

    public static void info(String message){
        logger.log(Level.INFO, message);
    }

    public static void error(String message){
        logger.log(Level.ERROR, message);
    }

    public static void warning(String message){
        logger.log(Level.WARN, message);
    }
}
