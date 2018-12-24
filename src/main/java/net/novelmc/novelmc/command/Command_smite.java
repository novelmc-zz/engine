package net.novelmc.novelmc.command;

import net.novelmc.novelmc.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Smites a bad player", usage = "/<command> <player> [reason]", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_smite
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        String reason = null;
        if (player == null)
        {
            sender.sendMessage(ChatColor.RED + "Cannot find that player.");
            return true;
        }

        if (args.length > 2)
        {
            reason = StringUtils.join(args, " ", 1, args.length);
        }

        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Smiting " + player.getName()
                + (reason != null ? "\n Reason: " + ChatColor.YELLOW + reason : ""));
        player.setGameMode(GameMode.SURVIVAL);
        player.setOp(false);
        player.getInventory().clear();
        player.getWorld().strikeLightningEffect(player.getLocation());
        player.setHealth(0);
        return true;
    }
}
