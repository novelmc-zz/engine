package net.novelmc.novelengine.command;

import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.banning.BanType;
import net.novelmc.novelengine.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandParameters(description = "Permanently ban a username or IP", usage = "/<command> <username | ip> [reason]", source = SourceType.BOTH, rank = Rank.MANAGER)
public class Command_permban
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        String reason = null;
        if (args.length > 2)
        {
            reason = StringUtils.join(args, " ", 1, args.length);
        }

        //check if input is IP
        Pattern patt = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = patt.matcher(args[0]);

        if (m.matches())
        {
            // check if the ip is banned
            if (BanManager.isIPPermBanned(args[0]))
            {
                sender.sendMessage(ChatColor.GRAY + "That IP-address has already been permanently banned!");
                return true;
            }

            BanManager.addBan(sender, "", args[0], reason, null, BanType.PERMANENT_IP);
            sender.sendMessage(ChatColor.GRAY + "Added permanent ban for IP " + args[0]);
            return true;
        }

        // ban name
        if (BanManager.isNamePermBanned(args[0]))
        {
            sender.sendMessage(ChatColor.GRAY + "That username has already been permanently banned.");
            return true;
        }

        BanManager.addBan(sender, args[0], null, reason, null, BanType.PERMANENT_NAME);
        sender.sendMessage(ChatColor.GRAY + "Added permanent ban for name " + args[0]);
        return true;
    }
}
