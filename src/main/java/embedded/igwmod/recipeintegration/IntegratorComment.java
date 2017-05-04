package embedded.igwmod.recipeintegration;

import java.util.List;

import embedded.igwmod.api.IRecipeIntegrator;
import embedded.igwmod.gui.IReservedSpace;
import embedded.igwmod.gui.IWidget;
import embedded.igwmod.gui.LocatedStack;
import embedded.igwmod.gui.LocatedString;

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
