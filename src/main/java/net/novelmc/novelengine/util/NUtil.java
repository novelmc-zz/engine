package net.novelmc.novelengine.util;

import net.novelmc.novelengine.rank.Displayable;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.staff.StaffList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NUtil
{

    public static final List<String> DEVELOPERS = Arrays.asList("db8e5b84-c670-41f6-9a2b-1ad9a8c7e104"/*untuned*/, "cb3dab42-0f6f-48dc-8b44-534a482dae95"/*mafrans*/, "c8e5af82-6aba-4dd7-83e8-474381380cc9"/*Paldiu*/);
    public static final List<String> ADMINS = Arrays.asList("0bc1c464-7c4c-4b20-9c17-a1079145de8d"/*rovertpug*/, "61474d4b-a596-4b67-a91f-eb00c07bbbb5"/*breedme*/, "b58caae2-254a-47db-a129-e8ebce4e6206"/*irix*/, "a8ab4eb2-2805-4631-8351-f3be58056e37"/*pih*/);
    public static final List<String> DIRECTORS = Arrays.asList("7c2eee6f-33a2-44de-8dd1-1e3fe06d2150"/*falceso*/, "6ea8bbd1-2496-4389-a12f-a3e1fc74372c"/*synpatn*/, "a429d3d7-93fa-47f0-8692-f2567b1293b0"/*shrimpuu*/);
    /*
    *   Placed certain developers in appropriate tiers for order priority. 
     */

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
                Bukkit.getOnlinePlayers().stream().map((player) ->
                {
                    player.sendMessage(message);
                    return player;
                }).forEachOrdered((_item) ->
                {
                    Bukkit.getConsoleSender().sendMessage(NUtil.colorize(message));
                });
                break;

            case STAFF_ONLY:
                Bukkit.getOnlinePlayers().stream().filter((player) -> (StaffList.isStaff(player))).map((player) ->
                {
                    player.sendMessage(message);
                    return player;
                }).forEachOrdered((_item) ->
                {
                    Bukkit.getConsoleSender().sendMessage(NUtil.colorize(message));
                });
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
            globalMessage(colorize(String.format("&8<-> &4&lSTAFF&r&8 » &7%s &8(%s%s &7%s&8)&7", action, display.getColor(), display.getTag(), sender.getName())), MessageType.STAFF_ONLY);
        } else
        {
            globalMessage(colorize(String.format("&8<-> &9&lSERVER&r&8 » &7%s &8(%s%s &7%s&8)&7", action, display.getColor(), display.getTag(), sender.getName())));
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
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
            {
                NLog.severe(ex);
            }
        }

        return cachedCommandMap;
    }
}
