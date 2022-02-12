package rip.orbit.hcteams.team.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.team.Team;

public class SetTeamBalanceCommand {

    @Command(names={ "setteambalance", "setteambal" }, permission="foxtrot.setteambalance")
    public static void setTeamBalance(Player sender, @cc.fyre.proton.command.param.Parameter(name="team") Team team, @cc.fyre.proton.command.param.Parameter(name="balance") float balance) {
        team.setBalance(balance);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + "'s balance is now " + ChatColor.LIGHT_PURPLE + team.getBalance() + ChatColor.YELLOW + ".");
    }

}