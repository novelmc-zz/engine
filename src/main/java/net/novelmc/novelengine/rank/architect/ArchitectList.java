package net.novelmc.novelengine.rank.architect;

import lombok.Getter;
import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.util.NLog;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArchitectList
{

    @Getter
    private static List<Architect> architects;
    @Getter
    private static List<String> impostors;
    private static NovelEngine plugin;

    public ArchitectList(NovelEngine plugin)
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

        for (String key : plugin.ac.getKeys(false))
        {
            Architect a = new Architect(key);
            a.load(plugin.ac.getConfigurationSection(key));
            architects.add(a);
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
        a.save(plugin.ac.createSection(a.getConfigKey()));
    }

    public static void addArchitect(Player player)
    {
        Architect a = new Architect(player.getName().toLowerCase());
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
        a.save(plugin.ac.getConfigurationSection(a.getConfigKey()));
    }

    public static void updateIp(Player player)
    {
        Architect a = getArchitect(player);

        if (a == null)
        {
            return;
        }

        architects.remove(a);
        a.getIps().add(player.getAddress().getHostString());
        architects.add(a);
        a.save(plugin.ac.getConfigurationSection(a.getConfigKey()));
    }

    public static boolean isImpostor(Player player)
    {
        return impostors.contains(player.getName());
    }
}
