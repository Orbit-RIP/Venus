package net.frozenorb.foxtrot.shops.blockshop.menu;

import net.frozenorb.foxtrot.shops.blockshop.menu.type.ChristmasBlocks;
import net.frozenorb.foxtrot.shops.blockshop.menu.type.button.ChristmasPurchaseButton;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MaikoX
 */

public class ChristmasBlockMenu extends Menu {

    private int[] RED_SLOTS = new int[] {0, 2, 4, 6, 8, 10, 16, 18, 26, 36, 44, 46, 48, 50, 52};

    private int[] GREEN_SLOTS = new int[] {1, 3, 5, 7, 9, 17, 27, 35, 37, 43, 45, 47, 49, 51, 53 };
//    private int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};

    @Override
    public String getTitle(Player player) {
        return "Christmas Blocks";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        for(int i : RED_SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 14));
        }

        for(int i : GREEN_SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 5));
        }

        for(ChristmasBlocks christmasBlocks : ChristmasBlocks.values()) {
            buttons.put(christmasBlocks.getSlot(), new ChristmasPurchaseButton(player, christmasBlocks));
        }
        return buttons;
    }
}
