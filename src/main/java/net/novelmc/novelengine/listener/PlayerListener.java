package net.novelmc.novelengine.listener;

import net.novelmc.novelengine.banning.Ban;
import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.command.Command_vanish;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.architect.ArchitectList;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NPlayer;
import net.novelmc.novelengine.util.NUtil;
import net.novelmc.novelengine.util.NovelBase;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;


public class PlayerListener extends NovelBase implements Listener
{
    public PlayerListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();

        plugin.playerDatabase.add(player.getName(), player.getAddress().getHostString());
        player.setOp(true);

        if (StaffList.isStaff(player))
        {
            if ( ! StaffList.getStaff(player).getIps().contains(player.getAddress().getHostString()) &&  ! StaffList.getStaff(player).getHomeIp().equals(player.getAddress().getHostString()))
            {
                NUtil.globalMessage(NUtil.colorize("&8<-> &4&lSTAFF &cALERT: " + player.getName() + " has been flagged as a staff impostor!"));
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(NUtil.colorize("&2&lINFO >&r &cYou have been marked as an impostor, please verify yourself.")); //Yes, I do now
                StaffList.getImpostors().add(player.getName());

                return;
            }

            player.setPlayerListName(StringUtils.substring(Rank.getDisplay(player).getColor() + player.getName(), 0, 16));
        }

        if (ArchitectList.isArchitect(player) &&  ! StaffList.isStaff(player))
        {
            if ( ! ArchitectList.getArchitect(player).getIps().contains(player.getAddress().getHostString()))
            {
                NUtil.globalMessage(NUtil.colorize("&4&lSTAFF >&r &cNOTICE: " + player.getName() + " has been flagged as a staff impostor!"));
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(NUtil.colorize("&2&lINFO >&r &7You have been marked as an impostor, please verify yourself."));
                ArchitectList.getImpostors().add(player.getName());

                return;
            }

            player.setPlayerListName(StringUtils.substring(Rank.getRank(player).getColor() + player.getName(), 0, 16));
        }
        Command_vanish.VANISHED.stream().filter((p) -> ( ! StaffList.isStaff(player))).forEachOrdered((p) ->
        {
            player.hidePlayer(plugin, p);
        });
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        final Player player = event.getPlayer();
        Ban ban = BanManager.getBan(event.getPlayer().getUniqueId().toString(), event.getAddress().getHostAddress());

        if (ban != null &&  ! ban.isExpired())
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
        if (Command_vanish.VANISHED.contains(event.getPlayer()))
        {
            event.setQuitMessage(null);
            NUtil.globalMessage(NUtil.colorize("&4&lSTAFF >&r " + event.getPlayer().getDisplayName() + " has silently logged out."), NUtil.MessageType.STAFF_ONLY);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        EmoteListener.handleEmotes(event);
        if (NPlayer.isBusy(player))
        {
            NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &7" + player.getName() + " is no longer marked as busy."), NUtil.MessageType.ALL);
            NPlayer.busyPlayers.remove(player);
        }
        if (StaffList.isStaff(event.getPlayer()) && event.getMessage().startsWith(">"))
        {
            NUtil.globalMessage(NUtil.colorize("&4&lSTAFF >&r &7" + Rank.getDisplay(player).getTag() + " &7" + player.getName() + "&8: ") + ChatColor.GOLD + event.getMessage().substring(1), NUtil.MessageType.STAFF_ONLY);
            event.setCancelled(true);
        } else
        {
            event.setFormat(Rank.getDisplay(player).getTag() + " " + ChatColor.GRAY + player.getDisplayName() + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + event.getMessage());
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        //TODO: World system
        Player player = event.getPlayer();
        if (NPlayer.isFrozen(player))
        {
            player.teleport(player);
            return;
        }
        if (NPlayer.isBusy(player))
        {
            NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &7" + player.getName() + " is no longer marked as busy."), NUtil.MessageType.ALL);
            NPlayer.busyPlayers.remove(player);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        //TODO: Freeze system and World system
    }

    @EventHandler
    public void onPlayerCommand(ServerCommandEvent event)
    {
        CommandSender sender = event.getSender();
        if (sender instanceof ConsoleCommandSender)
        {
            return;
        }
        Player player = (Player) sender;
        if (NPlayer.isBusy(player))
        {
            NUtil.globalMessage(NUtil.colorize("&9&lSERVER >&r &7" + player.getName() + " is no longer marked as busy."), NUtil.MessageType.ALL);
            NPlayer.busyPlayers.remove(player);
        }
    }
}
