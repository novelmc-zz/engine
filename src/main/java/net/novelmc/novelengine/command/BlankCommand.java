package net.novelmc.novelengine.command;

import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NLog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class BlankCommand extends NCommand
{

    Class clazz;
    Object object;

    public BlankCommand(String commandName, String usage, String description, List<String> aliases, SourceType source, Rank rank, Class clazz) throws NoSuchMethodException
    {
        super(commandName, usage, description, aliases, source, rank);
        this.clazz = clazz;
        try
        {
            this.object = clazz.getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            NLog.severe(ex);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        try
        {
            return (boolean) clazz.getMethod("onCommand", CommandSender.class, Command.class, String.class, String[].class).invoke(object, sender, cmd, string, args);
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            NLog.severe(ex);
        }
        return false;
    }
}
