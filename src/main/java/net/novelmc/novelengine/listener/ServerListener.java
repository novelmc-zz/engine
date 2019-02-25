package net.novelmc.novelengine.listener;

import net.novelmc.novelengine.util.NUtil;
import net.novelmc.novelengine.util.NovelBase;
import org.apache.commons.lang.StringUtils;
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
            event.setMotd(NUtil.colorize("      &f&l»&c&l&m---------&r&f&l» &c&lNOVEL&f&lMC &f&l«&c&l&m---------&r&f&l«\n            &7&l(&r&4&l!&7&l)  &cWhitelist Enabled&r &7&l (&r&4&l!&7&l)"));
            return;
        }

        if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers())
        {
            event.setMotd(NUtil.colorize("      &f&l»&c&l&m---------&r&f&l» &c&lNOVEL&f&lMC &f&l«&c&l&m---------&r&f&l«\n              &7&l(&r&4&l!&7&l)  &cServer is Full&r &7&l (&r&4&l!&7&l)"));
            return;
        }

        List<String> motd = plugin.config.getMOTD();
        event.setMotd(NUtil.colorize(StringUtils.join(motd, "\n")));
    }
}
