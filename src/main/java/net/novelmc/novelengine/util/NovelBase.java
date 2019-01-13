package net.novelmc.novelengine.util;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.config.Config;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class NovelBase
{

    // So apparently Minecraft doesn't use the default OS line separator, will do more research on this later -Mafrans
    public static String NEW_LINE = "\n"; // System.getProperty("line.separator");

    public static NovelEngine plugin = NovelEngine.plugin;
    public static Config config = plugin.config;
}
