package net.novelmc.novelengine.command;

import java.util.ArrayList;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NPlayer;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Mark yourself as being away or busy", usage = "/<command>", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_busy extends CommandBase
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 0)
        {
            return false;
        }

        Player player = (Player) sender;
        if (NPlayer.isBusy(player))
        {
            NUtil.globalMessage(NUtil.colorize("&8<-> &a&lSERVER&r&8 \u00BB &7" + sender.getName() + " is no longer marked as busy."), NUtil.MessageType.ALL);
            NPlayer.busyPlayers.remove(player);
            return true;
        }
        NUtil.globalMessage(NUtil.colorize("&8<-> &a&lSERVER&r&8 \u00BB &7" + sender.getName() + " is now marked as busy."), NUtil.MessageType.ALL);
        NPlayer.busyPlayers.add(player);
        return true;
    }
}
