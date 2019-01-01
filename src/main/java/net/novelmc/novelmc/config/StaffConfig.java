package net.novelmc.novelmc.config;

import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.util.NLog;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class StaffConfig extends YamlConfiguration
{

    private static StaffConfig config;
    private NovelMC plugin;
    private File file;

    public StaffConfig(NovelMC plugin)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "staff.yml");
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
        plugin.saveResource("staff.yml", false);
    }
}
