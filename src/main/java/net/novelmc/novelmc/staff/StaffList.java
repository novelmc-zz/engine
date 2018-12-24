package net.novelmc.novelmc.staff;

import lombok.Getter;
import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.rank.Rank;
import net.novelmc.novelmc.util.NLog;
import net.novelmc.novelmc.util.NUtil;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaffList
{

    @Getter
    private static List<Staff> staff;
    @Getter
    private static List<String> impostors;
    private NovelMC plugin;

    public StaffList(NovelMC plugin)
    {
        staff = new ArrayList<>();
        staff.clear();
        impostors = new ArrayList<>();
        impostors.clear();
        this.plugin = plugin;
        loadStaff();
    }

    public void loadStaff()
    {
        staff.clear();
        Connection c = plugin.sql.getConnection();
        try
        {
            ResultSet result = c.prepareStatement("SELECT * FROM staff").executeQuery();
            while (result.next())
            {
                Staff s = new Staff();
                s.setName(result.getString("name"));
                s.setIps(NUtil.deserializeArray(result.getString("ips")));
                s.setRank(Rank.findRank(result.getString("rank")));
                s.setActive(result.getBoolean("active"));
                staff.add(s);
            }
        }
        catch (SQLException ex)
        {
            NLog.severe(ex);
        }

        NLog.info("Successfully loaded " + staff.size() + " staff!");
    }

    public static boolean isStaff(Staff s)
    {
        String name = s.getName();
        for (Staff check : staff)
        {
            return check.getName().equals(name);
        }
        return false;
    }

    public static boolean isStaff(Player player)
    {
        return getStaff(player) != null;
    }

    public static Staff getStaff(Player player)
    {
        for (Staff s : staff)
        {
            if (s.getName().equals(player.getName()))
            {
                return s;
            }
        }
        return null;
    }

    public static void addStaff(Staff s)
    {
        if (isStaff(s))
        {
            return;
        }

        staff.add(s);
        s.save();
    }

    public static void addStaff(Player player)
    {
        Staff s = new Staff();
        s.setName(player.getName());
        s.setIps(Collections.singletonList(player.getAddress().getHostString()));
        addStaff(s);
    }

    public static void removeStaff(Staff s)
    {
        if (!isStaff(s))
        {
            return;
        }

        staff.remove(s);
        s.delete();
    }

    public static void updateRank(Player player, Rank rank)
    {
        if (!isStaff(player))
        {
            return;
        }

        if (rank.isAtLeast(Rank.TRAINEE))
        {
            return;
        }

        Staff s = getStaff(player);
        staff.remove(s);
        s.setRank(rank);
        staff.add(s);
        s.update();
    }

    public static void addIp(Player player, String ip)
    {
        if (!isStaff(player))
        {
            return;
        }

        Staff s = getStaff(player);
        staff.remove(s);
        List<String> ips = s.getIps();
        ips.add(ip);
        staff.add(s);
        s.update();
    }

    public static void updateActive(Player player, boolean active)
    {
        if (!isStaff(player))
        {
            return;
        }

        Staff s = getStaff(player);
        staff.remove(s);
        s.setActive(active);
        staff.add(s);
        s.update();
    }

    public static boolean isImpostor(Player player)
    {
        return impostors.contains(player.getName());
    }
}
