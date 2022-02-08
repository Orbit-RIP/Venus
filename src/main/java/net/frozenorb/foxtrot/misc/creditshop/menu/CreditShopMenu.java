package net.frozenorb.foxtrot.misc.creditshop.menu;

import net.frozenorb.foxtrot.misc.creditshop.CreditShop;
import net.frozenorb.foxtrot.misc.creditshop.menu.buttons.BalanceButton;
import net.frozenorb.foxtrot.misc.creditshop.menu.buttons.ProfileButton;
import net.frozenorb.foxtrot.misc.creditshop.menu.buttons.ItemButton;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CreditShopMenu extends Menu {

    private int[] RED_SLOTS = new int[] {0, 2, 4, 6, 8, 18, 26, 36, 44, 46, 48, 50, 52};

    private int[] GREEN_SLOTS = new int[] {1, 3, 5, 7, 9, 17, 27, 35, 45, 47, 49, 51, 53 };

    @Override
    public String getTitle(Player player) {
        return ChatColor.YELLOW + ChatColor.BOLD.toString() + "Token Shop";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Arrays.stream(CreditShop.values()).forEach(creditShop ->
                buttons.put(creditShop.getSlot(), new ItemButton(creditShop)));

        for(int i : RED_SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 14));
        }

        for(int i : GREEN_SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 5));
        }

        buttons.put(31, new BalanceButton());
        buttons.put(22, new ProfileButton());

        return buttons;
    }
}
