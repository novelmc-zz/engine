package net.novelmc.novelmc.listener;

import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.banning.Ban;
import net.novelmc.novelmc.banning.BanManager;
import net.novelmc.novelmc.rank.Rank;
import net.novelmc.novelmc.staff.StaffList;
import net.novelmc.novelmc.util.NLog;
import net.novelmc.novelmc.util.SQLManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.lang.reflect.Field;

public class PlayerListener implements Listener
{

    private final NovelMC plugin;
    private CommandMap cmap = getCommandMap();

    public PlayerListener(NovelMC plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();

        if (SQLManager.playerExists(player))
        {
            SQLManager.updatePlayer(player);
        }
        else
        {
            SQLManager.generateNewPlayer(player);
        }

        if (StaffList.isStaff(player))
        {
            if (!StaffList.getStaff(player).getIps().contains(player.getAddress().getHostString()))
            {
                StaffList.getImpostors().add(player.getName());
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has been flagged as an imposter!");
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(ChatColor.RED + "You have been marked as an imposter, please verify yourself.");
                return;
            }

            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is " + Rank.getDisplay(player).getLoginMessage());
            player.setPlayerListName(StringUtils.substring(Rank.getRank(player).getColor() + player.getName(), 0, 16));
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        final Player player = event.getPlayer();
        Ban ban = BanManager.getBan(event.getPlayer().getName(), event.getAddress().getHostAddress());

        if (ban != null && !ban.isExpired())
        {
            if (StaffList.isStaff(player))
            {
                event.allow();
                return;
            }

            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ban.getKickMessage());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (StaffList.getImpostors().contains(event.getPlayer().getName()))
        {
            StaffList.getImpostors().remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        event.setFormat(ChatColor.WHITE + "<" + event.getPlayer().getDisplayName() + ChatColor.WHITE + "> " + event.getMessage());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        //TODO: Freeze system and World system
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        //TODO: Freeze system and World system
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        final Player player = event.getPlayer();

        for (String blocked : plugin.config.getDefaultCommands())
        {
            if (event.getMessage().equalsIgnoreCase(blocked) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked))
            {
                player.sendMessage(ChatColor.RED + "That command is blocked!");
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
                player.sendMessage(ChatColor.RED + "That command is blocked!");
                return _item;
            }).forEachOrdered((_item) ->
            {
                event.setCancelled(true);
            });
        }

        for (String blocked : plugin.config.getAdminCommands())
        {
            if ((event.getMessage().equalsIgnoreCase(blocked) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked)) && !StaffList.isStaff(player))
            {
                player.sendMessage(ChatColor.RED + "That command is blocked!");
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

            cmap.getCommand(blocked).getAliases().stream().filter((blocked2) -> ((event.getMessage().equalsIgnoreCase(blocked2) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked2)) && !StaffList.isStaff(player))).map((_item) ->
            {
                player.sendMessage(ChatColor.RED + "That command is blocked!");
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
            }
            catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
            {
                NLog.severe(e);
            }
        }
        else if (cmap != null)
        {
            return cmap;
        }
        return getCommandMap();
    }
}
