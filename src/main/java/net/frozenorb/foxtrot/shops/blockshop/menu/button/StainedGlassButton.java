package net.frozenorb.foxtrot.shops.blockshop.menu.button;

import net.frozenorb.foxtrot.shops.blockshop.menu.StainedGlassMenu;
import cc.fyre.proton.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class StainedGlassButton extends Button {

    @Override
    public String getName(Player player) {
        return ChatColor.GRAY + "Stained Glass Blocks";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.STAINED_GLASS;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        new StainedGlassMenu().openMenu(player);
    }
}
