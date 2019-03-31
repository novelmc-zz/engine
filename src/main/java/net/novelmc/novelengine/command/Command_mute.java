package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.listener.MuteListener;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Mutes someone", usage = "/<command> <player | purge>", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_mute extends CommandBase
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }
        if (args[0].equalsIgnoreCase("purge"))
        {
            int amount = MuteListener.getMutedAmount();
            MuteListener.purgeMuted();
            NUtil.playerAction(sender, " - Unmuting all players", true);
            sender.sendMessage(NUtil.colorize("&2&lINFO >&r &7Unmuted " + amount + " player(s)."));
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(NUtil.colorize("&2&lINFO >&r &7That player could not be found."));
            return true;
        }
        MuteListener.setMuted(player, !MuteListener.isMuted(player));
        NUtil.playerAction(sender, " - " + (MuteListener.isMuted(player) ? "M" : "Unm") + "uting " + player.getName(), true);
        return true;
    }
}