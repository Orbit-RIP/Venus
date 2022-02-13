package rip.orbit.hcteams.shops.sellshop.command;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.shops.sellshop.menu.SellShopMenu;



public class SellShopCommand {

    @Command(names = {"sellshop"}, permission = "")
    public static void execute(Player player) {
        new SellShopMenu().openMenu(player);
    }
}