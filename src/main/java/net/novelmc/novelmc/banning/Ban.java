package net.novelmc.novelmc.banning;

import lombok.Getter;
import lombok.Setter;
import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.util.NLog;
import net.novelmc.novelmc.util.NUtil;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Ban
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
                    + "Your IP address is currently permanently banned from this server.\n"
                    + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "Reason not specified") + "\n"
                    + ChatColor.RED + "Banned by: " + ChatColor.YELLOW + by;
        }
        if (type == BanType.PERMANENT_NAME)
        {
            return ChatColor.RED
                    + "Your name is currently permanently banned from this server.\n"
                    + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "Reason not specified") + "\n"
                    + ChatColor.RED + "Banned by: " + ChatColor.YELLOW + by;
        }
        if (type == BanType.IP)
        {
            return ChatColor.RED
                    + "Your IP address is currently banned from this server.\n"
                    + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "Reason not specified") + "\n"
                    + ChatColor.RED + "Banned by: " + ChatColor.YELLOW + by + "\n"
                    + ChatColor.RED + "Your ban will expire on "
                    + ChatColor.YELLOW + NUtil.dateToString(expiry);
        }

        // Normal ban
        return ChatColor.RED
                + "You're currently banned from this server.\n"
                + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "Reason not specified") + "\n"
                + ChatColor.RED + "Banned by: " + ChatColor.YELLOW + by + "\n"
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
        Connection c = NovelMC.plugin.sql.getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("INSERT INTO bans (name, ip, `by`, reason, expiry, type) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, ip);
            statement.setString(3, by);
            statement.setString(4, reason);
            statement.setLong(5, NUtil.getUnixTime(expiry));
            statement.setString(6, type.toString());
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            NLog.severe(ex);
        }
    }

    public void delete()
    {
        Connection c = NovelMC.plugin.sql.getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("DELETE FROM bans WHERE name = ? OR ip = ?");
            statement.setString(1, name);
            statement.setString(2, ip);
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            NLog.severe(ex);
        }
    }
}