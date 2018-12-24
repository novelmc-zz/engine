package net.novelmc.novelmc.staff;

import lombok.Getter;
import lombok.Setter;
import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.rank.Rank;
import net.novelmc.novelmc.util.NLog;
import net.novelmc.novelmc.util.NUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Staff
{

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> ips;
    @Getter
    @Setter
    private Rank rank = Rank.TRAINEE;
    @Getter
    @Setter
    private boolean active = true;

    public void save()
    {
        Connection c = NovelMC.plugin.sql.getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("INSERT INTO staff (name, ips, rank, active) VALUES (?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, NUtil.serializeArray(ips));
            statement.setString(3, rank.name());
            statement.setBoolean(4, active);
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
            PreparedStatement statement = c.prepareStatement("DELETE FROM staff WHERE name = ?");
            statement.setString(1, name);
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            NLog.severe(ex);
        }
    }

    public void update()
    {
        Connection c = NovelMC.plugin.sql.getConnection();
        try
        {
            PreparedStatement newip = c.prepareStatement("UPDATE staff SET ips = ?  WHERE name = ?");
            newip.setString(1, NUtil.serializeArray(ips));
            newip.setString(2, name);
            PreparedStatement newrank = c.prepareStatement("UPDATE staff SET rank = ? WHERE name = ?");
            newrank.setString(1, rank.name());
            newrank.setString(2, name);
            PreparedStatement newactive = c.prepareStatement("UPDATE staff SET active = ? WHERE name = ?");
            newactive.setBoolean(1, active);
            newactive.setString(2, name);
            newip.executeUpdate();
            newrank.executeUpdate();
            newactive.executeUpdate();
        }
        catch (SQLException ex)
        {
            NLog.severe(ex);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":\n")
                .append(" - IPs: ").append(StringUtils.join(ips, ", ")).append("\n")
                .append(" - Rank: ").append(rank.name()).append("\n")
                .append(" - Active: ").append(active);
        return sb.toString();
    }
}
