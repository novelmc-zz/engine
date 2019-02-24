package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Op yourself.", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_opme extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        sender.setOp(true);
        sender.sendMessage(NUtil.colorize("&5&lINFO >&r &7You have been opped."));
        return true;
    }
}
