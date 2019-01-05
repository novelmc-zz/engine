package net.novelmc.novelmc.command;

import net.novelmc.novelmc.rank.Displayable;
import net.novelmc.novelmc.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Check your rank", usage = "/<command> [player]", source = SourceType.BOTH, rank = Rank.OP)
public class Command_rank
{
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length == 1)
        {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null)
            {
                sender.sendMessage(ChatColor.RED + "Cannot find that player!");
                return true;
            }
            sender.sendMessage(message(player));
            return true;
        }
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Users from console cannot execute this command!");
            return true;
        }
        sender.sendMessage(message((Player) sender));
        return true;
    }
    
    public String message(Player player)
    {
        Rank rank = Rank.getRank(player);
        Displayable display = Rank.getDisplay(player);
        StringBuilder sb = new StringBuilder()
                    .append(ChatColor.AQUA + player.getName() + " is " + rank.getLoginMessage());
        if (rank != display)
        {
            sb.append(ChatColor.AQUA + " (" + display.getColor() + display.getName() + ChatColor.AQUA + ")");
        }
        return sb.toString();
    }
}
