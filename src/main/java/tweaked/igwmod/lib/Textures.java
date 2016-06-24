package tweaked.igwmod.lib;

import net.minecraft.util.ResourceLocation;
import zdoctor.bmw.ModMain;

public class Textures{
    public static final String GUI_LOCATION = ModMain.MODID.toLowerCase() + ":textures/gui";
    public static final ResourceLocation GUI_WIKI = new ResourceLocation(GUI_LOCATION + "/GuiWiki.png");
    public static final ResourceLocation GUI_ACTIVE_TAB = new ResourceLocation(GUI_LOCATION + "/GuiActiveTab.png");
    public static final ResourceLocation GUI_NON_ACTIVE_TAB = new ResourceLocation(GUI_LOCATION + "/GuiNonActiveTab.png");
    public static final ResourceLocation GUI_PAGE_BROWSER = new ResourceLocation(GUI_LOCATION + "/GuiPageBrowser.png");
    public static final ResourceLocation GUI_ENTITIES = new ResourceLocation(GUI_LOCATION + "/GuiEntities.png");
}
