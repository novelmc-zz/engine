package net.novelmc.novelmc.command;

import net.novelmc.novelmc.rank.Rank;
import net.novelmc.novelmc.staff.StaffList;
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
                    if (!Rank.getRank(sender).isAtLeast(Rank.ADMIN))
                    {
                        sender.sendMessage(ChatColor.RED + "You must be at least Admin to be able to execute this command!");
                        return true;
                    }

                    if (sender instanceof Player)
                    {
                        sender.sendMessage(ChatColor.RED + "You must be on console to be able to execute this command!");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.RED + "Cannot find that player.");
                        return true;
                    }

                    if (StaffList.isStaff(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is already staff!");
                        return true;
                    }

                    if (StaffList.isImpostor(player) || StaffList.getStaff(player).getActive())
                    {
                        Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Readding " + player.getName() + " to the staff list");
                        StaffList.getImpostors().remove(player.getName());
                        StaffList.addIp(player, player.getAddress().getHostString());
                        return true;
                    }

                    Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Adding " + player.getName() + " to the staff list");
                    StaffList.addStaff(player);
                    return true;
                }

                if (args[0].equalsIgnoreCase("remove"))
                {
                    if (!Rank.getRank(sender).isAtLeast(Rank.ADMIN))
                    {
                        sender.sendMessage(ChatColor.RED + "You must be at least General Manager to be able to execute this command!");
                        return true;
                    }

                    if (sender instanceof Player)
                    {
                        sender.sendMessage(ChatColor.RED + "You must be on console to be able to execute this command!");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.RED + "Cannot find that player.");
                        return true;
                    }

                    if (!StaffList.isStaff(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is not staff!");
                        return true;
                    }

                    Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Removing " + player.getName() + " from the staff list");
                    StaffList.removeStaff(StaffList.getStaff(player));
                    return true;
                }

                if (args[0].equalsIgnoreCase("info"))
                {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.RED + "Cannot find that player.");
                        return true;
                    }

                    if (!StaffList.isStaff(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is not staff!");
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
                    if (!Rank.getRank(sender).isAtLeast(Rank.ADMIN))
                    {
                        sender.sendMessage(ChatColor.RED + "You must be at least Admin to execute this command!");
                        return true;
                    }

                    if (sender instanceof Player)
                    {
                        sender.sendMessage(ChatColor.RED + "You must be on console to be able to execute this command!");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.RED + "Cannot find that player!");
                        return true;
                    }

                    if (!StaffList.isStaff(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is not a staff!");
                        return true;
                    }

                    Rank rank = Rank.findRank(args[2]);
                    if (rank == null)
                    {
                        sender.sendMessage(ChatColor.RED + "Invalid rank!");
                        return true;
                    }

                    if (!rank.isAtLeast(Rank.TRAINEE))
                    {
                        sender.sendMessage(ChatColor.RED + "The rank must be at least Trainee!");
                        return true;
                    }

                    Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Setting " + player.getName() + "'s rank to " + rank.getName());
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
