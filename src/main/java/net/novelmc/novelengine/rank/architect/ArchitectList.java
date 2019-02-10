package net.novelmc.novelengine.rank.architect;

import lombok.Getter;
import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.NovelBase;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ArchitectList extends NovelBase
{

    @Getter
    private static List<Architect> architects;
    @Getter
    private static List<String> impostors;

    public ArchitectList()
    {
        architects = new ArrayList<>();
        architects.clear();
        impostors = new ArrayList<>();
        impostors.clear();
        loadArchitects();
    }

    public void loadArchitects()
    {
        architects.clear();

        plugin.architectConfig.getKeys(false).stream().map((key) ->
        {
            Architect a = new Architect(key);
            a.load(plugin.architectConfig.getConfigurationSection(key));
            return a;
        }).forEachOrdered((a) ->
        {
            architects.add(a);
        });

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
        a.save(plugin.architectConfig.createSection(a.getConfigKey()));
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
        if ( ! isArchitect(a))
        {
            return;
        }

        architects.remove(a);
        a.save(plugin.architectConfig.getConfigurationSection(a.getConfigKey()));
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
        a.save(plugin.architectConfig.getConfigurationSection(a.getConfigKey()));
    }

    public static boolean isImpostor(Player player)
    {
        return impostors.contains(player.getName());
    }
}
