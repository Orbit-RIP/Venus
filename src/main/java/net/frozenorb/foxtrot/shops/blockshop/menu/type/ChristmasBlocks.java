package net.frozenorb.foxtrot.shops.blockshop.menu.type;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaikoX
 */

@Getter
public enum ChristmasBlocks {

    SNOW_BLOCK(new ItemStack(Material.SNOW_BLOCK), 16, 500, 21),
    SNOW(new ItemStack(Material.SNOW), 16, 250, 30),
    ICE(new ItemStack(Material.ICE), 16, 500, 23),
    PACKED_ICE(new ItemStack(Material.PACKED_ICE), 16, 500, 32);

    private final ItemStack item;
    private final int amount;
    private final double price;
    private final int slot;

    ChristmasBlocks(ItemStack item, int amount, double price, int slot) {
        this.item = item;
        this.slot = slot;
        this.amount = amount;
        this.price = price;
    }
}
