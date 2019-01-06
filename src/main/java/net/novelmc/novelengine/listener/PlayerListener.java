package net.novelmc.novelengine.listener;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.architect.ArchitectList;
import net.novelmc.novelengine.banning.Ban;
import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.staff.StaffList;
import net.novelmc.novelengine.util.NLog;
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

    private final NovelEngine plugin;
    private CommandMap cmap = getCommandMap();

    public PlayerListener(NovelEngine plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();

        player.setOp(true);

        if (StaffList.isStaff(player))
        {
            if (!StaffList.getStaff(player).getIps().contains(player.getAddress().getHostString()))
            {
                for (Player all : Bukkit.getOnlinePlayers())
                {
                    if (StaffList.isStaff(all))
                    {
                        all.sendMessage(ChatColor.RED + "NOTICE: " + player.getName() + " has been flagged as a staff impostor!");
                    }
                }
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(ChatColor.RED + "You have been marked as an impostor, please verify yourself.");
                StaffList.getImpostors().add(player.getName());

                return;
            }

            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " is " + Rank.getDisplay(player).getLoginMessage());
            player.setPlayerListName(StringUtils.substring(Rank.getDisplay(player).getColor() + player.getName(), 0, 16));
        }

        if (ArchitectList.isArchitect(player) && !StaffList.isStaff(player))
        {
            if (!ArchitectList.getArchitect(player).getIps().contains(player.getAddress().getHostString()))
            {
                for (Player all : Bukkit.getOnlinePlayers())
                {
                    if (StaffList.isStaff(all))
                    {
                        all.sendMessage(ChatColor.RED + "NOTICE: " + player.getName() + " has been flagged as an architect impostor!");
                    }
                }
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(ChatColor.RED + "You have been marked as an impostor, please verify yourself.");
                ArchitectList.getImpostors().add(player.getName());

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
        if (ArchitectList.getImpostors().contains(event.getPlayer().getName()))
        {
            ArchitectList.getImpostors().remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        event.setFormat(Rank.getDisplay(event.getPlayer()).getTag() + " " + ChatColor.GRAY + event.getPlayer().getDisplayName() + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + event.getMessage());
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

        for (String blocked : plugin.config.getStaffCommands())
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