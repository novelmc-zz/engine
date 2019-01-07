package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Set your gamemode to adventure", aliases = "gma", source = SourceType.IN_GAME, rank = Rank.OP)
public class AdventureCommand
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        Player player = (Player) sender;
        player.setGameMode(GameMode.ADVENTURE);
        sender.sendMessage(ChatColor.GRAY + "Your gamemode have been set to adventure!");
        return true;
    }
}
