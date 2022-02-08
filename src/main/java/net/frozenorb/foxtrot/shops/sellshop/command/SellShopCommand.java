package net.frozenorb.foxtrot.shops.sellshop.command;

import net.frozenorb.foxtrot.shops.sellshop.menu.SellShopMenu;
import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by MaikoX
 */

public class SellShopCommand {

    @Command(names = {"sellshop"}, permission = "")
    public static void execute(Player player) {
        new SellShopMenu().openMenu(player);
    }
}