package net.frozenorb.foxtrot.shops.blockshop.menu.type;

import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaikoX
 */

@Getter
public enum BuildingBlocks {

    // First Row
    LEAVES_1(new ItemStack(Material.LEAVES_2, 1, (short) 1), 16, 200, 11),
    LEAVES_2(new ItemStack(Material.LEAVES, 1, (short) 1), 16, 200 ,12),
    LEAVES_3(new ItemStack(Material.LEAVES, 1, (short) 3), 16, 200, 13),
    LEAVES_4(new ItemStack(Material.LEAVES), 16, 200, 14),
    LEAVES_5(new ItemStack(Material.LEAVES, 1, (short) 2), 16, 200, 15),
    // Second Row
    ICE(new ItemStack(Material.ICE), 16, 125, 20),
    VINES(new ItemStack(Material.VINE), 16, 150, 21),
    LEAVES_6(new ItemStack(Material.LEAVES_2), 16, 200, 22),
    PAD(new ItemStack(Material.WATER_LILY), 16, 125, 23),
    PAKCED_ICE(new ItemStack(Material.PACKED_ICE), 16, 160, 24),
    // Third Row
    SNOW_BLOCK(new ItemStack(Material.SNOW_BLOCK), 16, 225, 29),
    STONE(new ItemStack(Material.STONE), 16, 150, 30),
    STONE_BRICK(new ItemStack(Material.SMOOTH_BRICK), 16, 400, 31),
    COBBLE_STONE(new ItemStack(Material.COBBLESTONE), 16, 100, 32),
    SNOW(new ItemStack(Material.SNOW), 16, 195, 33),
    //LAST ROW
    CHISELED_SANDSTONE(new ItemStack(Material.SANDSTONE, 1, (short) 1), 16, 650, 38),
    SMOOTH_SAND(new ItemStack(Material.SANDSTONE, 1, (short) 2), 16, 650, 39),
    END_STONE(new ItemStack(Material.ENDER_STONE), 16, 5000, 40),
    SANDSTONE(new ItemStack(Material.SANDSTONE), 16, 600, 41),
    BRICK(new ItemStack(Material.BRICK), 16, 550, 42);

    private final ItemStack item;
    private final int amount;
    private final double price;
    private final int slot;

    BuildingBlocks(ItemStack item, int amount, double price, int slot) {
        this.item = item;
        this.slot = slot;
        this.amount = amount;
        this.price = price;
    }
}