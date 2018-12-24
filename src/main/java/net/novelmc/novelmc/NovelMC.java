package net.novelmc.novelmc;

import net.novelmc.novelmc.banning.BanManager;
import net.novelmc.novelmc.command.CommandLoader;
import net.novelmc.novelmc.listener.PlayerListener;
import net.novelmc.novelmc.listener.ServerListener;
import net.novelmc.novelmc.staff.StaffList;
import net.novelmc.novelmc.util.NLog;
import net.novelmc.novelmc.util.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class NovelMC extends JavaPlugin
{

    public static NovelMC plugin;
    public BanManager bm;
    public CommandLoader cl;
    public Config config;
    public PlayerListener pl;
    public ServerListener srl;
    public SQLManager sql;
    public StaffList sl;

    @Override
    public void onLoad()
    {
        this.plugin = this;
        config = new Config(plugin);
    }

    @Override
    public void onEnable()
    {
        this.plugin = this;

        config.load();
        sql = new SQLManager(plugin);

        if (!sql.init())
        {
            NLog.severe("SQL has failed to connect. The plugin is disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        sl = new StaffList(plugin);
        bm = new BanManager(plugin);
        cl = new CommandLoader();
        pl = new PlayerListener(plugin);
        srl = new ServerListener(plugin);

        NLog.info("The plugin has been enabled!");
    }

    @Override
    public void onDisable()
    {
        this.plugin = null;

        config.save();

        try
        {
            sql.getConnection().commit();
            sql.getConnection().close();
        }
        catch (SQLException ex)
        {
            NLog.severe(ex);
        }

        NLog.info("The plugin has been disabled!");
    }
}
