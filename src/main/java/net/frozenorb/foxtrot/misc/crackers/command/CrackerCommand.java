package net.frozenorb.foxtrot.misc.crackers.command;

import net.frozenorb.foxtrot.util.InventoryUtils;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrackerCommand {

    @Command(names = {"cracker"}, permission = "fotrox.cracker")
    public static void crackerGive(CommandSender sender, @Parameter(name = "player") Player player, @Parameter(name = "amount") int amount) {
        if (amount < 0) {
            sender.sendMessage(ChatColor.RED + "Enter a positive amount.");
            return;
        }

        player.getInventory().addItem(InventoryUtils.crackerItem(amount));
        sender.sendMessage(player.getDisplayName() + ChatColor.GOLD + " has received " + ChatColor.GREEN + ChatColor.BOLD + amount + "x Christmas Cracker" + ChatColor.GOLD + ".");
        player.sendMessage(ChatColor.GOLD + "You have been given a " + ChatColor.GREEN + ChatColor.BOLD + "Christmas Cracker");
    }

    @Command(names = {"cracker giveall"}, permission = "fotrox.cracker")
    public static void crackerGiveAll(CommandSender sender, @Parameter(name = "amount") int amount) {
        for(Player online : Bukkit.getOnlinePlayers()) {
            online.getInventory().addItem(InventoryUtils.crackerItem(amount));
        }
        sender.sendMessage(ChatColor.GOLD + "You have given everyone " + ChatColor.GREEN + ChatColor.BOLD + amount + "x Christmas Cracker");
    }

}
