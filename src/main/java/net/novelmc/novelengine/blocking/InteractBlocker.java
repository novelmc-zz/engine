package net.novelmc.novelengine.blocking;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.util.NUtil;
import net.novelmc.novelengine.util.NovelBase;
import static net.novelmc.novelengine.util.NovelBase.plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractBlocker extends NovelBase implements Listener
{
    public InteractBlocker()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        switch (e.getAction())
        {
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
            {
                break;
            }
            
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
            {
                handleRMB(e);
                break;
            }
        }
    }
    
    public void handleRMB(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        switch (e.getMaterial())
        {
            case LAVA_BUCKET:
            {
                if (NovelEngine.plugin.config.isLavaEnabled())
                {
                    return;
                }
                e.setCancelled(true);
                player.sendMessage(NUtil.colorize("&7Lava is currently disabled."));
                break;
            }
            case WATER_BUCKET:
            {
                if (NovelEngine.plugin.config.isWaterEnabled())
                {
                    return;
                }
                e.setCancelled(true);
                player.sendMessage(NUtil.colorize("&7Water is currently disabled."));
                break;
            }
        }
    }
}
