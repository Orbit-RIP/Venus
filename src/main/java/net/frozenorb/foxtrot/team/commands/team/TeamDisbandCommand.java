package net.frozenorb.foxtrot.team.commands.team;

import com.google.common.collect.ImmutableMap;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.track.TeamActionTracker;
import net.frozenorb.foxtrot.team.track.TeamActionType;
import cc.fyre.proton.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeamDisbandCommand {

    @Command(names={ "team disband", "t disband", "f disband", "faction disband", "fac disband" }, permission="")
    public static void teamDisband(Player player) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);

        if (team == null){
            player.sendMessage(ChatColor.RED + "You are not on a team!");
            return;
        }

        if (!team.isOwner(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You must be the leader of the team to disband it!");
            return;
        }

        if (team.isRaidable()) {
            player.sendMessage(ChatColor.RED + "You cannot disband your team while raidable.");
            return;
        }

        team.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + player.getName() + " has disbanded the team.");

        if (team.getHQ() != null && team.getHQWaypoint() != null) {
            LunarClientAPI.getInstance().removeWaypoint(player, team.getHQWaypoint());
        }

        TeamActionTracker.logActionAsync(team, TeamActionType.PLAYER_DISBAND_TEAM, ImmutableMap.of(
                "playerId", player.getUniqueId(),
                "playerName", player.getName()
        ));

        team.disband();
    }

}