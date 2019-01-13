package net.novelmc.novelengine.command;

import net.novelmc.novelengine.rank.Displayable;
import net.novelmc.novelengine.rank.Rank;
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
                sender.sendMessage(ChatColor.GRAY + "That player could not be found.");
                return true;
            }
            sender.sendMessage(message(player));
            return true;
        }

        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.GRAY + "Users from console may only execute this command.");
            return true;
        }

        sender.sendMessage(message((Player) sender));
        return true;
    }

    public String message(Player player)
    {
        Displayable display = Rank.getDisplay(player);
        StringBuilder sb = new StringBuilder()
                .append(ChatColor.GRAY + player.getName() + " is " + display.getLoginMessage());
        return sb.toString();
    }
}
