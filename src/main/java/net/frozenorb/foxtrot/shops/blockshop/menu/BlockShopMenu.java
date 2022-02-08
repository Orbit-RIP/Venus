package net.frozenorb.foxtrot.shops.blockshop.menu;

import net.frozenorb.foxtrot.shops.blockshop.menu.button.*;
import net.frozenorb.foxtrot.shops.blockshop.menu.button.StainedGlassButton;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by MaikoX
 */

public class BlockShopMenu extends Menu {

    private int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 49, 51, 52, 53};

    @Override
    public String getTitle(Player player) {
        return "Block Shop";
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        for(int i : SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 1," "));
        }

        buttons.put(21, new NetherButton());
        buttons.put(22, new ChristmasButton());
        buttons.put(23, new RedstoneButton());
        buttons.put(30, new BuildingButton());
        buttons.put(31, new StainedClayButton());
        buttons.put(32, new StainedGlassButton());
        buttons.put(48, new BuyShopButton());
        buttons.put(50, new SellShopButton());
        return buttons;
    }
}
