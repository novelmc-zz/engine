package net.novelmc.novelengine.command;

import java.util.ArrayList;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NPlayer;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Freeze an in-game player", usage = "/<command> <<player> | global>", aliases = "fr", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_freeze extends CommandBase
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equalsIgnoreCase("global"))
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (!StaffList.isStaff(player))
                {
                    if (NPlayer.isFrozen(player))
                    {
                        NUtil.playerAction(sender, "Disabling the global freeze", false);
                        NPlayer.frozenPlayers.remove(player);
                        return true;
                    }
                    NUtil.playerAction(sender, "Enabling the global freeze", false);
                    NPlayer.frozenPlayers.add(player);
                    return true;
                }
            }
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO&r&8 \u00BB &7That player could not be found."));
            return true;
        }

        if (NPlayer.isFrozen(player))
        {
            sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO&r&8 \u00BB &7" + player.getName() + " is no longer frozen."));
            player.sendMessage(NUtil.colorize("&8<-> &3&lINFO&r&8 \u00BB &7You are no longer frozen."));
            NPlayer.frozenPlayers.remove(player);
            return true;
        }
        sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO&r&8 \u00BB &7" + player.getName() + " is now frozen."));
        player.sendMessage(NUtil.colorize("&8<-> &3&lINFO&r&8 \u00BB &7You are now frozen."));
        NPlayer.frozenPlayers.add(player);
        return true;
    }
}
