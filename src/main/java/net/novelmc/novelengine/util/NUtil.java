package net.novelmc.novelengine.util;

import net.novelmc.novelengine.rank.Displayable;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.staff.StaffList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NUtil
{

    public static final List<String> DEVELOPERS = Arrays.asList("_Fleek", "Super_", "untuned", "irix", "Mafrans");
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    public enum MessageType
    {
        ALL,
        STAFF_ONLY
    }

    public static void globalMessage(String message, MessageType messageType)
    {
        switch (messageType)
        {
            case ALL:
                for(Player player : Bukkit.getOnlinePlayers())
                {
                    player.sendMessage(message);
                }
                break;

            case STAFF_ONLY:
                for(Player player : Bukkit.getOnlinePlayers())
                {
                    if(StaffList.isStaff(player))
                    {
                        player.sendMessage(message);
                    }
                }
                break;
        }
    }

    public static void globalMessage(String message)
    {
        globalMessage(message, MessageType.ALL);
    }

    public static void playerAction(CommandSender sender, String action, boolean staffOnly)
    {
        Displayable display = Rank.getDisplay(sender);
        if (staffOnly)
        {
            globalMessage(colorize(String.format("&8<-> &4&lSTAFF&r&8: &7%s &8(%s%s &7%s&8)&7", action, display.getColor(), display.getTag(), sender.getName())), MessageType.STAFF_ONLY);
        }
        else
        {
            globalMessage(colorize(String.format("&8<-> &a&lSERVER&r&8: &7%s &8(%s%s &7%s&8)&7", action, display.getColor(), display.getTag(), sender.getName())));
        }
    }

    public static String colorize(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    // Credits: Start (TotalFreedom (Can't be bothered writing these))
    public static Date parseDateOffset(String time)
    {
        Pattern timePattern = Pattern.compile(
                "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
                        + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find())
        {
            if (m.group() == null || m.group().isEmpty())
            {
                continue;
            }
            for (int i = 0; i < m.groupCount(); i++)
            {
                if (m.group(i) != null && !m.group(i).isEmpty())
                {
                    found = true;
                    break;
                }
            }
            if (found)
            {
                if (m.group(1) != null && !m.group(1).isEmpty())
                {
                    years = Integer.parseInt(m.group(1));
                }
                if (m.group(2) != null && !m.group(2).isEmpty())
                {
                    months = Integer.parseInt(m.group(2));
                }
                if (m.group(3) != null && !m.group(3).isEmpty())
                {
                    weeks = Integer.parseInt(m.group(3));
                }
                if (m.group(4) != null && !m.group(4).isEmpty())
                {
                    days = Integer.parseInt(m.group(4));
                }
                if (m.group(5) != null && !m.group(5).isEmpty())
                {
                    hours = Integer.parseInt(m.group(5));
                }
                if (m.group(6) != null && !m.group(6).isEmpty())
                {
                    minutes = Integer.parseInt(m.group(6));
                }
                if (m.group(7) != null && !m.group(7).isEmpty())
                {
                    seconds = Integer.parseInt(m.group(7));
                }
                break;
            }
        }
        if (!found)
        {
            return null;
        }

        Calendar c = new GregorianCalendar();

        if (years > 0)
        {
            c.add(Calendar.YEAR, years);
        }
        if (months > 0)
        {
            c.add(Calendar.MONTH, months);
        }
        if (weeks > 0)
        {
            c.add(Calendar.WEEK_OF_YEAR, weeks);
        }
        if (days > 0)
        {
            c.add(Calendar.DAY_OF_MONTH, days);
        }
        if (hours > 0)
        {
            c.add(Calendar.HOUR_OF_DAY, hours);
        }
        if (minutes > 0)
        {
            c.add(Calendar.MINUTE, minutes);
        }
        if (seconds > 0)
        {
            c.add(Calendar.SECOND, seconds);
        }

        return c.getTime();
    }
    // Credits: End


    private static CommandMap cachedCommandMap = null;

    public static CommandMap getCommandMap()
    {
        if (cachedCommandMap == null)
        {
            try
            {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cachedCommandMap = (CommandMap) f.get(Bukkit.getServer());
            }
            catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
            {
                NLog.severe(ex);
            }
        }

        return cachedCommandMap;
    }
}
