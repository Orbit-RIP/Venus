package net.frozenorb.foxtrot.team.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ForceJoinCommand {

    @Command(names={ "ForceJoin" }, permission="foxtrot.forcejoin")
    public static void forceJoin(Player sender, @Parameter(name="team") Team team, @Parameter(name="player", defaultValue="self") Player player) {
        if (Foxtrot.getInstance().getTeamHandler().getTeam(player) != null) {
            if (player == sender) {
                sender.sendMessage(ChatColor.RED + "Leave your current team before attempting to forcejoin.");
            } else {
                sender.sendMessage(ChatColor.RED + "That player needs to leave their current team first!");
            }

            return;
        }

        team.addMember(player.getUniqueId());
        Foxtrot.getInstance().getTeamHandler().setTeam(player.getUniqueId(), team);
        player.sendMessage(ChatColor.DARK_AQUA + "You are now a member of " + team.getName() + "!");

        if (player != sender) {
            sender.sendMessage(ChatColor.DARK_AQUA + player.getName() + " added to " + team.getName() + "!");
        }
    }

}