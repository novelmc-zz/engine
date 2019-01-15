package net.novelmc.novelengine.command;

import net.novelmc.novelengine.command.util.CommandBase;
import net.novelmc.novelengine.command.util.CommandParameters;
import net.novelmc.novelengine.command.util.SourceType;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@CommandParameters(description = "Wipe those pesky entities that aren't players.", usage = "/<command> ", aliases = "ew", source = SourceType.BOTH, rank = Rank.TRAINEE)
public class Command_entitywipe extends CommandBase {

    public static final List<EntityType> DONTCLEAR = Arrays.asList(EntityType.ITEM_FRAME, EntityType.ARMOR_STAND);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

        Player player = (Player) sender;
        for (World eworld : Bukkit.getWorlds()) {
            for (Entity entity : eworld.getEntities()) {
                if (!(entity instanceof Player) && !entity.getType().equals(DONTCLEAR)) {
                    entity.remove();
                }
            }
        }

        NUtil.playerAction(player, "Removing all entities", false);
        return true;
    }
}
