package net.novelmc.novelengine.command;

import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.banning.BanType;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NPlayer;
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
        Pattern ipPattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = ipPattern.matcher(args[0]);
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

            if(reason != null) {
                NUtil.playerAction(sender, String.format("Banning IP: %s" + NEW_LINE + "Reason: " + ChatColor.YELLOW + "%s", args[0], reason), true);
            }
            else {
                NUtil.playerAction(sender, "Banning IP: " + args[0], true);
            }
            BanManager.addBan("", args[0], sender.getName(), reason, NUtil.parseDateOffset("1d"), BanType.IP);
            return true;
        }

        // Not ip
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        if (offlinePlayer.isOnline())
        {
            NPlayer player = (NPlayer)offlinePlayer.getPlayer();

            if (player.isBanned())
            {
                sender.sendMessage(ChatColor.DARK_GRAY + "That player is already banned!");
                return true;
            }

            if (args.length > 1)
            {
                reason = StringUtils.join(args, " ", 1, args.length);
            }

            if (reason != null)
            {
                NUtil.playerAction(sender, String.format(" - Banning %s" + NEW_LINE + "Reason: " + ChatColor.YELLOW + "%s", offlinePlayer.getName(), reason), true);
                player.kickPlayer(ChatColor.RED + "You have been banned!" + NEW_LINE + "Reason: " + ChatColor.YELLOW + reason);
            }
            else
            {
                NUtil.playerAction(sender, " - Banning " + offlinePlayer.getName(), true);
                player.kickPlayer(ChatColor.RED + "You have been banned!");
            }

            BanManager.addBan(player.getName(), player.getAddress().getHostString(), sender.getName(), reason, NUtil.parseDateOffset("1d"), BanType.NORMAL);
        }
        else
        {
            if(!plugin.playerDatabase.containsName(offlinePlayer.getName())) {
                sender.sendMessage(ChatColor.RED + "Offline player not found.");
                return true;
            }

            if (args.length > 1)
            {
                reason = StringUtils.join(args, " ", 1, args.length);
            }

            BanManager.addBan(offlinePlayer.getName(), plugin.playerDatabase.getIp(offlinePlayer.getName()), sender.getName(), reason, NUtil.parseDateOffset("1d"), BanType.NORMAL);

            if(reason != null)
            {
                NUtil.playerAction(sender, String.format(" - Banning %s" + NEW_LINE + "Reason: " + ChatColor.YELLOW + "%s", offlinePlayer.getName(), reason), true);
            }
            else
            {
                NUtil.playerAction(sender, " - Banning " + offlinePlayer.getName(), true);
            }
        }
        return true;
    }
}