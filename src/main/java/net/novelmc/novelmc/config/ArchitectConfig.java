package net.novelmc.novelmc.config;

import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.util.NLog;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ArchitectConfig extends YamlConfiguration
{

    private static ArchitectConfig config;
    private NovelMC plugin;
    private File file;

    public ArchitectConfig(NovelMC plugin)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "architect.yml");
        saveDefault();
    }

    public void load()
    {
        try
        {
            super.load(file);
        }
        catch (Exception ex)
        {
            NLog.severe(ex);
        }
    }

    public void save()
    {
        try
        {
            super.save(file);
        }
        catch (Exception ex)
        {
            NLog.severe(ex);
        }
    }

    private void saveDefault()
    {
        plugin.saveResource("architect.yml", false);
    }
}
