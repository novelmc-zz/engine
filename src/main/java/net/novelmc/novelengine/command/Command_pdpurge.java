package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.PlayerDatabase;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Purges all player data.", usage = "/<command>", source = SourceType.BOTH, rank = Rank.ADMIN)
public class Command_pdpurge extends CommandBase
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        PlayerDatabase pd = new PlayerDatabase();
        pd.purge();

        if ((sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "All playerdata purged.");
        }
        return true;
    }

}
