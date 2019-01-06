package net.novelmc.novelengine.command;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.util.NLog;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CommandLoader
{

    private static CommandMap cmap = getCommandMap();

    public CommandLoader()
    {
        registerCommands();
    }

    public static void registerCommands()
    {
        try
        {
            Pattern pattern = Pattern.compile("net/novelmc/novelengine/command/(Command_[^\\$]+)\\.class");
            CodeSource cs = NovelEngine.class.getProtectionDomain().getCodeSource();
            if (cs != null)
            {
                ZipInputStream zip = new ZipInputStream(cs.getLocation().openStream());
                ZipEntry zipEntry;
                while ((zipEntry = zip.getNextEntry()) != null)
                {
                    String entry = zipEntry.getName();
                    Matcher matcher = pattern.matcher(entry);
                    if (matcher.find())
                    {
                        try
                        {
                            Class<?> commandClass = Class.forName("net.novelmc.novelengine.command." + matcher.group(1));
                            if (commandClass.isAnnotationPresent(CommandParameters.class))
                            {
                                CommandParameters params = commandClass.getAnnotation(CommandParameters.class);
                                NCommand command = new BlankCommand(matcher.group(1).split("_")[1],
                                        params.description(),
                                        params.usage(),
                                        Arrays.asList(params.aliases().split(", ")),
                                        params.source(),
                                        params.rank(),
                                        commandClass);
                                command.register();
                            }
                            else
                            {
                                Constructor constructor = commandClass.getConstructor();
                                NCommand command = (NCommand) constructor.newInstance();
                                command.register();
                            }
                        }
                        catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex)
                        {
                            NLog.severe(ex);
                        }
                    }
                }
            }
        }
        catch (IOException ex)
        {
            NLog.severe(ex);
        }
    }

    private static CommandMap getCommandMap()
    {
        if (cmap == null)
        {
            try
            {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            }
            catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
            {
                NLog.severe(ex);
            }
        }
        else if (cmap != null)
        {
            return cmap;
        }
        return getCommandMap();
    }
}
