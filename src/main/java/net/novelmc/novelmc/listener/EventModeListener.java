package net.novelmc.novelmc.listener;

import org.bukkit.event.Listener;
import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.staff.StaffList;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;

public class EventModeListener implements Listener
{
    private final NovelMC plugin;
    
    private static boolean EVENTMODE = false;
    
    public EventModeListener(NovelMC plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static boolean isEventModeEnabled()
    {
        return EVENTMODE;
    }
    
    public static void enableEventMode()
    {
        if (isEventModeEnabled())
        {
            return;
        }
        
        EVENTMODE = true;
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.setWhitelisted(true);
        }
        Bukkit.getServer().setWhitelist(true);
        Bukkit.broadcastMessage(ChatColor.RED + "The server has entered event mode, now all online players are now whitelisted!");
    }
    
    public static void disableEventMode()
    {
        if (!isEventModeEnabled())
        {
            return;
        }
        
        EVENTMODE = false;
        for (OfflinePlayer player : Bukkit.getWhitelistedPlayers())
        {
            player.setWhitelisted(false);
        }
        Bukkit.getServer().setWhitelist(false);
        Bukkit.broadcastMessage(ChatColor.RED + "The server has left event mode, have a great rest of your day!");
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        Player player = e.getPlayer();
        if (isEventModeEnabled() && !player.isWhitelisted() && !StaffList.isStaff(player))
        {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The server is currently in event mode!");
        }
    }
}
