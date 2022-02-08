package net.frozenorb.foxtrot.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FixCommand {

    @Command(names={ "fix" }, permission="foxtrot.fix")
    public static void fix(Player sender) {
        ItemStack itemStack = sender.getItemInHand();

        if (itemStack == null
                || itemStack.getType() == null
                || itemStack.getType().isBlock()
                || itemStack.getDurability() == 0
                || itemStack.getType().getMaxDurability() < 1) {
            sender.sendMessage(ChatColor.RED + "This item cannot be repaired.");
            return;
        }

        itemStack.setDurability((short)0);
        sender.updateInventory();
        sender.sendMessage(ChatColor.GREEN + "Successfully repaired your item.");
    }
}
