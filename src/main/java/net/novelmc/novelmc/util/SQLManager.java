package net.novelmc.novelmc.util;

import lombok.Getter;
import net.novelmc.novelmc.NovelMC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLManager
{

    @Getter
    private static Connection connection;
    private NovelMC plugin;

    public SQLManager(NovelMC plugin)
    {
        this.plugin = plugin;
    }

    public boolean init()
    {
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
    }
}
