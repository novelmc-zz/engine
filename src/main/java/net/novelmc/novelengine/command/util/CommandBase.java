package net.novelmc.novelengine.command.util;

import net.novelmc.novelengine.util.NovelBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class CommandBase extends NovelBase implements CommandExecutor, TabCompleter
{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        return null;
    }
}
