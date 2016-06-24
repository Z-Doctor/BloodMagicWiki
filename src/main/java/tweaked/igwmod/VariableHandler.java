package tweaked.igwmod;

import net.minecraftforge.common.MinecraftForge;
import tweaked.igwmod.api.VariableRetrievalEvent;

public class VariableHandler{

    public static String getVariable(String varName){
        VariableRetrievalEvent event = new VariableRetrievalEvent(varName);
        MinecraftForge.EVENT_BUS.post(event);
        return event.replacementValue != null ? event.replacementValue : varName;
    }
}
