package rip.orbit.hcteams.map.color.menu;

import cc.fyre.proton.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.util.CC;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PVPTUTORIAL | Created on 05/05/2020
 */

@AllArgsConstructor
public class  ChatColorButton extends Button {
    private ChatColor chatColor;

    @Override
    public String getName(Player player) {
        return chatColor + "Select " + CC.getNameByChatColor(chatColor);
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> toReturn = new ArrayList<>();


        toReturn.add("&7&m---------------------------");
        toReturn.add("&fYou can &6left click &fto select " + chatColor + CC.getNameByChatColor(chatColor) + "&f.");
        toReturn.add("&7&m---------------------------");
        return CC.translate(toReturn);
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    @Override
    public byte getDamageValue(Player player) {
        return CC.getByChatColor(chatColor);
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (!player.hasPermission("color." + chatColor.name().replace("_", ""))) {
            player.sendMessage(ChatColor.RED + "No permission.");
            return;
        }

        player.closeInventory();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                " &6» &fYou have selected " + chatColor
                        + CC.getNameByChatColor(chatColor) + " &fas your chat color."));

        HCF.getInstance().getServerHandler().getChatColor().put(player.getUniqueId(), chatColor);
    }
}