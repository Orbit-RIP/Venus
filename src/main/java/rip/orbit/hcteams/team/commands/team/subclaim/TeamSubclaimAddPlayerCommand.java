package rip.orbit.hcteams.team.commands.team.subclaim;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.claims.Subclaim;

import java.util.UUID;

public class TeamSubclaimAddPlayerCommand {

    @Command(names={ "team subclaim addplayer", "t subclaim addplayer", "f subclaim addplayer", "faction subclaim addplayer", "fac subclaim addplayer", "team sub addplayer", "t sub addplayer", "f sub addplayer", "faction sub addplayer", "fac sub addplayer", "team subclaim grant", "t subclaim grant", "f subclaim grant", "faction subclaim grant", "fac subclaim grant", "team sub grant", "t sub grant", "f sub grant", "faction sub grant", "fac sub grant" }, permission="")
    public static void teamSubclaimAddPlayer(Player sender, @cc.fyre.proton.command.param.Parameter(name="subclaim") Subclaim subclaim, @cc.fyre.proton.command.param.Parameter(name="player") UUID player) {
        if (HCF.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        Team team = HCF.getInstance().getTeamHandler().getTeam(sender);

        if (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId()) && !team.isCaptain(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only the team captains can do this.");
            return;
        }

        if (!team.isMember(player)) {
            sender.sendMessage(ChatColor.RED + UUIDUtils.name(player) + " is not on your team!");
            return;
        }

        if (subclaim.isMember(player)) {
            sender.sendMessage(ChatColor.RED + "The player already has access to that subclaim!");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + UUIDUtils.name(player) + ChatColor.YELLOW + " has been added to the subclaim " + ChatColor.GREEN + subclaim.getName() + ChatColor.YELLOW + ".");
        subclaim.addMember(player);
        team.flagForSave();
    }

}