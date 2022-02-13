package rip.orbit.hcteams.shops.blockshop.command;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.shops.blockshop.menu.BlockShopMenu;

public class BlockShopCommand {

    @Command(names = {"blockshop", "shop"}, permission = "")
    public static void execute(Player player) {
        new BlockShopMenu().openMenu(player);
    }
}
