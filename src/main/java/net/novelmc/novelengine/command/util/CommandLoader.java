package net.novelmc.novelengine.command.util;

import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.command.CommandMap;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Set;

public class CommandLoader
{

    private final CommandMap commandMap = NUtil.getCommandMap();
    private final Reflections reflections = getReflections("net.novelmc.novelengine.command");
    private final String prefix;
    private final String suffix;

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
        Set<Class<? extends CommandBase>> annotated = reflections.getSubTypesOf(CommandBase.class);

        annotated.stream().filter((clazz) -> (clazz.getSimpleName().startsWith(prefix) && clazz.getSimpleName().endsWith(suffix))).forEachOrdered((clazz) ->
        {
            NCommand command;
            String commandName = clazz.getSimpleName().substring(prefix.length(), clazz.getSimpleName().length() - suffix.length()).toLowerCase();

            try
            {
                command = new BlankCommand(commandName,
                        (String) CommandParameters.class.getMethod("description").getDefaultValue(),
                        (String) CommandParameters.class.getMethod("usage").getDefaultValue(),
                        Arrays.asList(((String) CommandParameters.class.getMethod("aliases").getDefaultValue()).split(", ")),
                        (SourceType) CommandParameters.class.getMethod("source").getDefaultValue(),
                        (Rank) CommandParameters.class.getMethod("rank").getDefaultValue(),
                        (Class<CommandBase>) clazz);

                if (clazz.getAnnotationsByType(CommandParameters.class).length > 0)
                {
                    CommandParameters params = clazz.getAnnotation(CommandParameters.class);

                    command = new BlankCommand(commandName,
                            params.description(),
                            params.usage(),
                            Arrays.asList(params.aliases().split(", ")),
                            params.source(),
                            params.rank(),
                            (Class<CommandBase>) clazz);
                }

                command.register();
            } catch (NoSuchMethodException e)
            {
                NLog.severe("Could not load command: " + commandName);
                e.printStackTrace();
            }
        });
    }

    private static Reflections cachedReflections = null;

    private static Reflections getReflections(String pack)
    {
        if (cachedReflections == null)
        {
            cachedReflections = new Reflections(pack);
        }
        return cachedReflections;
    }
}
