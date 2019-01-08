package net.novelmc.novelengine.util;

import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.config.Config;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class NovelBase
{

    // So apparently Minecraft doesn't use the default OS line separator, will do more research on this later -Mafrans
    public String NEW_LINE = /* System.getProperty("line.separator"); */ "\n";

    public NovelEngine plugin = NovelEngine.plugin;
    public Config config = plugin.config;
}
