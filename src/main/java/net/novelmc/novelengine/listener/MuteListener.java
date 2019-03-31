package net.novelmc.novelengine.listener;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import net.novelmc.novelengine.util.NUtil;
import net.novelmc.novelengine.util.NovelBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteListener extends NovelBase implements Listener
{
    public MuteListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    private static List<Player> muted = new ArrayList<Player>();
    
    public static boolean isMuted(Player player)
    {
        return muted.contains(player);
    }
    
    public static void setMuted(Player player, boolean mute)
    {
        if (mute)
        {
            muted.add(player);
            return;
        }
        muted.remove(player);
    }
    
    public static int getMutedAmount()
    {
        return muted.size();
    }
    
    public static void purgeMuted()
    {
        muted.clear();
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        if (isMuted(player))
        {
            e.setCancelled(true);
            player.sendMessage(NUtil.colorize("&2&lINFO >&r &7You are currently muted and cannot chat."));
        }
    }
}
