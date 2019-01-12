package net.novelmc.novelengine.listener;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NovelBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventModeListener extends NovelBase implements Listener
{

    public EventModeListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static void enableEventMode()
    {
        if (plugin.config.isEventModeEnabled())
        {
            return;
        }

        plugin.config.setEventModeEnabled(true);

        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.setWhitelisted(true);
        }

        Bukkit.broadcastMessage(ChatColor.RED + "The server has entered event mode, now all online players are now whitelisted!");
    }

    public static void disableEventMode()
    {
        if (!plugin.config.isEventModeEnabled())
        {
            return;
        }

        plugin.config.setEventModeEnabled(false);

        for (OfflinePlayer player : Bukkit.getWhitelistedPlayers())
        {
            player.setWhitelisted(false);
        }

        Bukkit.broadcastMessage(ChatColor.RED + "The server has left event mode, have a great rest of your day!");
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        Player player = e.getPlayer();
        if (plugin.config.isEventModeEnabled() && !player.isWhitelisted() && !StaffList.isStaff(player))
        {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The server is currently in event mode!");
        }
    }
}
