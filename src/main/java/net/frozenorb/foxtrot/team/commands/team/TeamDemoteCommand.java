package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamDemoteCommand {

    @Command(names={ "team demote", "t demote", "f demote", "faction demote", "fac demote" }, permission="")
    public static void teamDemote(Player sender, @Parameter(name="player") UUID player) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.GOLD + "Only team co-leaders (and above) can do this.");
            return;
        }

        if (!team.isMember(player)) {
            sender.sendMessage(ChatColor.GOLD + UUIDUtils.name(player) + " is not on your team.");
            return;
        }

        if (team.isOwner(player)) {
            sender.sendMessage(ChatColor.RED + UUIDUtils.name(player) + " is the leader. To change leaders, the team leader must use /t leader <name>");
        } else if (team.isCoLeader(player)) {
            if (team.isOwner(sender.getUniqueId())) {
                team.removeCoLeader(player);
                team.addCaptain(player);
                team.sendMessage(ChatColor.GOLD + UUIDUtils.name(player) + " has been demoted to Captain!");
            } else {
                sender.sendMessage(ChatColor.RED + "Only the team leader can demote Co-Leaders.");
            }
        } else if (team.isCaptain(player)) {
            team.removeCaptain(player);
            team.sendMessage(ChatColor.GOLD + UUIDUtils.name(player) + " has been demoted to a member!");
        } else {
            sender.sendMessage(ChatColor.RED + UUIDUtils.name(player) + " is currently a member. To kick them, use /t kick");
        }
    }

}