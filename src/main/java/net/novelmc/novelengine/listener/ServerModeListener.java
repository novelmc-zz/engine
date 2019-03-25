package net.novelmc.novelengine.listener;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NovelBase;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class ServerModeListener extends NovelBase implements Listener
{

    public ServerModeListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // Booleans
    public static boolean isDev(Player player)
    {
        return NUtil.DEVELOPERS.contains(player.getName());
    }

    public static boolean isDirector(Player player)
    {
        return Rank.getRank(player).isAtLeast(Rank.DIRECTOR);
    }

    // Event Mode: Start
    public static void enableEventMode()
    {
        if (plugin.config.isEventModeEnabled())
        {
            return;
        }

        plugin.config.setEventModeEnabled(true);
        plugin.config.setDevModeEnabled(false);
        plugin.config.setDevelModeEnabled(false);
        plugin.config.setStaffModeEnabled(false);

        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.setWhitelisted(true);
        }

        NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &cThe server has entered event mode, all online players have been whitelisted."));
    }

    public static void disableEventMode()
    {
        if ( ! plugin.config.isEventModeEnabled())
        {
            return;
        }

        plugin.config.setEventModeEnabled(false);

        for (OfflinePlayer player : Bukkit.getWhitelistedPlayers())
        {
            player.setWhitelisted(false);
        }

        NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &cThe server has left event mode."));
    }

    // Event Mode: End
    // Developer Mode: Start
    public static void enableDevMode()
    {
        if (plugin.config.isDevModeEnabled())
        {
            return;
        }

        plugin.config.setDevModeEnabled(true);
        plugin.config.setEventModeEnabled(false);
        plugin.config.setDevelModeEnabled(false);
        plugin.config.setStaffModeEnabled(false);

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if ( ! isDev(player) &&  ! isDirector(player))
            {
                player.kickPlayer(NUtil.colorize("&9&lSERVER >&r &cThe server has entered developer-only mode."));
            }
        }

        NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &cThe server has entered developer-mode."));
    }

    public static void disableDevMode()
    {
        if ( ! plugin.config.isDevModeEnabled())
        {
            return;
        }

        plugin.config.setDevModeEnabled(false);

        NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &cThe server has left developer-only mode."));
    }

    // Developer Mode: End
    // Development Mode: Start
    public static void enableDevelMode()
    {
        if (plugin.config.isDevelModeEnabled())
        {
            return;
        }

        plugin.config.setDevelModeEnabled(true);
        plugin.config.setEventModeEnabled(false);
        plugin.config.setDevModeEnabled(false);
        plugin.config.setStaffModeEnabled(false);

        NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &cThe server has entered development mode, be aware of restarts, reloads, and bugs."));
    }

    public static void disableDevelMode()
    {
        if ( ! plugin.config.isDevelModeEnabled())
        {
            return;
        }

        plugin.config.setDevelModeEnabled(false);

        NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &cThe server has left development mode."));
    }

    // Development Mode: End
    // Staff Mode: Start
    public static void enableStaffMode()
    {
        if (plugin.config.isStaffModeEnabled())
        {
            return;
        }

        plugin.config.setStaffModeEnabled(true);
        plugin.config.setEventModeEnabled(false);
        plugin.config.setDevModeEnabled(false);
        plugin.config.setDevelModeEnabled(false);

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if ( ! StaffList.isStaff(player))
            {
                player.kickPlayer(NUtil.colorize("&9&lSERVER >&r &cThe server has entered staff-only mode."));
            }
        }

        NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &cThe server has entered staff-only mode."));
    }

    public static void disableStaffMode()
    {
        if ( ! plugin.config.isStaffModeEnabled())
        {
            return;
        }

        plugin.config.setStaffModeEnabled(false);

        NUtil.globalMessage(NUtil.colorize("&&9&lSERVER >&r &cThe server has exited staff-only mode."));
    }

    // Staff Mode: End
    // Disable Every Mode: Start
    public static void disableAllModes()
    {
        plugin.config.setEventModeEnabled(false);
        plugin.config.setDevModeEnabled(false);
        plugin.config.setDevelModeEnabled(false);
        plugin.config.setStaffModeEnabled(false);

        NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &cThe server has re-opened to everyone."));
    }

    // Disable Every Mode: End
    // Handling
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        Player player = e.getPlayer();

        // Event Mode
        if (plugin.config.isEventModeEnabled() &&  ! player.isWhitelisted() && ! StaffList.isStaff(player))
        {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, NUtil.colorize("&cThe server is currently in event mode."));
        }

        // Developer Mode
        if (plugin.config.isDevModeEnabled())
        {
            if ( ! isDev(player) &&  ! isDirector(player))
            {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, NUtil.colorize("&cThe server is currently in developer-only mode."));
            }
        }

        // Staff Mode
        if (plugin.config.isStaffModeEnabled() &&  ! StaffList.isStaff(player))
        {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, NUtil.colorize("&cThe server is currently in staff-only mode."));
        }
    }
}
