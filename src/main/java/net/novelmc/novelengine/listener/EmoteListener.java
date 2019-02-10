package net.novelmc.novelengine.listener;

import net.novelmc.novelengine.banning.Ban;
import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.command.Command_vanish;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.architect.ArchitectList;
import net.novelmc.novelengine.rank.staff.StaffList;
import net.novelmc.novelengine.util.NLog;
import net.novelmc.novelengine.util.NUtil;
import net.novelmc.novelengine.util.NovelBase;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import net.novelmc.novelengine.util.NPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.server.ServerCommandEvent;

public class EmoteListener extends NovelBase implements Listener
{

    public EmoteListener()
    {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static void handleEmotes(AsyncPlayerChatEvent event)
    {
        String message = event.getMessage();

        // Heart emoticon
        message.replace("<3", "â?¤");

        // Set the message
        event.setMessage(message);
    }
}
