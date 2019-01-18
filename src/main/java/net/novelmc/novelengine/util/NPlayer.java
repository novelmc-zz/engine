package net.novelmc.novelengine.util;

import java.util.ArrayList;
import net.novelmc.novelengine.banning.BanManager;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.Title;
import net.novelmc.novelengine.rank.architect.Architect;
import net.novelmc.novelengine.rank.architect.ArchitectList;
import net.novelmc.novelengine.rank.staff.StaffList;
import org.bukkit.entity.Player;

public abstract class NPlayer implements Player 
{
    public static ArrayList<Player> busyPlayers = new ArrayList<>();
    public static ArrayList<Player> frozenPlayers = new ArrayList<>();
    public static ArrayList<Player> commandSpyPlayers = new ArrayList<>();
    
    public boolean isBanned()
    {
        return BanManager.isBanned(this);
    }

    public boolean isStaff()
    {
        return StaffList.isStaff(this);
    }

    public boolean isArchitect()
    {
        return ArchitectList.isArchitect(this);
    }

    public Rank getRank()
    {
        return Rank.getRank(this);
    }
    
    // TODO: Use NPlayer instead of Player
    public static boolean isBusy(Player player)
    {
        return busyPlayers.contains(player);
    }
    
    // TODO: Use NPlayer instead of Player
    public static boolean isFrozen(Player player)
    {
        return frozenPlayers.contains(player);
    }
}
