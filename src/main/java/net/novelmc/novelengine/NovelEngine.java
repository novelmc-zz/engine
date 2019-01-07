package net.novelmc.novelengine;

import net.novelmc.novelengine.rank.architect.ArchitectList;
import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.command.util.CommandLoader;
import net.novelmc.novelengine.config.ArchitectConfig;
import net.novelmc.novelengine.config.Config;
import net.novelmc.novelengine.config.StaffConfig;
import net.novelmc.novelengine.listener.EventModeListener;
import net.novelmc.novelengine.listener.PlayerListener;
import net.novelmc.novelengine.listener.ServerListener;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.SQLManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NovelEngine extends JavaPlugin {

    public static NovelEngine plugin;
    public ArchitectConfig ac;
    public BanManager bm;
    public CommandLoader cl;
    public Config config;
    public EventModeListener eml;
    public PlayerListener pl;
    public ServerListener srl;
    public SQLManager sql;
    public StaffConfig staff;
    public StaffList sl;
    public ArchitectList al;

    @Override
    public void onLoad()
    {
        plugin = this;
        config = new Config(plugin);
        staff = new StaffConfig(plugin);
        ac = new ArchitectConfig(plugin);
    }

    @Override
    public void onEnable()
    {
        plugin = this;

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
        al = new ArchitectList(plugin);
        bm = new BanManager(plugin);
        cl = new CommandLoader("", "Command");
        pl = new PlayerListener(plugin);
        srl = new ServerListener(plugin);
        eml = new EventModeListener(plugin);


        NLog.info("The plugin has been enabled!");
    }

    @Override
    public void onDisable()
    {
        plugin = null;

        config.save();
        staff.save();
        ac.save();

        NLog.info("The plugin has been disabled!");
    }
}
