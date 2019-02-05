package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.listener.ServerModeListener;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

@CommandParameters(description = "Sets the mode that the server is in", usage = "/<command> [mode]", source = SourceType.BOTH, rank = Rank.ADMIN)
public class Command_mode extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length > 1)
        {
            return false;
        }

        if (args.length == 1)
        {
            switch (args[0])
            {
                case "dev":
                {
                    if (!NUtil.DEVELOPERS.contains(sender.getName()))
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &7You must be a &5&lDEV&r&7 to be able to execute this command."));
                        return true;
                    }
                    if (plugin.config.isDevModeEnabled())
                    {
                        NUtil.playerAction(sender, "Turning developer mode off", true);
                        ServerModeListener.disableDevMode();
                        return true;
                    }
                    NUtil.playerAction(sender, "Turning developer mode on", true);
                    ServerModeListener.enableDevMode();
                    return true;
                }
                case "event":
                {
                    if (plugin.config.isEventModeEnabled())
                    {
                        NUtil.playerAction(sender, "Turning event mode off", true);
                        ServerModeListener.disableEventMode();
                        return true;
                    }
                    NUtil.playerAction(sender, "Turning event mode on", true);
                    ServerModeListener.enableEventMode();
                    return true;
                }
                case "devel":
                {
                    if (!NUtil.DEVELOPERS.contains(sender.getName()))
                    {
                        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &7You must be a &5&lDEV&r&7 to be able to execute this command."));
                        return true;
                    }
                    if (plugin.config.isDevelModeEnabled())
                    {
                        NUtil.playerAction(sender, "Turning development mode off", true);
                        ServerModeListener.disableDevelMode();
                        return true;
                    }
                    NUtil.playerAction(sender, "Turning development mode on", true);
                    ServerModeListener.enableDevelMode();
                    return true;
                }
                case "staff":
                {
                    if (plugin.config.isStaffModeEnabled())
                    {
                        NUtil.playerAction(sender, "Turning staff mode off", true);
                        ServerModeListener.disableStaffMode();
                        return true;
                    }
                    NUtil.playerAction(sender, "Turning staff mode on", true);
                    ServerModeListener.enableStaffMode();
                    return true;
                }
                case "off":
                {
                    NUtil.playerAction(sender, "Returning to normal mode", true);
                    ServerModeListener.disableAllModes();
                    return true;
                }
                default:
                {
                    return false;
                }
            }
        }
        sender.sendMessage(NUtil.colorize("&8<-> &7Server Modes&8:"));
        sender.sendMessage(NUtil.colorize("&8<-> &5&ldev&r&8: &7Plugin testing for leadership and devs."));
        sender.sendMessage(NUtil.colorize("&8<-> &4&lstaff&r&8: &7Staff-only mode."));
        sender.sendMessage(NUtil.colorize("&8<-> &9&levent&r&8: &7Event mode."));
        sender.sendMessage(NUtil.colorize("&8<-> &6&ldevel&r&8: &7Public development testing mode."));
        sender.sendMessage(NUtil.colorize("&8<-> &7&loff&r&8: &7Normal mode functionality."));
        StringBuilder line = new StringBuilder()
                .append(ChatColor.GRAY)
                .append("The server is currently running in ");

        if (plugin.config.isDevModeEnabled())
        {
            line.append("developer mode.");
        } else if (plugin.config.isDevelModeEnabled())
        {
            line.append("development mode.");
        } else if (plugin.config.isEventModeEnabled())
        {
            line.append("event mode.");
        } else if (plugin.config.isStaffModeEnabled())
        {
            line.append("staff mode.");
        } else
        {
            line.append("normal mode.");
        }
        sender.sendMessage(line.toString());
        return true;
    }
}
