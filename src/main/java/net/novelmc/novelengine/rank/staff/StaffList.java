package net.novelmc.novelengine.rank.staff;

import lombok.Getter;
import static net.novelmc.novelengine.banning.BanManager.retUUID;
import net.novelmc.novelengine.config.StaffConfig;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.NovelBase;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StaffList extends NovelBase
{

    public static StaffConfig staffConfig;
    @Getter
    private static List<Staff> staff;
    @Getter
    private static List<String> impostors;

    public StaffList()
    {
        staff = new ArrayList<>();
        staff.clear();
        impostors = new ArrayList<>();
        impostors.clear();
        loadStaff();
    }

    public void loadStaff()
    {
        staff.clear();

        plugin.staffConfig.getKeys(false).stream().map((key) ->
        {
            Staff s = new Staff(key);
            s.load(plugin.staffConfig.getConfigurationSection(key));
            return s;
        }).forEachOrdered((s) ->
        {
            staff.add(s);
        });

        NLog.info("Successfully loaded " + staff.size() + " staff!");
    }

    public static boolean isStaff(Staff s)
    {
        String name = s.getUuid();
        for (Staff check : staff)
        {
            return check.getUuid().equals(retUUID(name));
        }
        return false;
    }

    public static boolean isStaff(Player player)
    {
        return getStaff(player) != null && getStaff(player).isActive();
    }

    public static Staff getStaff(Player player)
    {
        for (Staff s : staff)
        {
            if (s.getUuid().equals(retUUID(player.getName())))
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
        s.setActive(true);
        s.save(plugin.staffConfig.createSection(s.getConfigKey()));
    }

    public static void setUsername(Player player)
    {
        getStaff(player).setUsername(player.getName());
    }

    public static void addStaff(Player player)
    {
        Staff s = new Staff(player.getName().toLowerCase());
        s.setUuid(retUUID(player.getName()));
        s.setIps(Collections.singletonList(player.getAddress().getHostString()));
        addStaff(s);
        s.setActive(true);
    }

    public static void removeStaff(Staff s)
    {
        if ( ! isStaff(s))
        {
            return;
        }

        staff.remove(s);
        s.setActive(false);
        s.save(plugin.staffConfig.getConfigurationSection(s.getConfigKey()));
    }

    public static void updateRank(Player player, Rank rank)
    {
        Staff s = getStaff(player);

        if (s == null)
        {
            return;
        }

        s.setRank(rank);
        s.save(plugin.staffConfig.getConfigurationSection(s.getConfigKey()));
    }

    // This is never used?
    public static void updateActive(Player player, boolean active)
    {
        Staff s = getStaff(player);

        if (s == null)
        {
            return;
        }

        s.setActive(active);
        s.save(plugin.staffConfig.getConfigurationSection(s.getConfigKey()));
    }

    public static void updateIp(Player player)
    {
        Staff s = getStaff(player);

        if (s == null)
        {
            return;
        }

        staff.remove(s);
        s.getIps().add(player.getAddress().getHostString());
        staff.add(s);
        s.save(plugin.staffConfig.getConfigurationSection(s.getConfigKey()));
    }

    public static boolean isImpostor(Player player)
    {
        return impostors.contains(player.getName());
    }

    public static void updateHomeIp(Player player)
    {
        Staff s = getStaff(player);

        if (s == null)
        {
            return;
        }

        staff.remove(s);
        s.setHomeIp(player.getAddress().getHostString());
        staff.add(s);
        s.save(plugin.staffConfig.getConfigurationSection(s.getConfigKey()));
    }

    public static void clearHomeIp(Player player)
    {
        Staff s = getStaff(player);

        if (s == null)
        {
            return;
        }

        staff.remove(s);
        s.setHomeIp(null);
        staff.add(s);
        s.save(plugin.staffConfig.getConfigurationSection(s.getConfigKey()));
    }

    public static void updateCommandSpy(Player player, boolean commandSpy)
    {
        Staff s = getStaff(player);

        if (s == null)
        {
            return;
        }

        staff.remove(s);
        s.setCommandSpy(commandSpy);
        staff.add(s);
        s.save(plugin.staffConfig.getConfigurationSection(s.getConfigKey()));
    }
}
