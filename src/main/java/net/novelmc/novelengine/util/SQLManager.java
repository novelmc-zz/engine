package net.novelmc.novelengine.util;

import lombok.Getter;
import net.novelmc.novelengine.NovelEngine;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class SQLManager
{

    @Getter
    private static Connection connection;
    private NovelEngine plugin;
    private File file;
    private final String defaultJson = "{\"bans\":{}, \"players\":{}}";

    public SQLManager(NovelEngine plugin)
    {
        this.plugin = plugin;
    }

    public boolean init()
    {
        if(!plugin.config.isSQLEnabled())
        {
            return generateJson(new File(plugin.getDataFolder(), "database.yml"));
        }

        String host = plugin.config.getSQLHost();
        int port = plugin.config.getSQLPort();
        String username = plugin.config.getSQLUsername();
        String password = plugin.config.getSQLPassword();
        String database = plugin.config.getSQLDatabase();

        if (password == null)
        {
            password = ""; // Password can't be null
        }

        try
        {
            connection = connect(host, port, username, password, database);
            generateTables();
            return true;
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex)
        {
            NLog.severe(ex);
            return false;
        }
    }

    private Connection connect(String host, int port, String username, String password, String database) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException
    {
        String url = String.format("jdbc:mysql://%s:%s/%s", host, port, database);
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        return DriverManager.getConnection(url, username, password);
    }

    private void generateTables() throws SQLException
    {
        Connection c = getConnection();
        String bans = "CREATE TABLE IF NOT EXISTS bans ("
                + "name TEXT,"
                + "ip VARCHAR(64),"
                + "`by` TEXT NOT NULL,"
                + "reason TEXT,"
                + "expiry LONG NOT NULL,"
                + "type SET('PERMANENT_NAME', 'PERMANENT_IP', 'IP', 'NORMAL') NOT NULL)";
        c.prepareStatement(bans).executeUpdate();

        String players = "CREATE TABLE IF NOT EXISTS players ("
                + "name TEXT,"
                + "ip VARCHAR(64))";
        c.prepareStatement(players).executeUpdate();
    }

    private boolean generateJson(File file)
    {
        this.file = file;
        if(!file.exists())
        {
            try
            {
                if (!file.getParentFile().exists())
                {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            catch (IOException ignored) {}
        }

        try
        {
            String string = new String(Files.readAllBytes(file.toPath()));
            if(!(string.startsWith("{") && string.endsWith("}")))
            {
                FileUtils.writeStringToFile(file, defaultJson, StandardCharsets.UTF_8);
            }
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public JSONObject getDatabase()
    {
        try
        {
            String string = new String(Files.readAllBytes(file.toPath()));
            return new JSONObject(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return new JSONObject(defaultJson);
    }

    public boolean saveDatabase(JSONObject database)
    {
        try
        {
            FileUtils.writeStringToFile(file, database.toString(), StandardCharsets.UTF_8);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public String getDefaultJson()
    {
        return defaultJson;
    }
}
