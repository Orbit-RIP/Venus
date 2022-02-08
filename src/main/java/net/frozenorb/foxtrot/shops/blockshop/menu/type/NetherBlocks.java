package net.frozenorb.foxtrot.shops.blockshop.menu.type;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaikoX
 */

@Getter
public enum NetherBlocks {

    // First Row
    NETHER_BRICK_STAIR(new ItemStack(Material.NETHER_BRICK_STAIRS), 16, 75, 20),
    QUARTZ_SLAB(new ItemStack(Material.STEP, 1, (short) 7), 16, 225, 21),
    GLOWSTONE(new ItemStack(Material.GLOWSTONE), 16, 350, 22),
    QUARTS_STAIR(new ItemStack(Material.QUARTZ_STAIRS), 16, 225, 23),
    NETHER_BRICK_SLAB(new ItemStack(Material.STEP, 1, (short) 6), 16, 75, 24),
    // LAST Row
    NETHER_BRICK(new ItemStack(Material.NETHER_BRICK), 16, 100, 29),
    CHISELED_QUARTZ_BLOCK(new ItemStack(Material.QUARTZ_BLOCK, 1, (short) 1), 16, 330, 30),
    QUARTZ_BLOCK(new ItemStack(Material.QUARTZ_BLOCK), 16, 275, 31),
    PILLER_QUARTZ_BLOCK(new ItemStack(Material.QUARTZ_BLOCK, 1, (short) 2), 16, 350, 32),
    NETHERRACK(new ItemStack(Material.NETHERRACK), 16, 75, 33);

    private final ItemStack item;
    private final int amount;
    private final double price;
    private final int slot;

    NetherBlocks(ItemStack item, int amount, double price, int slot) {
        this.item = item;
        this.slot = slot;
        this.amount = amount;
        this.price = price;
    }
}