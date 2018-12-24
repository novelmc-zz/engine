package net.novelmc.novelmc.listener;

import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener implements Listener
{

    public ServerListener(NovelMC plugin)
    {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
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

        String motd = NUtil.colorize(NovelMC.plugin.config.getMOTD());
        motd = motd.replace("||", "\n");
        event.setMotd(motd);
    }
}
