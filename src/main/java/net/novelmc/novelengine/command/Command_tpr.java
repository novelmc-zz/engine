/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.novelmc.novelengine.command;

import java.util.Random;
import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Nilbog
 */
@CommandParameters(description = "Teleport to a random location.", usage = "/<command>", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_tpr extends CommandBase
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        Player p = (Player) sender;
        World w = p.getWorld();
        Random random = new Random();
        int x = random.nextInt(32767 - -32767 + 1) + -32767;
        int z = random.nextInt(32767 - -32767 + 1) + -32767;
        int y = w.getHighestBlockYAt(x, z);
        
        Location l = new Location(w, x, y ,z);
        p.teleport(l);
        p.sendMessage(ChatColor.GRAY + "You have been teleported to X:" + x + " Y:" + y + " Z:" + z);
        
        return true;
    }
}