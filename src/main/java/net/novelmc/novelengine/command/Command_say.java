package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Broadcast your message", usage = "/<command> <message>", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_say extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        Bukkit.broadcastMessage(NUtil.colorize("&8<-> &3&lINFO"
                + " &7" + sender.getName()
                + Rank.getDisplay(sender).getColor() + Rank.getDisplay(sender).getTag() + "&8» &f")
                + StringUtils.join(args, " ", 0, args.length));
        return true;
    }
}
