package tweaked.igwmod.recipeintegration;

import java.util.List;

import tweaked.igwmod.TextureSupplier;
import tweaked.igwmod.api.IRecipeIntegrator;
import tweaked.igwmod.gui.IReservedSpace;
import tweaked.igwmod.gui.IWidget;
import tweaked.igwmod.gui.LocatedStack;
import tweaked.igwmod.gui.LocatedString;
import tweaked.igwmod.gui.LocatedTexture;

public class IntegratorImage implements IRecipeIntegrator{

    @Override
    public String getCommandKey(){
        return "image";
    }

    @Override
    public void onCommandInvoke(String[] arguments, List<IReservedSpace> reservedSpaces, List<LocatedString> locatedStrings, List<LocatedStack> locatedStacks, List<IWidget> locatedTextures) throws IllegalArgumentException{
        if(arguments.length != 3 && arguments.length != 4) throw new IllegalArgumentException("The code needs to contain 3 or 4 parameters: x, y, [scale,] , texture location. It now contains " + arguments.length + ".");
        int[] coords = new int[2];
        double scale = 1;
        try {
            for(int i = 0; i < 2; i++)
                coords[i] = Integer.parseInt(arguments[i]);
            if(arguments.length == 4) {
                scale = Double.parseDouble(arguments[2]);
            }
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("The code contains an invalid number! Check for spaces or invalid characters.");
        }

        LocatedTexture texture = new LocatedTexture(TextureSupplier.getTexture(arguments[arguments.length - 1]), coords[0], coords[1]);
        texture.width = (int)(texture.width * scale);
        texture.height = (int)(texture.height * scale);
        locatedTextures.add(texture);
    }

}
