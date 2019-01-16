package net.novelmc.novelengine.command;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.listener.EventModeListener;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Toggles event mode", usage = "/<command> [on | off]", aliases = "emode, em", source = SourceType.BOTH, rank = Rank.MANAGER)
public class Command_eventmode extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        switch (args[0])
        {
            case "on":
            {
                if (NovelEngine.plugin.config.isEventModeEnabled())
                {
                    sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO &7Event mode has already been enabled."));
                    return true;
                }

                NUtil.playerAction(sender, "Turning event mode on", true);
                EventModeListener.enableEventMode();
                return true;
            }
            case "off":
            {
                if (!NovelEngine.plugin.config.isEventModeEnabled())
                {
                    sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO &7Event mode has not been enabled."));
                    return true;
                }

                NUtil.playerAction(sender, "Turning event mode off", true);
                EventModeListener.disableEventMode();
                return true;
            }
        }
        sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO &7Event mode is currently " + (NovelEngine.plugin.config.isEventModeEnabled() ? "enabled." : "disabled.")));
        return true;
    }
}
