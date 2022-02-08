package net.frozenorb.foxtrot.shops.blockshop.menu.type;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaikoX
 */

@Getter
public enum StainedClayBlocks {

    // First Row
    LIGHT_GRAY(new ItemStack(Material.STAINED_CLAY, 1, (short) 8), 16, 500, 12),
    LIGHT_BLUE(new ItemStack(Material.STAINED_CLAY, 1, (short) 3), 16, 500, 13),
    LIGHT_CYAN(new ItemStack(Material.STAINED_CLAY, 1, (short) 9), 16, 500, 14),
    // Second Row
    PURPLE(new ItemStack(Material.STAINED_CLAY, 1, (short) 10), 16, 500, 20),
    BLUE(new ItemStack(Material.STAINED_CLAY, 1, (short) 11), 16, 500, 21),
    BROWN(new ItemStack(Material.STAINED_CLAY, 1, (short) 12), 16, 500, 22),
    GREEN(new ItemStack(Material.STAINED_CLAY, 1, (short) 13), 16, 500, 23),
    RED(new ItemStack(Material.STAINED_CLAY, 1, (short) 14), 16, 500, 24),
    // Third Row
    ORANGE(new ItemStack(Material.STAINED_CLAY, 1, (short) 1), 16, 500, 29),
    WHITE(new ItemStack(Material.STAINED_CLAY), 16, 500, 30),
    MAGENTA(new ItemStack(Material.STAINED_CLAY, 1, (short) 2), 16, 500, 31),
    GRAY(new ItemStack(Material.STAINED_CLAY, 1, (short) 7), 16, 500, 32),
    BLACK(new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 16, 500, 33),
    // LAST Row
    YELLOW(new ItemStack(Material.STAINED_CLAY, 1, (short) 4), 16, 500, 39),
    LIME(new ItemStack(Material.STAINED_CLAY, 1, (short) 5), 16, 500, 40),
    PINK(new ItemStack(Material.STAINED_CLAY, 1, (short) 6), 16, 500, 41);

    private final ItemStack item;
    private final int amount;
    private final double price;
    private final int slot;

    StainedClayBlocks(ItemStack item, int amount, double price, int slot) {
        this.item = item;
        this.slot = slot;
        this.amount = amount;
        this.price = price;
    }
}