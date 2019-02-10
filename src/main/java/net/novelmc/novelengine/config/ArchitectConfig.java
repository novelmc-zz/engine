package net.novelmc.novelengine.config;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.util.NLog;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ArchitectConfig extends YamlConfiguration
{

    private static ArchitectConfig config;
    private NovelEngine plugin;
    private File file;

    public ArchitectConfig(NovelEngine plugin)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "architect.yml");

        if ( ! file.exists())
        {
            saveDefault();
        }
    }

    public void load()
    {
        try
        {
            super.load(file);
        } catch (Exception ex)
        {
            NLog.severe(ex);
        }
    }

    public void save()
    {
        try
        {
            super.save(file);
        } catch (Exception ex)
        {
            NLog.severe(ex);
        }
    }

    private void saveDefault()
    {
        plugin.saveResource("architect.yml", false);
    }
}
