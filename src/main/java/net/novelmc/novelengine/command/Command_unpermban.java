package net.novelmc.novelengine.command;

import net.novelmc.novelengine.banning.Ban;
import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.banning.BanType;
import net.novelmc.novelengine.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandParameters(description = "Remove a permban", usage = "/<command> <username | ip>", source = SourceType.BOTH, rank = Rank.MANAGER)
public class Command_unpermban
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        //check if input is IP
        Pattern patt = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = patt.matcher(args[0]);
        if (m.matches())
        {
            for (Ban ban : BanManager.getBansByType(BanType.PERMANENT_IP))
            {
                if (ban.getIp().equals(args[0]))
                {
                    BanManager.removeBan(ban);
                    sender.sendMessage(ChatColor.GREEN + "Removed permanent ban for IP " + args[0]);
                    return true;
                }
            }

            sender.sendMessage(ChatColor.GRAY + "That IP-address has not been permanently banned.");
            return true;
        }

        for (Ban ban : BanManager.getBansByType(BanType.PERMANENT_NAME))
        {
            if (ban.getName().equals(args[0]))
            {
                BanManager.removeBan(ban);
                sender.sendMessage(ChatColor.GREEN + "Removed permanent ban for name " + args[0]);
                return true;
            }
        }
        sender.sendMessage(ChatColor.GRAY + "That name has not been permanently banned!");
        return true;
    }
}
