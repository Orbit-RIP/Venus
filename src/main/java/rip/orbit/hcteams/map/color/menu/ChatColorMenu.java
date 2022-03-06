package rip.orbit.hcteams.map.color.menu;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.orbit.nebula.util.CC;

import java.util.*;

/**
 * Created by PVPTUTORIAL | Created on 05/05/2020
 */

@AllArgsConstructor
public class ChatColorMenu extends Menu {
    private UUID uuid;

    @Override
    public String getTitle(Player player) {
        return CC.translate("&6Select Color");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<ChatColor> colours = Arrays.asList(ChatColor.RED, ChatColor.YELLOW, ChatColor.AQUA, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.GOLD, ChatColor.WHITE);

        int size = (int) (Math.ceil(colours.size() / 9d) * 9) + 18;

        for (int i = 0; i < 9; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)7, "&e"));
        }

        for (int i = (size - 9); i < size; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)7, "&e"));
        }

        int i = 9;
        for (ChatColor chatColor : colours) {
            i++;
            buttons.put(i , new ChatColorButton(chatColor));
        }

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

}
