package net.novelmc.novelmc.command;

import net.novelmc.novelmc.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Deops a player", usage = "/<command> <player>", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_deop
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(ChatColor.RED + "Cannot find that player!");
            return true;
        }

        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Deopping " + player.getName());
        player.setOp(true);
        player.sendMessage(ChatColor.YELLOW + "You have been deopped!");
        return true;
    }
}
