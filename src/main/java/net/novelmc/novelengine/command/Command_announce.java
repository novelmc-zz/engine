package net.novelmc.novelengine.command;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Announce something in a title format", usage = "/<command> <message>", source = SourceType.IN_GAME, rank = Rank.TRAINEE)
public class Command_announce extends CommandBase
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        String msg = StringUtils.join(args, " ", 0, args.length);
        if (msg.length() == 0)
        {
            return false;
        }

        Player player = (Player) sender;

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(NUtil.colorize(msg)).create());
        return true;
    }
}
