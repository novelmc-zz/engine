package net.novelmc.novelmc.command;

import net.novelmc.novelmc.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Set your gamemode to survival", aliases = "gms", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_survival
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        Player player = (Player) sender;
        player.setGameMode(GameMode.SURVIVAL);
        sender.sendMessage(ChatColor.GOLD + "Your gamemode have been set to survival!");
        return true;
    }
}
