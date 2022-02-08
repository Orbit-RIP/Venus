package net.frozenorb.foxtrot.shops.buyshop.menu;

import net.frozenorb.foxtrot.shops.blockshop.menu.type.RedstoneBlocks;
import net.frozenorb.foxtrot.shops.blockshop.menu.type.button.RedstonePurchaseButton;
import net.frozenorb.foxtrot.shops.buyshop.BuyShopBlocks;
import net.frozenorb.foxtrot.shops.buyshop.menu.button.BuyPurchaseButton;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BuyShopMenu extends Menu {

    private int[] SLOTS = new int[] {0, 1, 7, 8, 9, 17, 36, 44, 45,46, 52, 53};

    @Override
    public String getTitle(Player player) {
        return "Buy Shop";
    }

    {
        setAutoUpdate(true);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        for(int i : SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 5," "));
        }

        for(BuyShopBlocks buyShopBlocks : BuyShopBlocks.values()) {
            buttons.put(buyShopBlocks.getSlot(), new BuyPurchaseButton(player, buyShopBlocks));
        }

        return buttons;
    }
}