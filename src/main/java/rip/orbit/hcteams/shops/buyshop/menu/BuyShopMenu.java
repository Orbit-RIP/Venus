package rip.orbit.hcteams.shops.buyshop.menu;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.shops.buyshop.HCFBuyShopBlocks;
import rip.orbit.hcteams.shops.buyshop.menu.button.HCFBuyPurchaseButton;

import java.util.HashMap;
import java.util.Map;

public class BuyShopMenu extends Menu {

    private int[] SLOTS = new int[]{0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53};

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
        for (int i : SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 5, " "));
        }

//        if (HCF.getInstance().getMapHandler().isKitMap()) {
//            for (KitsBuyShopBlocks kitsBuyShopBlocks : KitsBuyShopBlocks.values()) {
//                buttons.put(kitsBuyShopBlocks.getSlot(), new KitsBuyPurchaseButton(player, kitsBuyShopBlocks));
//            }

//        } else
            for (HCFBuyShopBlocks buyShopBlocks : HCFBuyShopBlocks.values()) {
                buttons.put(buyShopBlocks.getSlot(), new HCFBuyPurchaseButton(player, buyShopBlocks));
            }

        return buttons;
    }
}