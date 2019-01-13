package net.novelmc.novelengine.command;

import net.novelmc.novelengine.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Op yourself.", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_opme
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        sender.setOp(true);
        sender.sendMessage(ChatColor.GRAY + "You have been opped.");
        return true;
    }
}
