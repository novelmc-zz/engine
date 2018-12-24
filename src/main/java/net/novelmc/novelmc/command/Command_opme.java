package net.novelmc.novelmc.command;

import net.novelmc.novelmc.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Op yourself.", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_opme
{

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {
        Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Opping " + sender.getName());
        sender.setOp(true);
        return true;
    }
}
