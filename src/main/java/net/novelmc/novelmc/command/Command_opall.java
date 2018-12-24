package net.novelmc.novelmc.command;

import net.novelmc.novelmc.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Ops everyone on the server", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_opall
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Opping everyone on the server");

        Bukkit.getOnlinePlayers().forEach((player) ->
        {
            player.setOp(true);
            player.sendMessage(ChatColor.YELLOW + "You have been opped");
        });
        return true;
    }
}
