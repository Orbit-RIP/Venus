package rip.orbit.hcteams.shops.blockshop.menu;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.shops.blockshop.menu.type.NetherBlocks;
import rip.orbit.hcteams.shops.blockshop.menu.type.button.NetherPurchaseButton;

import java.util.HashMap;
import java.util.Map;



public class NetherBlockMenu extends Menu {

    private int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};

    @Override
    public String getTitle(Player player) {
        return "Nether Blocks";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        for(int i : SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 10," "));
        }

        for(NetherBlocks netherBlocks : NetherBlocks.values()) {
            buttons.put(netherBlocks.getSlot(), new NetherPurchaseButton(player, netherBlocks));
        }
        return buttons;
    }
}
