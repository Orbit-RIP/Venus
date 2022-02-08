package net.frozenorb.foxtrot.commands;

import com.comphenix.protocol.PacketType;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class StackCommand {

    @Command(names = {"stack", "more"}, permission = "foxtrot.stack")
    public static void stack(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        int done = 0;
        for(int i = 0; i < contents.length; i++) {
            ItemStack current = contents[i];
            if (current != null) {
                for (int i2 = i + 1; i2 < contents.length; i2++) {
                    ItemStack current2 = contents[i2];
                    if (current.isSimilar(current2)) {
                        int allowed = current.getMaxStackSize() - current.getAmount();
                        if(allowed > 0) {
                            int left = current2.getAmount() - allowed;
                            if (left > 0) {
                                current2.setAmount(left);
                                current.setAmount(current.getMaxStackSize());
                            } else {
                                done++;
                                current.setAmount(current.getAmount() + current2.getAmount());
                                contents[i2] = null;
                            }
                        }
                    }
                }
            }
        }
        inventory.setContents(contents);
        player.updateInventory();
        player.sendMessage(done == 0 ? CC.RED + "You have no items to stack" : CC.WHITE + "You've stacked " + done + " item" + (done != 1 ? "s" : ""));
    }
}


