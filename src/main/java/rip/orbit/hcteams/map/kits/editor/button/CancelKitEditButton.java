package rip.orbit.hcteams.map.kits.editor.button;

import cc.fyre.proton.menu.Button;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.map.kits.editor.menu.KitsMenu;
import rip.orbit.hcteams.util.item.InventoryUtils;

import java.util.List;

@AllArgsConstructor
final class CancelKitEditButton extends Button {

    @Override
    public String getName(Player player) {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Cancel";
    }

    @Override
    public List<String> getDescription(Player player) {
        return ImmutableList.of(
                "",
                ChatColor.GRAY + "Click to " + ChatColor.RED + "abort" + ChatColor.GRAY + " editing your",
                ChatColor.GRAY + "kit and return to the kit menu!"
        );
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    @Override
    public byte getDamageValue(Player player) {
        return DyeColor.RED.getWoolData();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        InventoryUtils.resetInventoryDelayed(player);

        new KitsMenu().openMenu(player);
    }

}