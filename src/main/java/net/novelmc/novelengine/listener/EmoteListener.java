package net.novelmc.novelengine.listener;

import net.novelmc.novelengine.util.NovelBase;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
        message.replace("<3", "\u2764");

        // Set the message
        event.setMessage(message);
    }
}
