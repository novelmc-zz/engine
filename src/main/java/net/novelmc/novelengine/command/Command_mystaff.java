package net.novelmc.novelengine.command;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.staff.StaffList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Configure your admin profile", usage = "/<command> [sethomeip | clearhomeip]", source = SourceType.IN_GAME, rank = Rank.TRAINEE)
public class Command_mystaff extends CommandBase
{
    private static NovelEngine plugin;
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        Player player = (Player) sender;
        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("sethomeip"))
            {
                StaffList.updateHomeIp(player);
                sender.sendMessage(ChatColor.GRAY + "Current IP address set as your home IP address.");
                return true;
            }
            if (args[0].equalsIgnoreCase("clearhomeip"))
            {
                StaffList.clearHomeIp(player);
                sender.sendMessage(ChatColor.GRAY + "You no longer have a home IP address.");
                return true;
            }
        }
        return false;
    }
}