package rip.orbit.hcteams.team.commands.team;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.team.Team;

public class TeamMaxOnlineCommand {

    @Command(names={ "team maxOnline" }, permission="op")
    public static void teamMaxOnline(Player sender, @cc.fyre.proton.command.param.Parameter(name="team") Team team, @cc.fyre.proton.command.param.Parameter(name="max online", defaultValue="-5") int maxOnline) {
        if (maxOnline == -5) {
            if (team.getMaxOnline() == -1) {
                sender.sendMessage(team.getName(sender) + ChatColor.YELLOW + "'s player limit is " + ChatColor.GREEN + "not set" + ChatColor.YELLOW + ".");
            } else {
                sender.sendMessage(team.getName(sender) + ChatColor.YELLOW + "'s player limit is " + ChatColor.RED + team.getMaxOnline() + ChatColor.YELLOW + ".");
            }
        } else {
            team.setMaxOnline(maxOnline);
            sender.sendMessage(team.getName(sender) + ChatColor.YELLOW + "'s player limit has been set to " + ChatColor.RED + maxOnline + ChatColor.YELLOW + ".");
        }
    }

}