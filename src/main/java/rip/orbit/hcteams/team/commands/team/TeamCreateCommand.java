package rip.orbit.hcteams.team.commands.team;

import cc.fyre.proton.command.Command;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.commands.staff.EOTWCommand;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.track.TeamActionTracker;
import rip.orbit.hcteams.team.track.TeamActionType;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;
import rip.orbit.nebula.profile.stat.GlobalStatistic;
import rip.orbit.nebula.profile.stat.StatType;

import java.util.Set;
import java.util.regex.Pattern;

public class TeamCreateCommand {

    public static Pattern ALPHA_NUMERIC = Pattern.compile("[^a-zA-Z0-9]");
    private static Set<String> disallowedTeamNames = ImmutableSet.of("list", "Glowstone");

    @Command(names = {"team create", "t create", "f create", "faction create", "fac create"}, permission = "")
    public static void teamCreate(Player sender, @cc.fyre.proton.command.param.Parameter(name = "team") String team) {
        if (HCF.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        if (HCF.getInstance().getTeamHandler().getTeam(sender) != null) {
            sender.sendMessage(ChatColor.GRAY + "You're already in a team!");
            return;
        }

        if (team.length() > 16) {
            sender.sendMessage(ChatColor.RED + "Maximum team name size is 16 characters!");
            return;
        }

        if (team.length() < 3) {
            sender.sendMessage(ChatColor.RED + "Minimum team name size is 3 characters!");
            return;
        }

//        if (TeamGeneralConfiguration.getDisallowedNames().contains(team.toLowerCase()) && !sender.isOp()) {
//            sender.sendMessage(ChatColor.RED + "That faction name is not allowed.");
//            return;
//        }

        if (HCF.getInstance().getTeamHandler().getTeam(team) != null) {
            sender.sendMessage(ChatColor.GRAY + "That team already exists!");
            return;
        }

        if (ALPHA_NUMERIC.matcher(team).find()) {
            sender.sendMessage(ChatColor.RED + "Team names must be alphanumeric!");
            return;
        }

        if (EOTWCommand.realFFAStarted()) {
            sender.sendMessage(ChatColor.RED + "You can't create teams during FFA.");
            return;
        }

        // sender.sendMessage(ChatColor.DARK_AQUA + "Team Created!");
        sender.sendMessage(ChatColor.GRAY + "To learn more about teams, do /team");

        Team createdTeam = new Team(team);

        TeamActionTracker.logActionAsync(createdTeam, TeamActionType.PLAYER_CREATE_TEAM, ImmutableMap.of(
                "playerId", sender.getUniqueId(),
                "playerName", sender.getName()
        ));

        createdTeam.setUniqueId(new ObjectId());
        createdTeam.setOwner(sender.getUniqueId());
        createdTeam.setName(team);
        createdTeam.setDTR(1);

        HCF.getInstance().getTeamHandler().setupTeam(createdTeam);

        Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(sender.getUniqueId());

        Bukkit.broadcastMessage(ChatColor.YELLOW + "Team " + ChatColor.BLUE + createdTeam.getName() + ChatColor.YELLOW + " has been " + ChatColor.GREEN + "created" + ChatColor.YELLOW + " by " + sender.getDisplayName());

        for (GlobalStatistic statistic : profile.getGlobalStatistics()) {
            if (HCF.getInstance().getMapHandler().isKitMap()) {
                if (statistic.getStatType() == StatType.KITS) {
                    if (!statistic.getPastTeams().contains(createdTeam.getName())) {
                        statistic.getPastTeams().add(createdTeam.getName());
                        return;
                    }
                }
            } else {
                if (statistic.getStatType() == StatType.HCF) {
                    if (!statistic.getPastTeams().contains(createdTeam.getName())) {
                        statistic.getPastTeams().add(createdTeam.getName());
                        return;
                    }
                }
            }
        }

    }

}