package net.novelmc.novelengine.command;

import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.banning.BanType;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandParameters(description = "Bans a bad player or IP", usage = "/<command> <player | ip> [reason]", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_ban extends CommandBase
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        String reason = null;
        //check if input is IP
        Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = pattern.matcher(args[0]);
        if (m.matches())
        {
            if (BanManager.isIPBanned(args[0]))
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "That IP is already banned!");
                return true;
            }

            if (args.length > 2)
            {
                reason = StringUtils.join(args, " ", 1, args.length);
            }
            else
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "You must specify a reason!");
                return true;
            }

            NUtil.playerAction(sender, "Banning IP: " + args[0]
                    + (reason != null ? "\n Reason: " + ChatColor.YELLOW + reason + "\n" : ""), true);
            BanManager.addBan(sender, "", args[0], reason, NUtil.parseDateOffset("1d"), BanType.IP);
            return true;
        }

        //not ip
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        if (player.isOnline())
        {
            Player p = player.getPlayer();

            if (BanManager.isBanned(p))
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "That player is already banned!");
                return true;
            }

            if (args.length > 2)
            {
                reason = StringUtils.join(args, " ", 1, args.length);
            }
            else
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "You must specify a reason!");
                return true;
            }

            NUtil.playerAction(sender, " - Banning " + player.getName()
                    + (reason != null ? "\n Reason: " + ChatColor.YELLOW + reason + "\n" : ""), true);

            BanManager.getBansByType(BanType.IP).stream().filter((ban) -> (ban.getIp().equals(p.getAddress().getHostString()))).forEachOrdered((ban) ->
            {
                BanManager.removeBan(ban);
            });

            BanManager.addBan(sender, p, reason, NUtil.stringToDate("1d"), BanType.NORMAL);
            p.kickPlayer(ChatColor.RED + "You have been banned!");
            return true;
        }

        //TODO: Offline bans

        return true;
    }
}