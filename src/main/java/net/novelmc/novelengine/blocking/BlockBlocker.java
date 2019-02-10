package net.novelmc.novelengine.blocking;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.util.NUtil;
import net.novelmc.novelengine.util.NovelBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBlocker extends NovelBase implements Listener
{

    public BlockBlocker()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        Player player = e.getPlayer();
        switch (e.getBlockPlaced().getType())
        {
            case LAVA:
            {
                if (NovelEngine.plugin.config.isLavaEnabled())
                {
                    return;
                }
                e.setCancelled(true);
                player.sendMessage(NUtil.colorize("&8<-> &3&lINFO &7Lava is currently disabled."));
                break;
            }
            case WATER:
            {
                if (NovelEngine.plugin.config.isWaterEnabled())
                {
                    return;
                }
                e.setCancelled(true);
                player.sendMessage(NUtil.colorize("&8<-> &3&lINFO &7Water is currently disabled."));
                break;
            }
        }
    }
}
