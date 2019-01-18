package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Ops a player", usage = "/<command> <player>", source = SourceType.BOTH, rank = Rank.OP)
public class Command_op extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO&r&8 \u00BB &7That player could not be found."));
            return true;
        }

        NUtil.playerAction(sender, "Opping " + player.getName(), false);
        player.setOp(true);
        return true;
    }
}
