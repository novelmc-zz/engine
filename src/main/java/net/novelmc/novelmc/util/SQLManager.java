package net.novelmc.novelmc.util;

import lombok.Getter;
import net.novelmc.novelmc.NovelMC;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        String staff = "CREATE TABLE IF NOT EXISTS staff ("
                + "name VARCHAR(64) PRIMARY KEY,"
                + "ips VARCHAR(255) NOT NULL,"
                + "rank VARCHAR(64) NOT NULL,"
                + "active BOOLEAN NOT NULL)";
        String bans = "CREATE TABLE IF NOT EXISTS bans ("
                + "name TEXT,"
                + "ip VARCHAR(64),"
                + "`by` TEXT NOT NULL"
                + "reason TEXT,"
                + "expiry LONG NOT NULL"
                + "type SET('PERMANENT_NAME', 'PERMANENT_IP', 'IP', 'NORMAL') NOT NULL)";
        String players = "CREATE TABLE IF NOT EXISTS players ("
                + "name VARCHAR(64) PRIMARY KEY,"
                + "ip VARCHAR(255) NOT NULL," // Most recent ip
                + "ips VARCHAR(255) NOT NULL,"
                + "architect BOOLEAN NOT NULL)";

        c.prepareStatement(staff).executeUpdate();
        c.prepareStatement(bans).executeUpdate();
        c.prepareStatement(players).executeUpdate();
    }

    public static boolean playerExists(Player player)
    {
        Connection c = getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("SELECT * FROM players WHERE name = ?");
            statement.setString(1, player.getName());
            ResultSet result = statement.executeQuery();

            if (result.next())
            {
                return result.getString("name") != null;
            }
        }
        catch (SQLException ex)
        {
            NLog.severe(ex);
        }

        return false;
    }

    public static void updatePlayer(Player player)
    {
        Connection c = getConnection();
        try
        {
            List<String> ips = new ArrayList<>();
            ResultSet result = c.prepareStatement("SELECT ips FROM players WHERE name = " + player.getName()).executeQuery();
            if (result.next())
            {
                ips = NUtil.deserializeArray(result.getString("ips"));
            }

            PreparedStatement statement = c.prepareStatement("UPDATE players SET ip = ?, ips = ? WHERE name = ?");
            statement.setString(1, player.getAddress().getHostString());
            statement.setString(2, NUtil.serializeArray(ips));
            statement.setString(3, player.getName());
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            NLog.severe(ex);
        }
    }

    public static void generateNewPlayer(Player player)
    {
        Connection c = getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("INSERT INTO players (name, ip, ips) VALUES (?, ?, ?)");
            statement.setString(1, player.getName());
            statement.setString(2, player.getAddress().getHostString());
            List<String> ips = Collections.singletonList(player.getAddress().getHostString());
            statement.setString(3, NUtil.serializeArray(ips));
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            NLog.severe(ex);
        }
    }
}
