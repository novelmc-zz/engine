package net.novelmc.novelengine.listener;

import net.novelmc.novelengine.util.NUtil;
import net.novelmc.novelengine.util.NovelBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;

public class ServerListener extends NovelBase implements Listener
{

    public ServerListener()
    {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerListPing(ServerListPingEvent event)
    {
        if (Bukkit.hasWhitelist())
        {
            event.setMotd(ChatColor.RED + "Whitelist enabled!");
            return;
        }

        if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers())
        {
            event.setMotd(ChatColor.RED + "Server is full!");
            return;
        }
/*
        String motd = NUtil.colorize(NovelEngine.plugin.config.getMOTD());
        //motd = motd.replace("||", NEW_LINE);
        event.setMotd(motd);
*/
        List<String> motd = plugin.config.getMOTD();
        event.setMotd(NUtil.colorize(motd.get(1)) + NEW_LINE + NUtil.colorize(motd.get(2)));
    }
}
