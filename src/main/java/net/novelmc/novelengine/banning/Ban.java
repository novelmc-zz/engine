package net.novelmc.novelengine.banning;

import lombok.Getter;
import lombok.Setter;
import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.NUtil;
import net.novelmc.novelengine.util.NovelBase;
import net.novelmc.novelengine.util.SQLManager;
import org.bukkit.ChatColor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Ban extends NovelBase
{

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String ip;
    @Getter
    @Setter
    private String by;
    @Getter
    @Setter
    private String reason;
    @Getter
    @Setter
    private Date expiry;
    @Getter
    @Setter
    private BanType type = BanType.NORMAL;

    public String getKickMessage()
    {
        if (type == BanType.PERMANENT_IP)
        {
            return ChatColor.RED
                    + "Your IP address is currently permanently banned from this server." + NEW_LINE
                    + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "Reason not specified") + NEW_LINE
                    + ChatColor.RED + "Banned by: " + ChatColor.YELLOW + by;
        }
        if (type == BanType.PERMANENT_NAME)
        {
            return ChatColor.RED
                    + "Your name is currently permanently banned from this server." + NEW_LINE
                    + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "Reason not specified") + NEW_LINE
                    + ChatColor.RED + "Banned by: " + ChatColor.YELLOW + by;
        }
        if (type == BanType.IP)
        {
            return ChatColor.RED
                    + "Your IP address is currently banned from this server." + NEW_LINE
                    + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "Reason not specified") + NEW_LINE
                    + ChatColor.RED + "Your ban will expire on "
                    + ChatColor.YELLOW + NUtil.dateToString(expiry);
        }

        // Normal ban
        return ChatColor.RED
                + "You're currently banned from this server." + NEW_LINE
                + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "Reason not specified") + NEW_LINE
                + ChatColor.RED + "Banned by: " + ChatColor.YELLOW + by + NEW_LINE
                + ChatColor.RED + "Your ban will expire on "
                + ChatColor.YELLOW + NUtil.dateToString(expiry);
    }

    public boolean isExpired()
    {
        if (expiry == null)
        {
            return false;
        }

        return expiry.after(new Date());
    }

    public void save()
    {
        if(config.isSQLEnabled())
        {
            Connection c = SQLManager.getConnection();
            try {
                PreparedStatement statement = c.prepareStatement("INSERT INTO bans (name, ip, `by`, reason, expiry, type) VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, name);
                statement.setString(2, ip);
                statement.setString(3, by);
                statement.setString(4, reason);
                statement.setLong(5, NUtil.getUnixTime(expiry));
                statement.setString(6, type.toString());
                statement.executeUpdate();
            } catch (SQLException ex) {
                NLog.severe(ex);
            }
        }
        else
            {
            JSONObject database = plugin.sqlManager.getDatabase();
            JSONObject bans = database.getJSONObject("bans");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("ip", ip);
            jsonObject.put("by", by);
            jsonObject.put("reason", reason);
            jsonObject.put("expiry", expiry);
            jsonObject.put("type", type);

            bans.put(name, jsonObject);
            database.put("bans", bans);
            plugin.sqlManager.saveDatabase(database);
        }
    }

    public void delete()
    {
        if(config.isSQLEnabled())
        {
            Connection c = SQLManager.getConnection();
            try {
                PreparedStatement statement = c.prepareStatement("DELETE FROM bans WHERE name = ? OR ip = ?");
                statement.setString(1, name);
                statement.setString(2, ip);
                statement.executeUpdate();
            } catch (SQLException ex) {
                NLog.severe(ex);
            }
        }
        else
            {
            JSONObject database = plugin.sqlManager.getDatabase();
            JSONObject bans = database.getJSONObject("bans");
            bans.remove(name);
            database.put("bans", bans);

            plugin.sqlManager.saveDatabase(database);
        }
    }
}