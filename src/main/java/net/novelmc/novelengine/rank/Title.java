package net.novelmc.novelengine.rank;

import lombok.Getter;
import net.novelmc.novelengine.NovelEngine;
import net.novelmc.novelengine.util.NUtil;
import org.bukkit.ChatColor;

public enum Title implements Displayable
{

    ARCHITECT("an", "Architect", ChatColor.GREEN),
    DEVELOPER("a", "Developer", ChatColor.DARK_PURPLE);

    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final ChatColor color;
    
    private NovelEngine pl;

    Title(String determiner, String name, ChatColor color)
    {
        this.determiner = determiner;
        this.name = name;
        this.color = color;
        this.tag = NUtil.colorize(pl.config.getString("tags." + name()));
    }

    public String getLoginMessage()
    {
        return determiner + " " + color + name;
    }
}
