package net.novelmc.novelengine.rank;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Title implements Displayable
{

    ARCHITECT("an", "Architect", "ARC", ChatColor.GREEN),
    DEVELOPER("a", "Developer", "DEV", ChatColor.DARK_PURPLE),
    DIRECTOR("a", "Director", "DIR", ChatColor.RED);

    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final ChatColor color;

    Title(String determiner, String name, String tag, ChatColor color)
    {
        this.determiner = determiner;
        this.name = name;
        this.color = color;
        this.tag = color + "" + ChatColor.BOLD + tag + ChatColor.RESET + color;
    }

    public String getLoginMessage()
    {
        return determiner + " " + color + name;
    }
}
