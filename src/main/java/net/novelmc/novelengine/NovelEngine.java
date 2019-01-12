package net.novelmc.novelengine;

import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.command.util.CommandLoader;
import net.novelmc.novelengine.config.ArchitectConfig;
import net.novelmc.novelengine.config.Config;
import net.novelmc.novelengine.config.StaffConfig;
import net.novelmc.novelengine.listener.EventModeListener;
import net.novelmc.novelengine.listener.PlayerListener;
import net.novelmc.novelengine.listener.ServerListener;
import net.novelmc.novelengine.rank.architect.ArchitectList;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.PlayerDatabase;
import net.novelmc.novelengine.util.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class NovelEngine extends JavaPlugin
{

    public static NovelEngine plugin;
    public ArchitectConfig architectConfig;
    public BanManager banManager;
    public CommandLoader commandLoader;
    public Config config;
    public EventModeListener eventModeListener;
    public PlayerListener playerListener;
    public PlayerDatabase playerDatabase;
    public ServerListener serverListener;
    public SQLManager sqlManager;
    public StaffConfig staffConfig;
    public StaffList staffList;
    public ArchitectList architectList;

    @Override
    public void onLoad()
    {
        plugin = this;
        config = new Config(plugin);
        staffConfig = new StaffConfig(plugin);
        architectConfig = new ArchitectConfig(plugin);
    }

    @Override
    public void onEnable()
    {
        plugin = this;

        config.load();
        staffConfig.load();
        architectConfig.load();

        sqlManager = new SQLManager(plugin);

        if (!sqlManager.init())
        {
            NLog.severe("Unable to connect to MySQL database! Shutting down...");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        staffList = new StaffList();
        architectList = new ArchitectList();
        banManager = new BanManager();
        commandLoader = new CommandLoader("Command_");
        playerListener = new PlayerListener();
        playerDatabase = new PlayerDatabase();
        serverListener = new ServerListener();
        eventModeListener = new EventModeListener();

        commandLoader.registerCommands();


        NLog.info("The plugin has been enabled!");
    }

    @Override
    public void onDisable()
    {
        plugin = null;

        config.save();
        staffConfig.save();
        architectConfig.save();

        NLog.info("The plugin has been disabled!");
    }
}
