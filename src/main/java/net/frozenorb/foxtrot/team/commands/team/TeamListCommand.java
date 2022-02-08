package net.frozenorb.foxtrot.team.commands.team;

import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public class TeamListCommand {
    public static final DecimalFormat DTR_FORMAT = new DecimalFormat("0.00");

    @Command(names={ "team list", "t list", "f list", "faction list", "fac list" }, permission="")
    public static void teamList(final Player sender, @Parameter(name="page", defaultValue="1") final int page) {
        // This is sort of intensive so we run it async (cause who doesn't love async!)
        new BukkitRunnable() {

            public void run() {
                if (page < 1) {
                    sender.sendMessage(ChatColor.RED + "You cannot view a page less than 1");
                    return;
                }

                Map<Team, Integer> teamPlayerCount = new HashMap<>();

                // Sort of weird way of getting player counts, but it does it in the least iterations (1), which is what matters!
                for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
                    if (player.hasMetadata("invisible")) {
                        continue;
                    }

                    Team playerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(player);

                    if (playerTeam != null) {
                        if (teamPlayerCount.containsKey(playerTeam)) {
                            teamPlayerCount.put(playerTeam, teamPlayerCount.get(playerTeam) + 1);
                        } else {
                            teamPlayerCount.put(playerTeam, 1);
                        }
                    }
                }

                int maxPages = (teamPlayerCount.size() / 10) + 1;
                int currentPage = Math.min(page, maxPages);

                LinkedHashMap<Team, Integer> sortedTeamPlayerCount = sortByValues(teamPlayerCount);

                int start = (currentPage - 1) * 10;
                int index = 0;

                sender.sendMessage(Team.GRAY_LINE);
                sender.sendMessage(ChatColor.GOLD + "Faction List " +  ChatColor.WHITE + "(Page " + currentPage + "/" + maxPages + ")");

                for (Map.Entry<Team, Integer> teamEntry : sortedTeamPlayerCount.entrySet()) {
                    index++;

                    if (index < start) {
                        continue;
                    }

                    if (index > start + 10) {
                        break;
                    }

                    FancyMessage teamMessage = new FancyMessage();

                    Team team = teamEntry.getKey();

                    teamMessage.text("  " + index + ". ").color(ChatColor.WHITE).then();
                    teamMessage.text(teamEntry.getKey().getName()).color(ChatColor.YELLOW).tooltip(
                            ChatColor.YELLOW + "DTR: " + teamEntry.getKey().getDTRColor() + Team.DTR_FORMAT.format(teamEntry.getKey().getDTR()) + ChatColor.YELLOW + " / " + teamEntry.getKey().getMaxDTR() + "\n" +
                            ChatColor.GRAY + "Click to view team info").command("/t who " + teamEntry.getKey().getName()).then();
                    teamMessage.text(" [" + teamEntry.getValue() + "/" + ChatColor.GRAY + teamEntry.getKey().getSize() + "]").color(ChatColor.GRAY).then();
                    teamMessage.text(" [" + team.getDTRColor() + DTR_FORMAT.format(team.getDTR()) + team.getDTRSuffix() + ChatColor.GRAY + "/" + team.getMaxDTR() + " DTR]").color(ChatColor.GRAY);

                    teamMessage.send(sender);
                }

                sender.sendMessage(ChatColor.WHITE + "You can view other pages by using " + ChatColor.GRAY + "/f list <page>" + ChatColor.WHITE + ".");
                sender.sendMessage(Team.GRAY_LINE);
            }

        }.runTaskAsynchronously(Foxtrot.getInstance());
    }

    public static LinkedHashMap<Team, Integer> sortByValues(Map<Team, Integer> map) {
        LinkedList<java.util.Map.Entry<Team, Integer>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, (o1, o2) -> (o2.getValue().compareTo(o1.getValue())));

        LinkedHashMap<Team, Integer> sortedHashMap = new LinkedHashMap<>();

        for (Map.Entry<Team, Integer> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return (sortedHashMap);
    }

}