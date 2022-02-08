package net.frozenorb.foxtrot.shops.blockshop.command;

import net.frozenorb.foxtrot.shops.blockshop.menu.BlockShopMenu;
import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;

public class BlockShopCommand {

    @Command(names = {"blockshop", "shop"}, permission = "")
    public static void execute(Player player) {
        new BlockShopMenu().openMenu(player);
    }
}
