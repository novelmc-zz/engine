package net.novelmc.novelengine.command;

import net.novelmc.novelengine.banning.Ban;
import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.banning.BanType;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandParameters(description = "Unbans a player or an IP", usage = "/<command> <username | ip>", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_unban extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        String target = args[0];

        // normal bans
        for (Ban ban : BanManager.getBansByType(BanType.NORMAL))
        {
            if (ban.getName().equals(target) || ban.getIp().equals(target))
            {
                Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Unbanning " + ban.getName());
                BanManager.removeBan(ban);
                return true;
            }
        }

        //ip bans
        for (Ban ban : BanManager.getBansByType(BanType.IP))
        {
            if (ban.getIp().equals(target))
            {
                Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Unbanning IP: " + target);
                BanManager.removeBan(ban);
                return true;
            }
        }

        Pattern patt = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = patt.matcher(args[0]);
        sender.sendMessage(m.matches() ? ChatColor.DARK_GRAY + "IP is not banned." : ChatColor.DARK_GRAY + "Player is not banned.");
        return true;
    }
}