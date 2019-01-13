package net.novelmc.novelengine.command;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.Title;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Toggle staffmode.", source = SourceType.BOTH, rank = Rank.MOD)
public class Command_staffmode extends CommandBase {

    NovelEngine plugin = NovelEngine.getPlugin(NovelEngine.class);
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        boolean staffmode = plugin.config.isStaffModeEnabled();
        if (Rank.getDisplay(sender).getTag().equalsIgnoreCase(Title.DEVELOPER.getTag()) || Rank.getRank(sender).isAtLeast(Rank.MANAGER))
        {
            if (args.length > 1){
                //argslength0 argslength1 argslength2
                sender.sendMessage(ChatColor.RED + "Correct usage is /staffmode or /staffmode <true | false>");
                return true;
            }
            if (args.length == 0){
                if (staffmode){
                    plugin.config.set("general.staffmode", false);
                    plugin.config.save();
                    NUtil.playerAction(sender, "Turning off staffmode", false);
                    return true;
                } else if (!staffmode){
                    plugin.config.set("general.staffmode", true);
                    plugin.config.save();
                    NUtil.playerAction(sender, "Turning on staffmode", false);
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("true")){
                if (staffmode){
                    sender.sendMessage(ChatColor.RED + "StaffMode is already enabled.");
                    return true;
                }
                plugin.config.set("general.staffmode", true);
                plugin.config.save();
                NUtil.playerAction(sender, "Turning on staffmode", false);
                return true;
            }

            if (args[0].equalsIgnoreCase("false")){
                if (!staffmode){
                    sender.sendMessage(ChatColor.RED + "StaffMode is already disabled.");
                    return true;
                }
                plugin.config.set("general.staffmode", false);
                plugin.config.save();
                NUtil.playerAction(sender, "Turning off staffmode", false);
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "You must be either a MANAGER or a DEVELOPER to run this command.");

        return true;
    }

}