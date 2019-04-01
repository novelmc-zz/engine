package net.novelmc.novelengine;

import java.util.function.IntToDoubleFunction;
import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.blocking.BlockBlocker;
import net.novelmc.novelengine.blocking.CommandBlocker;
import net.novelmc.novelengine.blocking.InteractBlocker;
import net.novelmc.novelengine.command.util.CommandLoader;
import net.novelmc.novelengine.config.ArchitectConfig;
import net.novelmc.novelengine.config.Config;
import net.novelmc.novelengine.config.StaffConfig;
import net.novelmc.novelengine.listener.MuteListener;
import net.novelmc.novelengine.listener.ServerModeListener;
import net.novelmc.novelengine.listener.PlayerListener;
import net.novelmc.novelengine.listener.ServerListener;
import net.novelmc.novelengine.rank.architect.ArchitectList;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.PlayerDatabase;
import net.novelmc.novelengine.util.SQLManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NovelEngine extends JavaPlugin
{

    public static NovelEngine plugin;
    public ArchitectConfig architectConfig;
    public BanManager banManager;
    public BlockBlocker blockBlocker;
    public CommandBlocker commandBlocker;
    public CommandLoader commandLoader;
    public Config config;
    public InteractBlocker interactBlocker;
    public MuteListener muteListener;
    public ServerModeListener serverModeListener;
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

        if ( ! sqlManager.init())
        {
            NLog.severe("Unable to connect to MySQL database! Shutting down...");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        staffList = new StaffList();
        architectList = new ArchitectList();
        banManager = new BanManager();
        blockBlocker = new BlockBlocker();
        commandBlocker = new CommandBlocker();
        commandLoader = new CommandLoader("Command_");
        interactBlocker = new InteractBlocker();
        muteListener = new MuteListener();
        playerListener = new PlayerListener();
        playerDatabase = new PlayerDatabase();
        serverListener = new ServerListener();
        serverModeListener = new ServerModeListener();

        commandLoader.registerCommands();

        NLog.info("The plugin has been enabled!");
    }

    @Override
    public void onDisable()
    {
        config.save();
        staffConfig.save();
        architectConfig.save();

        plugin = null;

        NLog.info("The plugin has been disabled!");
    }
}
