package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Displayable;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Check your rank", usage = "/<command> [player]", source = SourceType.BOTH, rank = Rank.OP)
public class Command_rank extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length == 1)
        {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null)
            {
                sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO&r&8 \u00BB &7That player could not be found."));
                return true;
            }
            sender.sendMessage(message(player));
            return true;
        }

        if ( ! (sender instanceof Player))
        {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null)
            {
                sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO&r&8 \u00BB &7That player could not be found."));
                return true;
            }
            sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO&r&8 \u00BB &7Users from console may only view other players. [/rank <player>]"));
            return true;
        }

        sender.sendMessage(message((Player) sender));
        return true;
    }

    public String message(Player player)
    {
        Displayable display = Rank.getDisplay(player);
        Rank rank = Rank.getRank(player);
        StringBuilder sb = new StringBuilder().append(ChatColor.GRAY).append(player.getName()).append(" is ")
                .append(display.getLoginMessage());
        if (rank != display)
        {
            sb.append(ChatColor.GRAY).append(" (").append(rank.getColor()).append(rank.getName()).append(ChatColor.GRAY).append(")");
        }
        return sb.toString();
    }
}
