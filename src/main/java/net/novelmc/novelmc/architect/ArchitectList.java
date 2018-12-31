package net.novelmc.novelmc.architect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.util.NLog;
import net.novelmc.novelmc.util.NUtil;
import org.bukkit.entity.Player;

public class ArchitectList 
{

    @Getter
    private static List<Architect> architects;
    @Getter
    private static List<String> impostors;
    private NovelMC plugin;

    public ArchitectList(NovelMC plugin)
    {
        architects = new ArrayList<>();
        architects.clear();
        impostors = new ArrayList<>();
        impostors.clear();
        this.plugin = plugin;
        loadArchitects();
    }

    public void loadArchitects()
    {
        architects.clear();
        Connection c = plugin.sql.getConnection();
        try
        {
            ResultSet result = c.prepareStatement("SELECT * FROM architect").executeQuery();
            while (result.next())
            {
                Architect a = new Architect();
                a.setName(result.getString("name"));
                a.setIps(NUtil.deserializeArray(result.getString("ips")));
                architects.add(a);
            }
        }
        catch (SQLException ex)
        {
            NLog.severe(ex);
        }

        NLog.info("Successfully loaded " + architects.size() + " architects!");
    }

    public static boolean isArchitect(Architect a)
    {
        String name = a.getName();
        for (Architect check : architects)
        {
            return check.getName().equals(name);
        }
        return false;
    }

    public static boolean isArchitect(Player player)
    {
        return getArchitect(player) != null;
    }

    public static Architect getArchitect(Player player)
    {
        for (Architect a : architects)
        {
            if (a.getName().equals(player.getName()))
            {
                return a;
            }
        }
        return null;
    }

    public static void addArchitect(Architect a)
    {
        if (isArchitect(a))
        {
            return;
        }

        architects.add(a);
        a.save();
    }

    public static void addArchitect(Player player)
    {
        Architect a = new Architect();
        a.setName(player.getName());
        a.setIps(Collections.singletonList(player.getAddress().getHostString()));
        addArchitect(a);
    }

    public static void removeArchitect(Architect a)
    {
        if (!isArchitect(a))
        {
            return;
        }

        architects.remove(a);
        a.delete();
    }

    public static void addIp(Player player, String ip)
    {
        if (!isArchitect(player))
        {
            return;
        }

        Architect a = getArchitect(player);
        architects.remove(a);
        List<String> ips = a.getIps();
        ips.add(ip);
        architects.add(a);
        a.update();
    }

    public static boolean isImpostor(Player player)
    {
        return impostors.contains(player.getName());
    }
}
