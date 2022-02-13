package rip.orbit.hcteams.shops.sellshop;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;



@Getter
public enum SellShopBlocks {

    // First Row
    END_PORTAL(new ItemStack(Material.ENDER_PORTAL_FRAME), 1, 2000, 13),

    // Second Row
    LAPIS_BLOCK(new ItemStack(Material.LAPIS_BLOCK), 16, 2000, 21),
    DIAMOND_BLOCK(new ItemStack(Material.DIAMOND_BLOCK), 16, 2250, 22),
    IRON_BLOCK(new ItemStack(Material.IRON_BLOCK), 16, 1500, 23),

    // Second Row
    COAL_BLOCK(new ItemStack(Material.COAL_BLOCK), 16, 1250, 30),
    EMERALD_BLOCK(new ItemStack(Material.EMERALD_BLOCK), 16, 2500, 31),
    REDSTONE_BLOCK(new ItemStack(Material.REDSTONE_BLOCK), 16, 1250, 32),

    // LAST Row
    GOLD_BLOCK(new ItemStack(Material.GOLD_BLOCK), 16, 2000, 40);

    private final ItemStack item;
    private final int amount;
    private final double price;
    private final int slot;

    SellShopBlocks(ItemStack item, int amount, double price, int slot) {
        this.item = item;
        this.slot = slot;
        this.amount = amount;
        this.price = price;
    }
}
