package net.novelmc.novelmc.architect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.novelmc.novelmc.NovelMC;
import net.novelmc.novelmc.util.NLog;
import net.novelmc.novelmc.util.NUtil;
import org.apache.commons.lang.StringUtils;

public class Architect 
{
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> ips;

    public void save()
    {
        Connection c = NovelMC.plugin.sql.getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("INSERT INTO architect (name, ips) VALUES (?, ?)");
            statement.setString(1, name);
            statement.setString(2, NUtil.serializeArray(ips));
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
            PreparedStatement statement = c.prepareStatement("DELETE FROM architect WHERE name = ?");
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
            PreparedStatement newip = c.prepareStatement("UPDATE architect SET ips = ?  WHERE name = ?");
            newip.setString(1, NUtil.serializeArray(ips));
            newip.setString(2, name);
            newip.executeUpdate();
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
                .append(" - IPs: ").append(StringUtils.join(ips, ", ")).append("\n");
        return sb.toString();
    }
}