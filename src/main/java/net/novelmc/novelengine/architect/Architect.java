package net.novelmc.novelengine.architect;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Architect
{

    @Getter
    private String configKey;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> ips = new ArrayList<String>();

    public Architect(String configKey)
    {
        this.configKey = configKey;
    }

    public void save(ConfigurationSection section)
    {
        section.set("name", name);
        section.set("ips", Lists.newArrayList(ips));
    }

    public void load(ConfigurationSection section)
    {
        name = section.getString("name", configKey);
        ips.addAll(section.getStringList("ips"));
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":\n")
                .append(" - IPs: ").append(StringUtils.join(ips, ", ")).append("\n");
        return sb.toString();
    }
}