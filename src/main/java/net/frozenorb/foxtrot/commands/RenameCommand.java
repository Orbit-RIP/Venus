package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.ItemBuilder;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class RenameCommand {

    @Command(names = { "rename" }, permission = "command.rename", description = "Rename your held item")
    public static void rename(Player sender, @Parameter(name = "name", wildcard = true) String name) {
        if (sender.getItemInHand() == null || sender.getItemInHand().getType().equals(Material.AIR)) {
            sender.sendMessage(ChatColor.RED + "You must be holding an item");
            return;
        }
        if (sender.getItemInHand().getType().equals(Material.TRIPWIRE_HOOK)) {
            sender.sendMessage(ChatColor.RED + "You cannot rename this item");
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "Renamed your " + ChatColor.WHITE + ItemUtils.getName(sender.getItemInHand()) + ChatColor.GOLD + " to " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', (name) + ChatColor.GOLD + "."));
        sender.setItemInHand(new ItemBuilder(sender.getItemInHand()).displayName(ChatColor.translateAlternateColorCodes('&', (name))).build());
    }
}
