package net.frozenorb.foxtrot.commands;

import java.util.Map;
import java.util.UUID;

import cc.fyre.proton.Proton;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.util.UUIDUtils;

public class EcoCheckCommand {

    @Command(names={ "eco check" }, permission="op", hidden = true)
    public static void ecoCheck(Player sender) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }

        for (Team team : Foxtrot.getInstance().getTeamHandler().getTeams()) {
            if (isBad(team.getBalance())) {
                sender.sendMessage(ChatColor.YELLOW + "Team: " + ChatColor.WHITE + team.getName());
            }
        }

        try {
            Map<UUID, Double> balances = Proton.getInstance().getEconomyHandler().getBalances();

            for (Map.Entry<UUID, Double> balanceEntry  : balances.entrySet()) {
                if (isBad(balanceEntry.getValue())) {
                    sender.sendMessage(ChatColor.YELLOW + "Player: " + ChatColor.WHITE + UUIDUtils.name(balanceEntry.getKey()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isBad(double bal) {
        return (Double.isNaN(bal) || Double.isInfinite(bal) || bal > 1_000_000D);
    }

}