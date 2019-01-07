package net.novelmc.novelengine.config;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.util.NLog;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config extends YamlConfiguration
{

    private static Config config;
    private NovelEngine plugin;
    private File file;

    public Config(NovelEngine plugin)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists())
        {
            saveDefault();
        }
    }

    public String getServerName()
    {
        return super.getString("general.name");
    }

    public String getMOTD()
    {
        return super.getString("general.motd");
    }

    public String getWebsite()
    {
        return super.getString("general.website");
    }

    public boolean isEventModeEnabled()
    {
        return super.getBoolean("general.eventmode");
    }

    public void setEventModeEnabled(boolean enable)
    {
        super.set("general.eventmode", enable);
    }

    public List<String> getDefaultCommands()
    {
        return super.getStringList("commands.default");
    }

    public List<String> getStaffCommands()
    {
        return super.getStringList("commands.staff");
    }

    public String getSQLUsername()
    {
        return super.getString("sql.username");
    }

    public String getSQLPassword()
    {
        return super.getString("sql.password");
    }

    public String getSQLHost()
    {
        return super.getString("sql.host");
    }

    public int getSQLPort()
    {
        return super.getInt("sql.port");
    }

    public String getSQLDatabase()
    {
        return super.getString("sql.database");
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
        plugin.saveResource("config.yml", false);
    }
}
