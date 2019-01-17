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

@CommandParameters(description = "Unbans a player or an IP", usage = "/<command> <username | ip>", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_unban extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        String target = args[0];

        // normal bans
        for (Ban ban : BanManager.getBansByType(BanType.NORMAL))
        {
            if (ban.getName().equals(target) || ban.getIp().equals(target))
            {
                NUtil.playerAction(sender, "- Unbanning " + ban.getName(), false);
                BanManager.removeBan(ban);
                return true;
            }
        }

        //ip bans
        for (Ban ban : BanManager.getBansByType(BanType.IP))
        {
            if (ban.getIp().equals(target))
            {
                NUtil.playerAction(sender, "- Unbanning " + target, false);
                BanManager.removeBan(ban);
                return true;
            }
        }

        Pattern patt = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = patt.matcher(args[0]);
        sender.sendMessage(m.matches() ? NUtil.colorize("&8<-> &4&lSTAFF &7IP is not banned.") : NUtil.colorize("&8<-> &4&lSTAFF &7Player is not banned."));
        return true;
    }
}
