package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Smites a bad player", usage = "/<command> <player> [reason]", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_smite extends CommandBase
{

    @Override
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

        NUtil.playerAction(sender, "Smiting " + player.getName()
                + (reason != null ? NEW_LINE + " Reason: " + ChatColor.YELLOW + reason + NEW_LINE : ""), true);
        player.setGameMode(GameMode.SURVIVAL);
        player.setOp(false);
        player.getInventory().clear();
        player.getWorld().strikeLightningEffect(player.getLocation());
        player.setHealth(0);
        player.sendMessage(ChatColor.DARK_GRAY + "You have been smitten by " + sender.getName()
                + (reason != null ? NEW_LINE + "Reason: " + ChatColor.GRAY + reason : ""));
        return true;
    }
}
