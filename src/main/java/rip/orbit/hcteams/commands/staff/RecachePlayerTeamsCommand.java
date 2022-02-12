package rip.orbit.hcteams.commands.staff;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.team.Team;

import java.util.*;

public class RecachePlayerTeamsCommand {

    @Command(names={ "playerteamcache rebuild" }, permission="op")
    public static void recachePlayerTeamsRebuild(Player sender) {
        sender.sendMessage(ChatColor.DARK_PURPLE + "Rebuilding player team cache...");
        HCF.getInstance().getTeamHandler().recachePlayerTeams();
        sender.sendMessage(ChatColor.DARK_PURPLE + "The player death cache has been rebuilt.");
    }

    @Command(names={ "playerteamcache check" }, permission="op")
    public static void recachePlayerTeams(Player sender) {
        sender.sendMessage(ChatColor.DARK_PURPLE + "Checking player team cache...");
        Map<UUID, String> dealtWith = new HashMap<>();
        Set<UUID> errors = new HashSet<>();

        for (Team team : HCF.getInstance().getTeamHandler().getTeams()) {
            for (UUID member : team.getMembers()) {
                if (dealtWith.containsKey(member) && !errors.contains(member)) {
                    errors.add(member);
                    sender.sendMessage(ChatColor.RED + " - " + member + " (Team: " + team.getName() + ", Expected: " + dealtWith.get(member) + ")");
                    continue;
                }

                dealtWith.put(member, team.getName());
            }
        }

        if (errors.size() == 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "No errors found while checking player team cache.");
        } else {
            sender.sendMessage(ChatColor.DARK_PURPLE.toString() + errors.size() + " error(s) found while checking player team cache.");
        }
    }

}