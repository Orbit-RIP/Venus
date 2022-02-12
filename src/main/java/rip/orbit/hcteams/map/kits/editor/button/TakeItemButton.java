package rip.orbit.hcteams.map.kits.editor.button;

import cc.fyre.proton.menu.Button;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.orbit.hcteams.HCF;

import java.util.List;

final class TakeItemButton extends Button {

    private final ItemStack item;

    TakeItemButton(ItemStack item) {
        this.item = Preconditions.checkNotNull(item, "item");
    }

    // We just override this whole method, as we need to keep enchants/potion data/etc
    @Override
    public ItemStack getButtonItem(Player player) {
        return item;
    }

    @Override public String getName(Player player) {
        return null;
    }

    @Override public List<String> getDescription(Player player) {
        return null;
    }

    @Override public Material getMaterial(Player player) {
        return null;
    }

    @Override
    public void clicked(final Player player, final int slot, ClickType clickType) {
        // make the item show up again
        Bukkit.getScheduler().runTaskLater(HCF.getInstance(), () -> {
            player.getOpenInventory().getTopInventory().setItem(slot, item);
        }, 4L);
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return false;
    }

}