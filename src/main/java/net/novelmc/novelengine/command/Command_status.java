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

@CommandParameters(description = "Check the server and world status.", source = SourceType.BOTH, rank = Rank.OP)
public class Command_status extends CommandBase {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        sender.sendMessage(NUtil.colorize("&7Server Information&8:"));
        sender.sendMessage(NUtil.colorize("&8<-> &7Onlinemode&8:" + (Bukkit.getServer().getOnlineMode() ? "&a&ltrue" : "&c&lfalse") + "&7."));
        sender.sendMessage(NUtil.colorize("&8<-> &7Plugin Version&8: &9&l" + plugin.getDescription().getVersion()));
        int i = 0;
        for (World world : Bukkit.getServer().getWorlds()) {
            sender.sendMessage(NUtil.colorize("&7" + String.format("World %d&8: &7%s with &9&l%d&r&7 players.", i++, world.getName(), world.getPlayers().size())));
        }
        return true;
    }
}
