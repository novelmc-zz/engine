package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Manage a staff member", usage = "/<command> <add <player> | remove <player> | setrank <player> <rank> | info <player>", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_staff extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 2)
        {
            return false;
        }

        switch (args.length)
        {
            case 2:
            {
                if (args[0].equalsIgnoreCase("add"))
                {
                    if ( ! Rank.getRank(sender).isAtLeast(Rank.ADMIN))
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7You must be a &9&lADMIN&r&7 to be able to execute this command."));
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7That player could not be found."));
                        return true;
                    }

                    if (StaffList.isStaff(player))
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7That player is already staff."));
                        return true;
                    }

                    if (StaffList.isImpostor(player))
                    {
                        NUtil.playerAction(sender, "Re-adding " + player.getName() + " to the staff list", true);

                        if (StaffList.isImpostor(player))
                        {
                            StaffList.getImpostors().remove(player.getName());
                        }

                        StaffList.updateIp(player);
                        return true;
                    }

                    NUtil.playerAction(sender, "Adding " + player.getName() + " to the staff list", true);
                    StaffList.addStaff(player);
                    return true;
                }

                if (args[0].equalsIgnoreCase("remove"))
                {
                    if ( ! Rank.getRank(sender).isAtLeast(Rank.ADMIN))
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7You must be a &9&lADMIN&r&7 to execute this command!"));
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7That player could not be found."));
                        return true;
                    }

                    if ( ! StaffList.isStaff(player))
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7That player is not a staff member."));
                        return true;
                    }

                    NUtil.playerAction(sender, "Removing " + player.getName() + " from the staff list", true);
                    StaffList.removeStaff(StaffList.getStaff(player));
                    return true;
                }

                if (args[0].equalsIgnoreCase("info"))
                {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7That player could not be found."));
                        return true;
                    }

                    if ( ! StaffList.isStaff(player))
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7That player is not staff member."));
                        return true;
                    }

                    sender.sendMessage(ChatColor.GRAY + StaffList.getStaff(player).toString());
                    return true;
                }
            }
            case 3:
            {
                if (args[0].equalsIgnoreCase("setrank"))
                {
                    if ( ! Rank.getRank(sender).isAtLeast(Rank.ADMIN))
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7You must be a &9&lDIRECTOR&r&7 to to execute this command."));
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7That player could not be found."));
                        return true;
                    }

                    if ( ! StaffList.isStaff(player))
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7That player is not a staff member."));
                        return true;
                    }

                    Rank rank = Rank.findRank(args[2]);
                    if (rank == null)
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7The provided rank is invalid."));
                        return true;
                    }
                    
                    if ((rank == Rank.DIRECTOR) && (!Rank.getRank(sender).isAtLeast(Rank.DIRECTOR))) {
                        sender.sendMessage("&4&lSTAFF >&r &7The provided rank is invalid.");
                        return true;
                    }

                    if ( ! rank.isAtLeast(Rank.TRAINEE))
                    {
                        sender.sendMessage(NUtil.colorize("&4&lSTAFF >&r &7The rank provided must be &3&lTRAINEE&r&7 or higher."));
                        return true;
                    }

                    NUtil.playerAction(sender, "Setting " + player.getName() + "'s rank to " + rank.getName(), true);
                    StaffList.updateRank(player, rank);
                    return true;
                }
            }
            default:
            {
                return false;
            }
        }
    }
}
