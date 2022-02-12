package rip.orbit.hcteams.team.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.team.Team;

public class SetTeamMeteorsCommand {

    @Command(names={ "setteammeteors", "setteammeteors" }, permission="foxtrot.setteambalance")
    public static void setTeamBalance(Player sender, @cc.fyre.proton.command.param.Parameter(name="team") Team team, @cc.fyre.proton.command.param.Parameter(name="meteors") int balance) {
        team.setMeteors(balance);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + "'s meteors is now " + ChatColor.LIGHT_PURPLE + team.getMeteors() + ChatColor.YELLOW + ".");
    }

}