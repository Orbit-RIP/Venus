package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.deathmessage.event.PlayerKilledEvent;
import net.frozenorb.foxtrot.events.events.EventCapturedEvent;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.event.TeamMissionCompleteEvent;
import net.frozenorb.foxtrot.team.event.TeamPointsChangeEvent;
import net.frozenorb.foxtrot.team.mission.MissionType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Enderman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author xanderume@gmail (JavaProject)
 */
public class TeamMissionListener implements Listener {

    //All of these events should most likely have MONITOR priority.

    @EventHandler(priority = EventPriority.MONITOR)
    private void onTeamMissionComplete(TeamMissionCompleteEvent event) {
        event.getTeam().setTokens(event.getMissionType().getTokens());
        event.getTeam().getCompletedMissions().add(event.getMissionType());

        Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.GOLD + "[Missions] " + ChatColor.BLUE + event.getTeam().getName() + ChatColor.YELLOW + " has completed the " + ChatColor.WHITE + event.getMissionType().getTitle() + ChatColor.YELLOW + "!");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onEntityDeath(EntityDeathEvent event) {

        if (!(event.getEntity() instanceof Enderman)) {
            return;
        }

        if (event.getEntity().getKiller() == null) {
            return;
        }

        final Team team = Foxtrot.getInstance().getTeamHandler().getTeam(event.getEntity().getKiller());

        if (team == null) {
            return;
        }

        team.addEnderManKill();

        if (team.getCompletedMissions().contains(MissionType.ENDER_MAN)) {
            return;
        }

        if (MissionType.ENDER_MAN.hasCompleted(team)) {
            new TeamMissionCompleteEvent(team,MissionType.ENDER_MAN).call();
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onBlockBreak(BlockBreakEvent event) {

        if (event.isCancelled()) {
            return;
        }

        if (event.getBlock().getType() != Material.DIAMOND_ORE) {
            return;
        }

        final Team team = Foxtrot.getInstance().getTeamHandler().getTeam(event.getPlayer());

        if (team == null) {
            return;
        }

        if (team.getCompletedMissions().contains(MissionType.DIAMOND_ORE)) {
            return;
        }

        if (MissionType.DIAMOND_ORE.hasCompleted(team)) {
            new TeamMissionCompleteEvent(team,MissionType.DIAMOND_ORE).call();
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerDeath(PlayerKilledEvent event) {

        final Team team = Foxtrot.getInstance().getTeamHandler().getTeam(event.getKiller());

        if (team == null) {
            return;
        }

        if (team.getCompletedMissions().contains(MissionType.FACTION_KILLS)) {
            return;
        }

        if (MissionType.FACTION_KILLS.hasCompleted(team)) {
            new TeamMissionCompleteEvent(team,MissionType.FACTION_KILLS).call();
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onEventCapture(EventCapturedEvent event) {

        final Team team = Foxtrot.getInstance().getTeamHandler().getTeam(event.getPlayer());

        if (team == null) {
            return;
        }

        if (team.getCompletedMissions().contains(MissionType.KOTH_CAPTURES)) {
            return;
        }

        if (MissionType.KOTH_CAPTURES.hasCompleted(team)) {
            new TeamMissionCompleteEvent(team,MissionType.KOTH_CAPTURES).call();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPointsChange(TeamPointsChangeEvent event) {

        if (event.getTeam().getCompletedMissions().contains(MissionType.TEAM_POINTS)) {
            return;
        }

        if (MissionType.TEAM_POINTS.hasCompleted(event.getTeam())) {
            new TeamMissionCompleteEvent(event.getTeam(),MissionType.TEAM_POINTS).call();
        }

    }

}
