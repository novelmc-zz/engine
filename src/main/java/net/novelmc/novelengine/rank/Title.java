package net.novelmc.novelengine.rank;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Title implements Displayable
{

    ARCHITECT("an", "Architect", "ARCHITECT", ChatColor.DARK_GREEN),
    DEVELOPER("a", "Developer", "DEV", ChatColor.DARK_PURPLE),
    ADVISOR("an", "Advisor", "ADVISOR", ChatColor.DARK_RED),
    LEADER("a", "Leader", "LEADER", ChatColor.DARK_RED);

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
