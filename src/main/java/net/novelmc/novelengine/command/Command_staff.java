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

@CommandParameters(description = "Manage an admin", usage = "/<command> <add <player> | remove <player> | setrank <player> <rank> | info <player>", source = SourceType.BOTH, rank = Rank.TRAINEE)
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
                    if (!Rank.getRank(sender).isAtLeast(Rank.MANAGER))
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7You must be a Manager to be able to execute this command."));
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7That player could not be found."));
                        return true;
                    }

                    if (StaffList.isStaff(player))
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7That player is already staff."));
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
                    if (!Rank.getRank(sender).isAtLeast(Rank.MANAGER))
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7You must be a Manager to execute this command!"));
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7That player could not be found."));
                        return true;
                    }

                    if (!StaffList.isStaff(player))
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7That player is not a staff member."));
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
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7That player could not be found."));
                        return true;
                    }

                    if (!StaffList.isStaff(player))
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7That player is not staff member."));
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
                    if (!Rank.getRank(sender).isAtLeast(Rank.MANAGER))
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7You must be a Manager to to execute this command."));
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7hat player could not be found."));
                        return true;
                    }

                    if (!StaffList.isStaff(player))
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7That player is not a staff member."));
                        return true;
                    }

                    Rank rank = Rank.findRank(args[2]);
                    if (rank == null)
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7The provided rank is invalid."));
                        return true;
                    }

                    if (!rank.isAtLeast(Rank.TRAINEE))
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF &7The rank provided must be Trainee or higher."));
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
