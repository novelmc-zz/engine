package net.novelmc.novelengine.util;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PlayerDatabase extends NovelBase
{

    private final List<String[]> players;

    public PlayerDatabase()
    {
        players = new ArrayList<>();
    }

    public void purge()
    {
        players.clear();

        if ( ! players.isEmpty())
        {
            players.stream().forEach((empty) ->
            {
                players.remove(empty);
            });
        } else
        {
            NLog.info("PlayerDatabase is already empty.");
        }

        NLog.info("PlayerDatabase purged. Current count: " + players.size());
    }

    public void load()
    {
        players.clear();

        if (config.isSQLEnabled())
        {
            Connection c = SQLManager.getConnection();

            try
            {
                ResultSet result = c.prepareStatement("SELECT * FROM players").executeQuery();
                while (result.next())
                {
                    add(result.getString("name"), result.getString("ip"));
                }
            } catch (SQLException ex)
            {
                NLog.severe(ex);
                return;
            }
        } else
        {
            JSONObject playerJson = plugin.sqlManager.getDatabase().getJSONObject("players");
            playerJson.keySet().stream().map((key) -> playerJson.getJSONObject(key)).forEachOrdered((obj) ->
            {
                add(obj.getString("name"), obj.getString("ip"));
            });
        }

        NLog.info("Successfully loaded " + players.size() + " cached players!");
    }

    public boolean containsName(String name)
    {
        return players.stream().anyMatch((data) -> (data[0].equalsIgnoreCase(name)));
    }

    public boolean containsIp(String ip)
    {
        return players.stream().anyMatch((data) -> (data[1].equalsIgnoreCase(ip)));
    }

    private String[] getDataByName(String name)
    {
        for (String[] data : players)
        {
            if (data[0].equalsIgnoreCase(name))
            {
                return data;
            }
        }
        return new String[]
        {
            null, null
        };
    }

    private String[] getDataByIp(String ip)
    {
        for (String[] data : players)
        {
            if (data[1].equalsIgnoreCase(ip))
            {
                return data;
            }
        }
        return new String[]
        {
            null, null
        };
    }

    public String getIp(String name)
    {
        return getDataByName(name)[1];
    }

    public void add(String name, String ip)
    {
        if (containsName(name))
        {
            players.remove(getDataByName(name));
        }
        players.add(new String[]
        {
            name, ip
        });
    }

    public String getName(String ip)
    {
        return getDataByName(ip)[0];
    }
}
