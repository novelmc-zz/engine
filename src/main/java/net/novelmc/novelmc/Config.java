package net.novelmc.novelmc;

import net.novelmc.novelmc.util.NLog;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config extends YamlConfiguration
{

    private static Config config;
    private NovelMC plugin;
    private File file;

    public Config(NovelMC plugin)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");
        saveDefault();
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

    public List<String> getDefaultCommands()
    {
        return super.getStringList("commands.default");
    }

    public List<String> getAdminCommands()
    {
        return super.getStringList("commands.admin");
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
