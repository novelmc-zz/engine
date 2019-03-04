package net.novelmc.novelengine.rank.architect;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.novelmc.novelengine.util.NovelBase;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Architect extends NovelBase
{

    @Getter
    private final String configKey;
    @Getter
    @Setter
    private String uuid;
    @Getter
    @Setter
    private List<String> ips = new ArrayList<String>();

    public Architect(String configKey)
    {
        this.configKey = configKey;
    }

    public void save(ConfigurationSection section)
    {
        section.set("uuid", uuid);
        section.set("ips", Lists.newArrayList(ips));
    }

    public void load(ConfigurationSection section)
    {
        uuid = section.getString("uuid", configKey);
        ips.addAll(section.getStringList("ips"));
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(uuid).append(":").append(NEW_LINE)
                .append(" - IPs: ").append(StringUtils.join(ips, ", ")).append(NEW_LINE);
        return sb.toString();
    }
}
