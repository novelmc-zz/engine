package net.novelmc.novelengine.blocking;

import java.lang.reflect.Field;
import java.util.Iterator;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.NPlayer;
import net.novelmc.novelengine.util.NUtil;
import static net.novelmc.novelengine.util.NovelBase.plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlocker implements Listener
{
    protected CommandMap cmap = getCommandMap();
    private boolean chk = false;
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        final Player player = event.getPlayer();

        if (StaffList.isStaff(player))
        {
            chk = true;
        }

        Bukkit.getOnlinePlayers().stream().filter((all) -> (NPlayer.hasCommandSpyEnabled(all) && StaffList.isStaff(all))).forEachOrdered((all) ->
        {
            all.sendMessage(NUtil.colorize("&7" + player.getName() + ": " + event.getMessage()));
        });

        for (String blocked : plugin.config.getDefaultCommands())
        {
            if (event.getMessage().equalsIgnoreCase(blocked) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked))
            {
                player.sendMessage(NUtil.colorize("&2&lINFO >&r &7That command is blocked!"));
                event.setCancelled(true);
                continue;
            }
            if (cmap.getCommand(blocked) == null)
            {
                continue;
            }
            if (cmap.getCommand(blocked).getAliases() == null)
            {
                continue;
            }
            cmap.getCommand(blocked).getAliases().stream().filter((blocked2) -> (event.getMessage().equalsIgnoreCase(blocked2) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked2))).map((_item) ->
            {
                player.sendMessage(NUtil.colorize("&2&lINFO >&r &7That command is blocked!"));
                return _item;
            }).forEachOrdered((_item) ->
            {
                event.setCancelled(true);
            });
        }

        if (event.getMessage().contains(":"))
        {
            player.sendMessage(NUtil.colorize("&2&lINFO >&r &7Plugin-specific commands are blocked."));
            event.setCancelled(true);
        }

        for (String blocked : plugin.config.getStaffCommands())
        {
            if ((event.getMessage().equalsIgnoreCase(blocked) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked)) &&  ! chk)
            {
                player.sendMessage(NUtil.colorize("&2&lINFO >&r &7That command is blocked!"));
                event.setCancelled(true);
                continue;
            }

            if (cmap.getCommand(blocked) == null)
            {
                continue;
            }

            if (cmap.getCommand(blocked).getAliases() == null)
            {
                continue;
            }

            cmap.getCommand(blocked).getAliases().stream().filter((blocked2) -> ((event.getMessage().equalsIgnoreCase(blocked2) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked2)) &&  ! chk)).map((_item) ->
            {
                player.sendMessage(NUtil.colorize("&2&lINFO >&r &7That command is blocked!"));
                return _item;
            }).forEachOrdered((_item) ->
            {
                event.setCancelled(true);
            });
        }
    }
    
    private CommandMap getCommandMap()
    {
        if (cmap == null)
        {
            try
            {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
            {
                NLog.severe(e);
            }
        } else if (cmap != null)
        {
            return cmap;
        }
        return getCommandMap();
    }
}