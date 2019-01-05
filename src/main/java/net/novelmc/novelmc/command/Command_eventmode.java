package net.novelmc.novelmc.command;

import net.novelmc.novelmc.listener.EventModeListener;
import net.novelmc.novelmc.rank.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;

@CommandParameters(description = "Toggles EventMode", usage = "/<command> [on | off]", aliases = "emode,em", source = SourceType.BOTH, rank = Rank.MANAGER)
public class Command_eventmode
{
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        switch (args[0])
        {
            case "on":
            {
                if (EventModeListener.isEventModeEnabled())
                {
                    sender.sendMessage(ChatColor.RED + "Event mode is already on!");
                    return true;
                }
                Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Turning event mode on");
                EventModeListener.enableEventMode();
                return true;
            }
            case "off":
            {
                if (!EventModeListener.isEventModeEnabled())
                {
                    sender.sendMessage(ChatColor.RED + "Event mode isn't on!");
                    return true;
                }
                Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Turning event mode off");
                EventModeListener.disableEventMode();
                return true;
            }
        }
        sender.sendMessage(ChatColor.GOLD + "Event mode is currently " + (EventModeListener.isEventModeEnabled() ? "enabled." : "disabled."));
        return true;
    }
}
