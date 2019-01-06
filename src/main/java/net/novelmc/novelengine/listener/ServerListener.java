package net.novelmc.novelengine.listener;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener implements Listener
{

    public ServerListener(NovelEngine plugin)
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

        String motd = NUtil.colorize(NovelEngine.plugin.config.getMOTD());
        motd = motd.replace("||", "\n");
        event.setMotd(motd);
    }
}
