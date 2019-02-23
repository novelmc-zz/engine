package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NPlayer;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Toggle CommandSpy", aliases = "cmdspy", source = SourceType.IN_GAME, rank = Rank.TRAINEE)
public class Command_commandspy extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        Player player = (Player) sender;
        if (NPlayer.hasCommandSpyEnabled(player))
        {
            StaffList.updateCommandSpy(player, false);
            sender.sendMessage(NUtil.colorize("&3&lINFO >&r &7CommandSpy disabled."));
            return true;
        }
        StaffList.updateCommandSpy(player, true);
        sender.sendMessage(NUtil.colorize("&3&lINFO >&r &7CommandSpy enabled."));
        return true;
    }
}
