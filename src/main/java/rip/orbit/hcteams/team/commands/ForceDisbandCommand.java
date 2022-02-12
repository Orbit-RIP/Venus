package rip.orbit.hcteams.team.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.team.Team;

public class ForceDisbandCommand {

    @Command(names={ "forcedisband" }, permission="foxtrot.forcedisband")
    public static void forceDisband(Player sender, @cc.fyre.proton.command.param.Parameter(name="team") Team team) {
        team.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + sender.getName() + " has force disbanded the team.");
        team.disband();
        sender.sendMessage(ChatColor.YELLOW + "Force disbanded the team " + ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + ".");
    }

}