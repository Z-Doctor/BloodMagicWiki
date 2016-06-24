package tweaked.igwmod.recipeintegration;

import java.util.List;

import tweaked.igwmod.api.IRecipeIntegrator;
import tweaked.igwmod.gui.IReservedSpace;
import tweaked.igwmod.gui.IWidget;
import tweaked.igwmod.gui.LocatedStack;
import tweaked.igwmod.gui.LocatedString;

public class IntegratorComment implements IRecipeIntegrator{

    @Override
    public String getCommandKey(){
        return "comment";
    }

    @Override
    public void onCommandInvoke(String[] arguments, List<IReservedSpace> reservedSpaces, List<LocatedString> locatedStrings, List<LocatedStack> locatedStacks, List<IWidget> locatedTextures) throws IllegalArgumentException{
        //Just ignore it, which is what a comment is doing ;).
    }

}
