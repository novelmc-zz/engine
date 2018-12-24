package net.novelmc.novelmc.command;

import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.rank.Rank;
import net.novelmc.novelmc.util.NLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public abstract class NCommand implements CommandExecutor, TabCompleter
{

    protected static CommandMap cmap;
    protected final String commandName;
    protected final String description;
    protected final String usage;
    protected final List<String> aliases;
    protected final SourceType source;
    protected final Rank rank;

    public NCommand(String commandName, String description, String usage, List<String> aliases, SourceType source, Rank rank)
    {
        this.commandName = commandName;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.source = source;
        this.rank = rank;
    }

    public void register()
    {
        ReflectCommand cmd = new ReflectCommand(commandName);

        if (description != null)
        {
            cmd.setDescription(description);
        }

        if (usage != null)
        {
            cmd.setUsage(usage);
        }

        if (aliases != null)
        {
            cmd.setAliases(aliases);
        }

        if (!getCommandMap().register("", cmd))
        {
            unregisterBukkitCommand(Bukkit.getPluginCommand(cmd.getName()));
            getCommandMap().register("", cmd);
        }

        cmd.setExecutor(this);
    }

    @Override
    public abstract boolean onCommand(CommandSender sender, Command cmd, String string, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args)
    {
        return null;
    }

    private Object getPrivateField(Object object, String field) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

    private void unregisterBukkitCommand(PluginCommand cmd)
    {
        try
        {
            Object result = getPrivateField(NovelMC.plugin.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");

            @SuppressWarnings("unchecked")
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            knownCommands.remove(cmd.getName());

            cmd.getAliases().forEach((registeredalias) ->
            {
                knownCommands.remove(registeredalias);
            });
        }
        catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException ex)
        {
            NLog.severe(ex);
        }
    }

    final CommandMap getCommandMap()
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
            catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex)
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

    public final class ReflectCommand extends Command
    {

        private NCommand cmd = null;

        protected ReflectCommand(String command)
        {
            super(command);
        }

        public void setExecutor(NCommand cmd)
        {
            this.cmd = cmd;
        }

        @Override
        public boolean execute(CommandSender sender, String string, String[] args)
        {
            if (cmd != null)
            {
                if ((sender instanceof Player) && source == SourceType.CONSOLE)
                {
                    sender.sendMessage("You must be on console to execute this command!");
                    return true;
                }

                if (!(sender instanceof Player) && source == SourceType.IN_GAME)
                {
                    sender.sendMessage("You must be in game to execute this command!");
                    return true;
                }

                if (!Rank.getRank(sender).isAtLeast(rank))
                {
                    sender.sendMessage(ChatColor.RED + "You must be at least " + rank.getName() + " to be able to execute this command!");
                    return true;
                }

                if (!cmd.onCommand(sender, this, string, args))
                {
                    sender.sendMessage(usage.replaceAll("<command>", commandName));
                    return false;
                }
            }

            return false;
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args)
        {
            if (cmd != null)
            {
                return cmd.onTabComplete(sender, this, alias, args);
            }

            return null;
        }
    }
}
