package tweaked.igwmod.gui.tabs;

import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import tweaked.igwmod.ConfigHandler;
import tweaked.igwmod.gui.GuiWiki;

public class IGWWikiTab extends BaseWikiTab{

    public IGWWikiTab(){
        pageEntries.add("intro");
        pageEntries.add("forServers");
//        if(ConfigHandler.debugMode) {
//            pageEntries.add("devIntro");
//            pageEntries.add("devItemAndBlock");
//            pageEntries.add("devPageCommands");
//            pageEntries.add("devForModders");
//        }
    }

    @Override
    public String getName(){
        return "igwmod.wikitab.igwmod.name";
    }

    @Override
    public ItemStack renderTabIcon(GuiWiki gui){
        return new ItemStack(Blocks.LOG);
    }

    @Override
    protected String getPageName(String pageEntry){
        return I18n.format("igwtab.entry." + pageEntry);
    }

    @Override
    protected String getPageLocation(String pageEntry){
        return "igwmod:igwtab/" + pageEntry;
    }
}
