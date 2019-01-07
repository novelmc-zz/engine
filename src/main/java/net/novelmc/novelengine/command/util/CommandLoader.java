package net.novelmc.novelengine.command.util;

import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.command.CommandMap;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class CommandLoader {
    private CommandMap commandMap = NUtil.getCommandMap();
    private Reflections reflections = NUtil.getReflections();
    private String prefix;
    private String suffix;

    public CommandLoader(String prefix, String suffix)
    {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public CommandLoader()
    {
        this.prefix = "";
        this.suffix = "";
    }

    public CommandLoader(String prefix)
    {
        this.prefix = prefix;
        this.suffix = "";
    }

    public void registerCommands()
    {
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(CommandParameters.class);

        for(Class<?> clazz : annotated)
        {
            if(clazz.getSimpleName().startsWith(prefix) && clazz.getSimpleName().endsWith(suffix))
            {
                CommandParameters params = clazz.getAnnotation(CommandParameters.class);
                String commandName = clazz.getSimpleName().substring(prefix.length(), clazz.getSimpleName().length()-suffix.length()).toLowerCase();

                NCommand command;
                try
                {
                    command = new BlankCommand(commandName,
                            params.description(),
                            params.usage(),
                            Arrays.asList(params.aliases().split(", ")),
                            params.source(),
                            params.rank(),
                            clazz);
                    command.register();
                }
                catch (NoSuchMethodException e)
                {
                    NLog.severe("Could not load command: " + commandName);
                    e.printStackTrace();
                }
            }
        }
    }
}
