package rip.orbit.hcteams.shops.blockshop.menu.button;

import cc.fyre.proton.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.shops.blockshop.menu.RedstoneBlockMenu;

import java.util.ArrayList;
import java.util.List;

public class RedstoneButton extends Button {

    @Override
    public String getName(Player player) {
        return ChatColor.GRAY + "Redstone Blocks";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.REDSTONE;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        new RedstoneBlockMenu().openMenu(player);
    }
}