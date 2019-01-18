package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Talk with staff members privately.", usage = "/<command> <message>", aliases = "sc, o", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_staffchat extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        NUtil.globalMessage(NUtil.colorize("&b»&6»&a» &7" + Rank.getDisplay(sender).getTag() + " &7" + sender.getName() + "&8: ") + ChatColor.WHITE + StringUtils.join(args, " ", 0, args.length), NUtil.MessageType.STAFF_ONLY);
        Bukkit.getConsoleSender().sendMessage(NUtil.colorize("&b»&6»&a» &7" + Rank.getDisplay(sender).getTag() + " &7" + sender.getName() + "&8: ") + ChatColor.WHITE + StringUtils.join(args, " ", 0, args.length));
        return true;
    }
}
