package net.novelmc.novelengine.ptero;

import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.entities.panel.admin.Server;
import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.util.NLog;
import org.bukkit.ChatColor;

import java.util.List;

public class PteroManager {
    NovelEngine plugin = NovelEngine.getPlugin(NovelEngine.class);
    PteroAdminAPI api;
    Server server;
    public PteroManager(){}


    public boolean hasInfo(){
        boolean apikey = plugin.getConfig().getString("pterodactyl.apikey").isEmpty();
        boolean link = plugin.getConfig().getString("pterodactyl.link").isEmpty();
        if (apikey || link){
            return false;
        }
        return true;
    }

    public void connect(String apikey)
    {
        apikey = plugin.getConfig().getString("pterodactyl.apikey");
        String serverName = plugin.getConfig().getString("pterodactyl.serverName");
        if (!hasInfo()){
            NLog.info(ChatColor.RED + "[" + ChatColor.AQUA + plugin.getDescription().getName() + ChatColor.RED + "] " + ChatColor.GREEN + "No info for pterodactyl, not connecting.");
            return;
        }
        api = new PteroAdminAPI(plugin.getConfig().getString("pterodactyl.link"), apikey);
        List<Server> servers = api.getServersController().getServers(serverName);
        server = api.getServersController().getServer(servers.get(0).getShortId());
    }

    public PteroAdminAPI getApi() {
        return api;
    }
}