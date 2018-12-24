package net.novelmc.novelmc.command;

import net.novelmc.novelmc.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Broadcast your message", usage = "/<command> <message>", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_say
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "[Server:" + sender.getName() + "] " + StringUtils.join(args, " ", 0, args.length));
        return true;
    }
}
