package rip.orbit.hcteams.team.commands.team;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.team.Team;

import java.util.UUID;

public class TeamDemoteCommand {

    @Command(names={ "team demote", "t demote", "f demote", "faction demote", "fac demote" }, permission="")
    public static void teamDemote(Player sender, @cc.fyre.proton.command.param.Parameter(name="player") UUID player) {
        if (HCF.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        Team team = HCF.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You need to be in a team to do this.");
            return;
        }

        if (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team co-leaders (and above) can do this.");
            return;
        }

        if (!team.isMember(player)) {
            sender.sendMessage(ChatColor.DARK_AQUA + UUIDUtils.name(player) + " is not on your team.");
            return;
        }

        if (team.isOwner(player)) {
            sender.sendMessage(ChatColor.RED + UUIDUtils.name(player) + " is the leader. To change leaders, the team leader must use /t leader <name>");
        } else if (team.isCoLeader(player)) {
            if (team.isOwner(sender.getUniqueId())) {
                team.removeCoLeader(player);
                team.addCaptain(player);
                team.sendMessage(ChatColor.DARK_AQUA + UUIDUtils.name(player) + " has been demoted to Captain!");
            } else {
                sender.sendMessage(ChatColor.RED + "Only the team leader can demote Co-Leaders.");
            }
        } else if (team.isCaptain(player)) {
            team.removeCaptain(player);
            team.sendMessage(ChatColor.DARK_AQUA + UUIDUtils.name(player) + " has been demoted to a member!");
        } else {
            sender.sendMessage(ChatColor.RED + UUIDUtils.name(player) + " is currently a member. To kick them, use /t kick");
        }
    }

}