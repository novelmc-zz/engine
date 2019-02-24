package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Check the Server Status.", source = SourceType.BOTH, rank = Rank.OP)
public class Command_status extends CommandBase {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        sender.sendMessage(NUtil.colorize("&8<-> &3&lINFO&r&8 Information regarding the Server."));
        sender.sendMessage(NUtil.colorize("&8 Server is currently running with 'online-mode=" + (Bukkit.getServer().getOnlineMode() ? "true" : "false") + "'."));
        sender.sendMessage(NUtil.colorize("&8NovelEngine Version: " + plugin.getDescription().getVersion()));
        int i = 0;
        for (World world : Bukkit.getServer().getWorlds()) {
            sender.sendMessage(NUtil.colorize("&8" + String.format("World %d: %s - %d players.", i++, world.getName(), world.getPlayers().size())));
        }
        return true;
    }
}
