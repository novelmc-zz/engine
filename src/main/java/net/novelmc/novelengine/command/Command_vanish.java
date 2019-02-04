package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@CommandParameters(description = "Vanish/unvanish from existence! Well, hopefully.", source = SourceType.IN_GAME, rank = Rank.TRAINEE)
public class Command_vanish extends CommandBase
{

    //Credit: Base Template provided by TFPatches
    public static ArrayList<Player> VANISHED = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {

        boolean silent = false;

        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("-s") || args[0].equalsIgnoreCase("-silent"))
            {
                silent = true;
            }
        }

        Player player = (Player) sender;

        if (VANISHED.contains(player))
        {
            Bukkit.getOnlinePlayers().forEach((online) -> 
            {
                online.showPlayer(plugin, player);
            });

            if (!silent)
            {
                NUtil.globalMessage(ChatColor.YELLOW + player.getName() + " joined the game", NUtil.MessageType.ALL);
            }
            NUtil.globalMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &7Crack! " + player.getName() + " has reappeared!"), NUtil.MessageType.STAFF_ONLY);
            VANISHED.remove(player);
        }
        else
        {

            Bukkit.getOnlinePlayers().stream().filter((online) -> (!StaffList.isStaff(online))).forEachOrdered((online) -> 
            {
                online.hidePlayer(plugin, player);
            });
            if (!silent)
            {
                NUtil.globalMessage(ChatColor.YELLOW + player.getName() + " left the game", NUtil.MessageType.ALL);
            }
            NUtil.globalMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &7Poof! " + player.getName() + " has vanished!"), NUtil.MessageType.STAFF_ONLY);
            VANISHED.add(player);
        }
        return true;
    }
}
