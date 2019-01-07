package net.novelmc.novelengine.command.util;

import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NLog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class BlankCommand extends NCommand
{

    private CommandBase commandObject;

    public BlankCommand(String commandName, String usage, String description, List<String> aliases, SourceType source, Rank rank, Class<CommandBase> clazz) throws NoSuchMethodException
    {
        super(commandName, usage, description, aliases, source, rank);
        try
        {
            this.commandObject = clazz.getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            NLog.severe(ex);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        return commandObject.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return commandObject.onTabComplete(sender, command, label, args);
    }
}
