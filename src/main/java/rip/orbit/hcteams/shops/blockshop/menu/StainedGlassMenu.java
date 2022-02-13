package rip.orbit.hcteams.shops.blockshop.menu;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.shops.blockshop.menu.type.StainedGlassBlocks;
import rip.orbit.hcteams.shops.blockshop.menu.type.button.StainedGlassPurchaseButton;

import java.util.HashMap;
import java.util.Map;



public class StainedGlassMenu extends Menu {

    private int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};

    @Override
    public String getTitle(Player player) {
        return "Stained Glass Blocks";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        for(int i : SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 6," "));
        }

        for(StainedGlassBlocks stainedGlassBlocks : StainedGlassBlocks.values()) {
            buttons.put(stainedGlassBlocks.getSlot(), new StainedGlassPurchaseButton(player, stainedGlassBlocks));
        }
        return buttons;
    }
}
