package rip.orbit.hcteams.shops.sellshop.menu;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.shops.sellshop.SellShopBlocks;
import rip.orbit.hcteams.shops.sellshop.menu.button.SellPurchaseButton;

import java.util.HashMap;
import java.util.Map;



public class SellShopMenu extends Menu {

    private int[] RED_SLOTS = new int[] {0, 2, 4, 6, 8, 18, 26, 27, 35, 45, 47, 49, 51, 53};

    private int[] BLACK_SLOTS = new int[] {1, 3, 5, 7, 9, 17, 36, 44, 46, 48, 50, 52};
    {
        setAutoUpdate(true);
    }
    @Override
    public String getTitle(Player player) {
        return "Sell Shop";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        for(int i : RED_SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 14," "));
        }

        for(int i : BLACK_SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 15," "));
        }


        for(SellShopBlocks sellShopBlocks : SellShopBlocks.values()) {
            buttons.put(sellShopBlocks.getSlot(), new SellPurchaseButton(player, sellShopBlocks));
        }

        return buttons;
    }
}
