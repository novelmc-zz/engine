package net.novelmc.novelmc.command;

import net.novelmc.novelmc.architect.ArchitectList;
import net.novelmc.novelmc.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Manage an architect", usage = "/<command> <add <player> | remove <player> | info <player>", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_architect
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

                    if (ArchitectList.isArchitect(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is already an architect!");
                        return true;
                    }

                    if (ArchitectList.isImpostor(player))
                    {
                        Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Re-adding " + player.getName() + " to the architect list");

                        if (ArchitectList.isImpostor(player))
                        {
                            ArchitectList.getImpostors().remove(player.getName());
                        }

                        ArchitectList.updateIp(player);
                        return true;
                    }

                    Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Adding " + player.getName() + " to the architect list");
                    ArchitectList.addArchitect(player);
                    return true;
                }

                if (args[0].equalsIgnoreCase("remove"))
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

                    if (!ArchitectList.isArchitect(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is not an architect!");
                        return true;
                    }

                    Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Removing " + player.getName() + " from the architect list");
                    ArchitectList.removeArchitect(ArchitectList.getArchitect(player));
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

                    if (!ArchitectList.isArchitect(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is not an architect!");
                        return true;
                    }

                    sender.sendMessage(ChatColor.GRAY + ArchitectList.getArchitect(player).toString());
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
