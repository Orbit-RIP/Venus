package rip.orbit.hcteams.shops.buyshop.command;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.shops.buyshop.menu.BuyShopMenu;



public class BuyShopCommand {

    @Command(names = {"buyshop"}, permission = "")
    public static void execute(Player player) {
        new BuyShopMenu().openMenu(player);
    }
}