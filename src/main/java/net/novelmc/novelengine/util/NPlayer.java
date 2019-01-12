package net.novelmc.novelengine.util;

import net.novelmc.novelengine.banning.BanManager;
import org.bukkit.entity.Player;

public abstract class NPlayer implements Player {
    public boolean isBanned()
    {
        return BanManager.isBanned(this);
    }
}
