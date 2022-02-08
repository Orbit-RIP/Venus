package net.frozenorb.foxtrot.commands;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

import cc.fyre.proton.Proton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;

public class BalanceCommand {

    @Command(names={ "Balance", "Money", "Bal" }, permission="")
    public static void balance(Player sender, @Parameter(name="player", defaultValue="self") UUID player) {
        if (sender.getUniqueId().equals(player)) {
            sender.sendMessage(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + NumberFormat.getNumberInstance(Locale.US).format(Proton.getInstance().getEconomyHandler().getBalance(sender.getUniqueId())));
        } else {
            sender.sendMessage(ChatColor.GOLD + "Balance of " + Proton.getInstance().getUuidCache().name(player) + ": " + ChatColor.WHITE + NumberFormat.getNumberInstance(Locale.US).format(Proton.getInstance().getEconomyHandler().getBalance(player)));
        }
    }

}