package rip.orbit.hcteams.map.kits.editor.button;

import cc.fyre.proton.menu.Button;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.HCF;

import java.util.List;

final class ClearInventoryButton extends Button {

    @Override
    public String getName(Player player) {
        return ChatColor.BLUE.toString() + ChatColor.BOLD + "Clear Inventory";
    }

    @Override
    public List<String> getDescription(Player player) {
        return ImmutableList.of(
                "",
                ChatColor.GRAY + "Click to " + ChatColor.BLUE + "clear" + ChatColor.GRAY + " your editor inventory!"
        );
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    @Override
    public byte getDamageValue(Player player) {
        return DyeColor.BLUE.getWoolData();
    }

    @Override
    public void clicked(final Player player, int slot, ClickType clickType) {
        player.getInventory().clear();

        Bukkit.getScheduler().runTaskLater(HCF.getInstance(), player::updateInventory, 1L);
    }

}