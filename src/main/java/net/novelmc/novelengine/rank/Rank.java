package net.novelmc.novelengine.rank;

import lombok.Getter;
import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.rank.architect.ArchitectList;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Rank implements Displayable
{

    IMPOSTOR("an", "Impostor", "IMP", ChatColor.WHITE),
    NON_OP("a", "Non-Op", "", ChatColor.WHITE),
    OP("a", "Member", "MBR", ChatColor.WHITE),
    TRAINEE("a", "Trainee", "TRN", ChatColor.DARK_AQUA),
    MOD("a", "Mod", "MOD", ChatColor.GOLD),
    SENIOR_MOD("a", "Senior Mod", "SRM", ChatColor.GOLD),
    ADMIN("an", "Admin", "ADM", ChatColor.BLUE),
    DIRECTOR("a", "Director", "DIR", ChatColor.RED),
    CONSOLE("the", "Console", "CSL", ChatColor.DARK_RED);

    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final ChatColor color;

    Rank(String determiner, String name, String tag, ChatColor color)
    {
        this.determiner = determiner;
        this.name = name;
        this.color = color;
        this.tag = color + "" + ChatColor.BOLD + tag + ChatColor.RESET + color;
    }

    private static NovelEngine plugin;

    public int getLevel()
    {
        return ordinal();
    }

    public boolean isAtLeast(Rank rank)
    {
        return getLevel() >= rank.getLevel();
    }

    public String getLoginMessage()
    {
        return determiner + " " + color + name;
    }

    public static Rank findRank(String string)
    {
        try
        {
            return Rank.valueOf(string.toUpperCase());
        }
        catch (Exception ex)
        {
            NLog.severe("It appears one of your users in staff.yml has an invalid rank.");
            NLog.severe(ex);

        }
        return null;
    }

    public static Rank getRank(Player player)
    {
        if (StaffList.isImpostor(player) || ArchitectList.isImpostor(player))
        {
            return Rank.IMPOSTOR;
        }

        if (StaffList.isStaff(player))
        {
            return StaffList.getStaff(player).getRank();
        }

        return player.isOp() ? Rank.OP : Rank.NON_OP;
    }

    public static Rank getRank(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getRank((Player) sender);
        }
        return Rank.CONSOLE;
    }

    public static Displayable getDisplay(Player player)
    {
        if (NUtil.DEVELOPERS.contains(player.getName()))
        {
            return Title.DEVELOPER;
        }

        if (ArchitectList.isArchitect(player))
        {
            return Title.ARCHITECT;
        }

        return getRank(player);
    }

    public static Displayable getDisplay(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return Rank.CONSOLE;
        }

        return getDisplay((Player) sender);
    }
}
