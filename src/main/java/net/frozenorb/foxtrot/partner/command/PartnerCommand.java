package net.frozenorb.foxtrot.partner.command;

import net.frozenorb.foxtrot.util.InventoryUtils;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by MaikoX
 */

public class PartnerCommand {

    @Command(names = {"package"}, permission = "fotrox.abilitypackage")
    public static void partnerGive(CommandSender sender, @Parameter(name = "player") Player player, @Parameter(name = "amount") int amount) {
        if (amount < 0) {
            sender.sendMessage(ChatColor.RED + "Enter a positive amount.");
            return;
        }

        player.getInventory().addItem(InventoryUtils.packageItem(amount));
        sender.sendMessage(player.getDisplayName() + ChatColor.GOLD + " has received " + ChatColor.GREEN + ChatColor.BOLD + amount + "x Ability Package" + ChatColor.GOLD + ".");
        player.sendMessage(ChatColor.GOLD + "You have been given a " + ChatColor.GREEN + ChatColor.BOLD + "Ability Package");
    }

    @Command(names = {"package giveall"}, permission = "fotrox.abilitypackage")
    public static void partnerGiveAlL(CommandSender sender, @Parameter(name = "amount") int amount) {
        for(Player online : Bukkit.getOnlinePlayers()) {
            online.getInventory().addItem(InventoryUtils.packageItem(amount));
        }
        sender.sendMessage(ChatColor.GOLD + "You have given everyone " + ChatColor.GREEN + ChatColor.BOLD + amount + "x Ability Package");
    }

}
