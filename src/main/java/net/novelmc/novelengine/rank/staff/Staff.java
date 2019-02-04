package net.novelmc.novelengine.rank.staff;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.util.NovelBase;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Staff extends NovelBase
{

    @Getter
    private final String configKey;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> ips = new ArrayList<>();
    @Getter
    @Setter
    private String homeIp = null;
    @Getter
    @Setter
    private Rank rank = Rank.TRAINEE;
    @Getter
    @Setter
    private boolean active;
    @Getter
    @Setter
    private boolean advisor = false;
    @Getter
    @Setter
    private boolean leader = false;
    @Getter
    @Setter
    private boolean director = false;
    @Getter
    @Setter
    private boolean commandSpy = false;

    public Staff(String configKey)
    {
        this.configKey = configKey;
    }

    public void save(ConfigurationSection section)
    {
        section.set("name", name);
        section.set("ips", Lists.newArrayList(ips));
        section.set("homeip", homeIp);
        section.set("rank", rank.name());
        section.set("active", active);
        section.set("commandspy", commandSpy);
    }

    public void load(ConfigurationSection section)
    {
        name = section.getString("name", configKey);
        ips.addAll(section.getStringList("ips"));
        homeIp = section.getString("homeip");
        rank = Rank.findRank(section.getString("rank"));
        active = section.getBoolean("active", true);
        commandSpy = section.getBoolean("commandspy");
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":\n")
                .append(" - IPs: ").append(StringUtils.join(ips, ", ")).append("\n")
                .append(" - Home IP: ").append(homeIp).append("\n")
                .append(" - Rank: ").append(rank.name()).append("\n")
                .append(" - Active: ").append(active).append("\n")
                .append(" - CommandSpy: ").append(commandSpy).append("\n");
        return sb.toString();
    }
}
