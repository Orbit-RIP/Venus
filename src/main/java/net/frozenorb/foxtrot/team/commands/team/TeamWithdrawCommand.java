package net.frozenorb.foxtrot.team.commands.team;

import cc.fyre.proton.Proton;
import com.google.common.collect.ImmutableMap;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.track.TeamActionTracker;
import net.frozenorb.foxtrot.team.track.TeamActionType;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamWithdrawCommand {

    @Command(names={ "team withdraw", "t withdraw", "f withdraw", "faction withdraw", "fac withdraw", "team w", "t w", "f w", "faction w", "fac w" }, permission="")
    public static void teamWithdraw(Player sender, @Parameter(name = "amount") float amount) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.isCaptain(sender.getUniqueId()) || team.isCoLeader(sender.getUniqueId()) || team.isOwner(sender.getUniqueId())) {
            if (team.getBalance() < amount) {
                sender.sendMessage(ChatColor.RED + "The team doesn't have enough money to do this!");
                return;
            }

            if (Double.isNaN(team.getBalance())) {
                sender.sendMessage(ChatColor.RED + "You cannot withdraw money because your team's balance is broken!");
                return;
            }

            if (amount <= 0) {
                sender.sendMessage(ChatColor.RED + "You can't withdraw $0.0 (or less)!");
                return;
            }

            if (amount == Float.NaN) {
                sender.sendMessage(ChatColor.RED + "Nope.");
                return;
            }

            Proton.getInstance().getEconomyHandler().deposit(sender.getUniqueId(), amount);
            sender.sendMessage(ChatColor.YELLOW + "You have withdrawn " + ChatColor.LIGHT_PURPLE + amount + ChatColor.YELLOW + " from the team balance!");

            TeamActionTracker.logActionAsync(team, TeamActionType.PLAYER_WITHDRAW_MONEY, ImmutableMap.of(
                    "playerId", sender.getUniqueId(),
                    "playerName", sender.getName(),
                    "amount", amount,
                    "oldBalance", team.getBalance(),
                    "newBalance", team.getBalance() - amount
            ));

            team.setBalance(team.getBalance() - amount);
            team.sendMessage(ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.YELLOW + " withdrew " + ChatColor.LIGHT_PURPLE + "$" + amount + ChatColor.YELLOW + " from the team balance.");
        } else {
            sender.sendMessage(ChatColor.GOLD + "Only team captains can do this.");
        }
    }

}