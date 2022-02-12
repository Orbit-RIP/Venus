package rip.orbit.hcteams.team.commands.team.subclaim;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.claims.LandBoard;
import rip.orbit.hcteams.team.claims.Subclaim;

public class TeamSubclaimUnclaimCommand {

    @Command(names={ "team subclaim unclaim", "t subclaim unclaim", "f subclaim unclaim", "faction subclaim unclaim", "fac subclaim unclaim", "team subclaim unsubclaim", "t subclaim unsubclaim", "f subclaim unsubclaim", "faction subclaim unsubclaim", "fac subclaim unsubclaim", "team unsubclaim", "t unsubclaim", "f unsubclaim", "faction unsubclaim", "fac unsubclaim"}, permission="")
    public static void teamSubclaimUnclaim(Player sender, @cc.fyre.proton.command.param.Parameter(name="subclaim", defaultValue="location") Subclaim subclaim) {
        if (HCF.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        Team team = HCF.getInstance().getTeamHandler().getTeam(sender);

        if (team.isOwner(sender.getUniqueId()) || team.isCoLeader(sender.getUniqueId()) || team.isCaptain(sender.getUniqueId())) {
            team.getSubclaims().remove(subclaim);
            LandBoard.getInstance().updateSubclaim(subclaim);
            team.flagForSave();
            sender.sendMessage(ChatColor.RED + "You have unclaimed the subclaim " + ChatColor.YELLOW + subclaim.getName() + ChatColor.RED + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "Only team captains can unclaim subclaims!");
        }
    }

}