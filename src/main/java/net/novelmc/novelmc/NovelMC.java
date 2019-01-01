package net.novelmc.novelmc;

import net.novelmc.novelmc.banning.BanManager;
import net.novelmc.novelmc.command.CommandLoader;
import net.novelmc.novelmc.config.ArchitectConfig;
import net.novelmc.novelmc.config.Config;
import net.novelmc.novelmc.config.StaffConfig;
import net.novelmc.novelmc.listener.PlayerListener;
import net.novelmc.novelmc.listener.ServerListener;
import net.novelmc.novelmc.staff.StaffList;
import net.novelmc.novelmc.util.NLog;
import net.novelmc.novelmc.util.SQLManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NovelMC extends JavaPlugin
{

    public static NovelMC plugin;
    public ArchitectConfig ac;
    public BanManager bm;
    public CommandLoader cl;
    public Config config;
    public PlayerListener pl;
    public ServerListener srl;
    public SQLManager sql;
    public StaffConfig staff;
    public StaffList sl;

    @Override
    public void onLoad()
    {
        this.plugin = this;
        config = new Config(plugin);
        staff = new StaffConfig(plugin);
        ac = new ArchitectConfig(plugin);
    }

    @Override
    public void onEnable()
    {
        this.plugin = this;

        config.load();
        staff.load();
        ac.load();

        sql = new SQLManager(plugin);

        if (!sql.init())
        {
            NLog.severe("Unable to connect to MySQL database! Shutting down...");
            this.getServer().getPluginManager().disablePlugin(this);
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
        staff.save();
        ac.save();

        NLog.info("The plugin has been disabled!");
    }
}
