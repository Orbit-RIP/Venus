package rip.orbit.hcteams.commands;

import cc.fyre.proton.Proton;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.economy.EconomyHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class BalanceCommand {

    @Command(names={ "Balance", "Econ", "Bal", "$" }, permission="")
    public static void balance(Player sender, @cc.fyre.proton.command.param.Parameter(name="player", defaultValue="self") UUID player) {
        if (sender.getUniqueId().equals(player)) {
            sender.sendMessage(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + NumberFormat.getNumberInstance(Locale.US).format(EconomyHandler.getBalance(sender.getUniqueId())));
        } else {
            sender.sendMessage(ChatColor.GOLD + "Balance of " + Proton.getInstance().getUuidCache().name(player) + ": " + ChatColor.WHITE + NumberFormat.getNumberInstance(Locale.US).format(EconomyHandler.getBalance(player)));
        }
    }

}