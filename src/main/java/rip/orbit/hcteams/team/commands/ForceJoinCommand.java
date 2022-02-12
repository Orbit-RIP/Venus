package rip.orbit.hcteams.team.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.team.Team;

public class ForceJoinCommand {

    @Command(names={ "ForceJoin" }, permission="foxtrot.forcejoin")
    public static void forceJoin(Player sender, @cc.fyre.proton.command.param.Parameter(name="team") Team team, @cc.fyre.proton.command.param.Parameter(name="player", defaultValue="self") Player player) {
        if (HCF.getInstance().getTeamHandler().getTeam(player) != null) {
            if (player == sender) {
                sender.sendMessage(ChatColor.RED + "Leave your current team before attempting to forcejoin.");
            } else {
                sender.sendMessage(ChatColor.RED + "That player needs to leave their current team first!");
            }

            return;
        }

        team.addMember(player.getUniqueId());
        HCF.getInstance().getTeamHandler().setTeam(player.getUniqueId(), team);
        player.sendMessage(ChatColor.YELLOW + "You are now a member of " + ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + "!");

        if (player != sender) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.YELLOW + " added to " + ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + "!");
        }
    }

}