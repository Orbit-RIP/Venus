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

public class TeamDepositCommand {

    @Command(names={ "team deposit", "t deposit", "f deposit", "faction deposit", "fac deposit", "team d", "t d", "f d", "faction d", "fac d", "team m d", "t m d", "f m d", "faction m d", "fac m d" }, permission="")
    public static void teamDeposit(Player sender, @Parameter(name="amount") float amount) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (amount <= 0 || Float.isNaN(amount)) {
            sender.sendMessage(ChatColor.RED + "You can't deposit $0.0 (or less)!");
            return;
        }

        if (Float.isNaN(amount)) {
            sender.sendMessage(ChatColor.RED + "Nope.");
            return;
        }

        if (Proton.getInstance().getEconomyHandler().getBalance(sender.getUniqueId()) < amount) {
            sender.sendMessage(ChatColor.RED + "You don't have enough money to do this!");
            return;
        }

        Proton.getInstance().getEconomyHandler().withdraw(sender.getUniqueId(), amount);

        sender.sendMessage(ChatColor.WHITE + "You have added " + ChatColor.YELLOW + amount + ChatColor.WHITE + " to the team balance!");

        TeamActionTracker.logActionAsync(team, TeamActionType.PLAYER_DEPOSIT_MONEY, ImmutableMap.of(
                "playerId", sender.getUniqueId(),
                "playerName", sender.getName(),
                "amount", amount,
                "oldBalance", team.getBalance(),
                "newBalance", team.getBalance() + amount
        ));

        team.setBalance(team.getBalance() + amount);
        team.sendMessage(ChatColor.WHITE + sender.getName() + " deposited " + ChatColor.YELLOW + amount + ChatColor.WHITE + " into the team balance.");

        Foxtrot.getInstance().getWrappedBalanceMap().setBalance(sender.getUniqueId(), Proton.getInstance().getEconomyHandler().getBalance(sender.getUniqueId()));
    }

    @Command(names={ "team deposit all", "t deposit all", "f deposit all", "faction deposit all", "fac deposit all", "team d all", "t d all", "f d all", "faction d all", "fac d all", "team m d all", "t m d all", "f m d all", "faction m d all", "fac m d all" }, permission="")
    public static void teamDepositAll(Player sender) {
        teamDeposit(sender, (float) Proton.getInstance().getEconomyHandler().getBalance(sender.getUniqueId()));
    }

}