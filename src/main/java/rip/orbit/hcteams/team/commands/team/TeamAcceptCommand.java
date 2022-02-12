package rip.orbit.hcteams.team.commands.team;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.server.SpawnTagHandler;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.dtr.DTRHandler;
import rip.orbit.hcteams.team.event.FullTeamBypassEvent;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;
import rip.orbit.nebula.profile.stat.GlobalStatistic;
import rip.orbit.nebula.profile.stat.StatType;

public class TeamAcceptCommand {

    @Command(names = {"team accept", "t accept", "f accept", "faction accept", "fac accept", "team a", "t a", "f a", "faction a", "fac a", "team join", "t join", "f join", "faction join", "fac join", "team j", "t j", "f j", "faction j", "fac j"}, permission = "")
    public static void teamAccept(Player sender, @cc.fyre.proton.command.param.Parameter(name = "team") Team team) {
        if (HCF.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        if (team.getInvitations().contains(sender.getUniqueId())) {
            if (HCF.getInstance().getTeamHandler().getTeam(sender) != null) {
                sender.sendMessage(ChatColor.RED + "You are already on a team!");
                return;
            }

            if (team.getMembers().size() >= HCF.getInstance().getMapHandler().getTeamSize()) {
                FullTeamBypassEvent attemptEvent = new FullTeamBypassEvent(sender, team);
                HCF.getInstance().getServer().getPluginManager().callEvent(attemptEvent);

                if (!attemptEvent.isAllowBypass()) {
                    sender.sendMessage(ChatColor.RED + team.getName() + " cannot be joined: Team is full!");
                    return;
                }
            }

            if (DTRHandler.isOnCooldown(team) && !HCF.getInstance().getServerHandler().isPreEOTW() && !HCF.getInstance().getMapHandler().isKitMap() && !HCF.getInstance().getServerHandler().isVeltKitMap()) {
                sender.sendMessage(ChatColor.RED + team.getName() + " cannot be joined: Team not regenerating DTR!");
                return;
            }

            if (team.getMembers().size() >= 15 && HCF.getInstance().getTeamHandler().isRostersLocked()) {
                sender.sendMessage(ChatColor.RED + team.getName() + " cannot be joined: Team rosters are locked server-wide!");
                return;
            }

            if (SpawnTagHandler.isTagged(sender)) {
                sender.sendMessage(ChatColor.RED + team.getName() + " cannot be joined: You are combat tagged!");
                return;
            }

            team.getInvitations().remove(sender.getUniqueId());
            team.addMember(sender.getUniqueId());
            HCF.getInstance().getTeamHandler().setTeam(sender.getUniqueId(), team);

            team.sendMessage(ChatColor.YELLOW + sender.getName() + " has joined the team!");
            Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(sender.getUniqueId());

            for (GlobalStatistic statistic : profile.getGlobalStatistics()) {
                if (HCF.getInstance().getMapHandler().isKitMap()) {
                    if (statistic.getStatType() == StatType.KITS) {
                        if (!statistic.getPastTeams().contains(team.getName())) {
                            statistic.getPastTeams().add(team.getName());
                            return;
                        }
                    }
                } else {
                    if (statistic.getStatType() == StatType.HCF) {
                        if (!statistic.getPastTeams().contains(team.getName())) {
                            statistic.getPastTeams().add(team.getName());
                            return;
                        }
                    }
                }
            }

        } else {
            sender.sendMessage(ChatColor.RED + "This team has not invited you!");
        }
    }

}