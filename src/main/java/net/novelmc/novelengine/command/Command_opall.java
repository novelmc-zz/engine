package net.novelmc.novelengine.command;

import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Ops everyone on the server", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_opall
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        NUtil.playerAction(sender, "Opping everyone on the server", false);

        Bukkit.getOnlinePlayers().forEach((player) ->
        {
            player.setOp(true);
        });

        return true;
    }
}
