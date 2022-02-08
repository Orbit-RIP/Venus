package net.frozenorb.foxtrot.shops.buyshop.command;

import net.frozenorb.foxtrot.shops.buyshop.menu.BuyShopMenu;
import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by MaikoX
 */

public class BuyShopCommand {

    @Command(names = {"buyshop"}, permission = "")
    public static void execute(Player player) {
        new BuyShopMenu().openMenu(player);
    }
}