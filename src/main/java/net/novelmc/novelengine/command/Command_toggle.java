package net.novelmc.novelengine.command;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Toggles a feature", usage = "/<command> [feature]", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_toggle extends CommandBase
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
                case "lavaplace":
                {
                    NovelEngine.plugin.config.setLavaEnabled(!NovelEngine.plugin.config.isLavaEnabled());
                    sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &7Lava placement has been " + (NovelEngine.plugin.config.isLavaEnabled() ? "enabled." : "disabled.")));
                    return true;
                }
                case "waterplace":
                {
                    NovelEngine.plugin.config.setWaterEnabled(!NovelEngine.plugin.config.isWaterEnabled());
                    sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &7Water placement has been " + (NovelEngine.plugin.config.isWaterEnabled() ? "enabled." : "disabled.")));
                    return true;
                }
            }
        }
        sender.sendMessage(NUtil.colorize("&8<-> &4&lSTAFF&r&8 \u00BB &7Toggleable Features"));
        sender.sendMessage(NUtil.colorize("&7 - lavaplace"));
        sender.sendMessage(NUtil.colorize("&7 - waterplace"));
        return true;
    }
}
