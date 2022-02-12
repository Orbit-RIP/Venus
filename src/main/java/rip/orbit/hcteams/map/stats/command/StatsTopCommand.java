package rip.orbit.hcteams.map.stats.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import lombok.Getter;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.stats.StatsEntry;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

import java.util.Map;

public class StatsTopCommand {

    @Command(names = {"statstop", "leaderboards"}, permission = "")
    public static void statstop(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name = "objective", defaultValue = "kills") StatsObjective objective) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
        sender.sendMessage(ChatColor.WHITE + "Leaderboards: " + ChatColor.GOLD + "Kills");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));

        int index = 0;
        for (Map.Entry<StatsEntry, String> entry : HCF.getInstance().getMapHandler().getStatsHandler().getLeaderboards(objective, 10).entrySet()) {
            if (entry.getKey().getOwner() != null) {
                index++;
                Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(entry.getKey().getOwner(), true);
                sender.sendMessage((index == 1 ? ChatColor.WHITE + "1) " : ChatColor.WHITE.toString() + index + ") ") + ChatColor.GOLD.toString() + (objective == StatsObjective.TOP_FACTION ? entry.getKey().getFaction() : profile.getFancyName()) + ": " + ChatColor.WHITE + entry.getValue());
            }
        }

        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
    }

    @Getter
    public enum StatsObjective {

        KILLS("Kills", "k"),
        DEATHS("Deaths", "d"),
        KD("KD", "kdr"),
        TOP_FACTION("Top Factions", "topfaction", "tf"),
        HIGHEST_KILLSTREAK("Highest Killstreak", "killstreak", "highestkillstreak", "ks", "highestks", "hks");

        private String name;
        private String[] aliases;

        StatsObjective(String name, String... aliases) {
            this.name = name;
            this.aliases = aliases;
        }

    }

}
