package net.novelmc.novelengine.command;

import net.novelmc.novelengine.architect.ArchitectList;
import net.novelmc.novelengine.rank.Rank;
import net.novelmc.novelengine.rank.Title;
import net.novelmc.novelengine.staff.StaffList;
import net.novelmc.novelengine.util.NUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandParameters(description = "A list of online players", source = SourceType.BOTH, rank = Rank.IMPOSTOR)
public class Command_list
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length > 0)
        {
            return false;
        }

        List<String> impostors = new ArrayList<>();
        List<String> non_ops = new ArrayList<>();
        List<String> ops = new ArrayList<>();
        List<String> trainees = new ArrayList<>();
        List<String> mods = new ArrayList<>();
        List<String> senior_mods = new ArrayList<>();
        List<String> admins = new ArrayList<>();
        List<String> architects = new ArrayList<>();
        List<String> developers = new ArrayList<>();
        List<String> managers = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        sb.append(ChatColor.GRAY).append("There are ")
                .append(ChatColor.DARK_GRAY).append(Bukkit.getOnlinePlayers().size()).append("/").append(Bukkit.getMaxPlayers())
                .append(ChatColor.GRAY).append(" players online\n");

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (StaffList.isImpostor(player) || ArchitectList.isImpostor(player))
            {
                impostors.add(player.getName());
            }
            else if (Rank.getRank(player).isAtLeast(Rank.MANAGER))
            {
                managers.add(player.getName());
            }
            else if (NUtil.DEVELOPERS.contains(player.getName()))
            {
                developers.add(player.getName());
            }
            else if (ArchitectList.isArchitect(player))
            {
                architects.add(player.getName());
            }
            else if (Rank.getRank(player).isAtLeast(Rank.ADMIN))
            {
                admins.add(player.getName());
            }
            else if (Rank.getRank(player).isAtLeast(Rank.SENIOR_MOD))
            {
                senior_mods.add(player.getName());
            }
            else if (Rank.getRank(player).isAtLeast(Rank.MOD))
            {
                mods.add(player.getName());
            }
            else if (Rank.getRank(player).isAtLeast(Rank.TRAINEE))
            {
                trainees.add(player.getName());
            }
            else if (player.isOp())
            {
                ops.add(player.getName());
            }
            else
            {
                non_ops.add(player.getName());
            }
        }

        sb.append((impostors.isEmpty() ? "" : ChatColor.WHITE + Rank.IMPOSTOR.getTag() + ": " + StringUtils.join(impostors, ", ") + "\n"))
                .append((non_ops.isEmpty() ? "" : ChatColor.WHITE + Rank.NON_OP.getTag() + ": " + StringUtils.join(non_ops, ", ") + "\n"))
                .append((ops.isEmpty() ? "" : ChatColor.YELLOW + Rank.OP.getTag() + ": " + StringUtils.join(ops, ", ") + "\n"))
                .append((trainees.isEmpty() ? "" : ChatColor.DARK_AQUA + Rank.TRAINEE.getTag() + ": " + StringUtils.join(trainees, ", ") + "\n"))
                .append((mods.isEmpty() ? "" : ChatColor.GOLD + Rank.MOD.getTag() + ": " + StringUtils.join(mods, ", ") + "\n"))
                .append((senior_mods.isEmpty() ? "" : ChatColor.GOLD + Rank.SENIOR_MOD.getTag() + StringUtils.join(senior_mods, ", ") + "\n"))
                .append((admins.isEmpty() ? "" : ChatColor.RED + Rank.ADMIN.getTag() + ": " + StringUtils.join(admins, ", ") + "\n"))
                .append((architects.isEmpty() ? "" : ChatColor.DARK_PURPLE + Title.ARCHITECT.getTag() + ": " + StringUtils.join(architects, ", ") + "\n"))
                .append((developers.isEmpty() ? "" : ChatColor.BLUE + Title.DEVELOPER.getTag() + ": " + StringUtils.join(developers, ", ") + "\n"))
                .append((managers.isEmpty() ? "" : ChatColor.BLUE + Rank.MANAGER.getTag() + ": " + StringUtils.join(managers, ", ")));

        sender.sendMessage(sb.toString());
        return true;
    }
}
