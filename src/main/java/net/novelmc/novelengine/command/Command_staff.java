package net.novelmc.novelengine.command;

import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.staff.StaffList;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Manage an admin", usage = "/<command> <add <player> | remove <player> | setrank <player> <rank> | info <player>", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_staff
{

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
                        sender.sendMessage(ChatColor.DARK_GRAY + "You must be at least Manager to be able to execute this command!");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.DARK_GRAY + "Cannot find that player.");
                        return true;
                    }

                    if (StaffList.isStaff(player))
                    {
                        sender.sendMessage(ChatColor.DARK_GRAY + "That player is already staff!");
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
                        sender.sendMessage(ChatColor.DARK_GRAY + "You must be at least Manager to be able to execute this command!");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.DARK_GRAY + "Cannot find that player.");
                        return true;
                    }

                    if (!StaffList.isStaff(player))
                    {
                        sender.sendMessage(ChatColor.DARK_GRAY + "That player is not a staff!");
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
                        sender.sendMessage(ChatColor.DARK_GRAY + "Cannot find that player.");
                        return true;
                    }

                    if (!StaffList.isStaff(player))
                    {
                        sender.sendMessage(ChatColor.DARK_GRAY + "That player is not staff!");
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
                        sender.sendMessage(ChatColor.DARK_GRAY + "You must be at least Manager to execute this command!");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.DARK_GRAY + "Cannot find that player!");
                        return true;
                    }

                    if (!StaffList.isStaff(player))
                    {
                        sender.sendMessage(ChatColor.DARK_GRAY + "That player is not a staff!");
                        return true;
                    }

                    Rank rank = Rank.findRank(args[2]);
                    if (rank == null)
                    {
                        sender.sendMessage(ChatColor.DARK_GRAY + "Invalid rank!");
                        return true;
                    }

                    if (!rank.isAtLeast(Rank.TRAINEE))
                    {
                        sender.sendMessage(ChatColor.DARK_GRAY + "The rank must be at least Trainee!");
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
