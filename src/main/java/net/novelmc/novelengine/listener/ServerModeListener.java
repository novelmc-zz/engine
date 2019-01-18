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

        NUtil.globalMessage(NUtil.colorize("&cThe server has entered event mode, now all online players are now whitelisted!"));
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

        NUtil.globalMessage(NUtil.colorize("&cThe server has left event mode, have a great rest of your day!"));
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
            if (!isDev(player) && !isDirector(player))
            {
                player.kickPlayer(NUtil.colorize("&cThe server has entered developer mode."));
            }
        }

        NUtil.globalMessage(NUtil.colorize("&cThe server has entered developer mode, now only leadership and developers can join!"));
    }

    public static void disableDevMode()
    {
        if (!plugin.config.isDevModeEnabled())
        {
            return;
        }

        plugin.config.setDevModeEnabled(false);

        NUtil.globalMessage(NUtil.colorize("&cThe server has left developer mode, have a great rest of your day!"));
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

        NUtil.globalMessage(NUtil.colorize("&cThe server has entered development mode, be aware of restarts and reloads!"));
    }

    public static void disableDevelMode()
    {
        if (!plugin.config.isDevelModeEnabled())
        {
            return;
        }

        plugin.config.setDevelModeEnabled(false);

        NUtil.globalMessage(NUtil.colorize("&cThe server has left development mode, have a great rest of your day!"));
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
            if (!StaffList.isStaff(player))
            {
                player.kickPlayer(NUtil.colorize("&cThe server has entered staff mode."));
            }
        }

        NUtil.globalMessage(NUtil.colorize("&cThe server has entered staff mode, now only staff can join!"));
    }

    public static void disableStaffMode()
    {
        if (!plugin.config.isStaffModeEnabled())
        {
            return;
        }

        plugin.config.setStaffModeEnabled(false);

        NUtil.globalMessage(NUtil.colorize("&cThe server has left staff mode, have a great rest of your day!"));
    }
    
    // Staff Mode: End
    
    // Disable Every Mode: Start
    
    public static void disableAllModes()
    {
        plugin.config.setEventModeEnabled(false);
        plugin.config.setDevModeEnabled(false);
        plugin.config.setDevelModeEnabled(false);
        plugin.config.setStaffModeEnabled(false);
        
        NUtil.globalMessage(NUtil.colorize("&cThe server has returned to normal, have a great rest of your day!"));
    }
    
    // Disable Every Mode: End
    
    // Handling
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        Player player = e.getPlayer();
        
        // Event Mode
        if (plugin.config.isEventModeEnabled() && !player.isWhitelisted() && !StaffList.isStaff(player))
        {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, NUtil.colorize("&cThe server is currently in event mode!"));
        }
        
        // Developer Mode
        if (plugin.config.isDevModeEnabled())
        {
            if (!isDev(player) || !isDirector(player))
            {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, NUtil.colorize("&cThe server is currently in developer mode!"));
            }
        }
        
        // Staff Mode
        if (plugin.config.isStaffModeEnabled() && !StaffList.isStaff(player))
        {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, NUtil.colorize("&cThe server is currently in staff mode!"));
        }
    }
}
