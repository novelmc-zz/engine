package net.novelmc.novelengine.command;

import net.novelmc.novelengine.banning.Ban;
import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.banning.BanType;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandParameters(description = "Remove a permban", usage = "/<command> <username | ip>", source = SourceType.BOTH, rank = Rank.ADMIN)
public class Command_unpermban extends CommandBase
{

    @Override
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
                    NUtil.playerAction(sender, "Removing permanent ban for IP: " + m, true);
                    sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &aRemoved permanent ban for IP " + args[0]));
                    return true;
                }
            }

            sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &7That IP-address has not been permanently banned."));
            return true;
        }

        for (Ban ban : BanManager.getBansByType(BanType.PERMANENT_NAME))
        {
            if (ban.getName().equals(args[0]))
            {
                BanManager.removeBan(ban);
                NUtil.playerAction(sender, "Removing permanent ban on username: " + args[0], true);
                sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &aRemoved permanent ban for name " + args[0]));
                return true;
            }
        }
        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &7That name does not appear to be be permanently banned!"));
        return true;
    }
}
