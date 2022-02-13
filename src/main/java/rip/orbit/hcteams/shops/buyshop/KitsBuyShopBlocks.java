package rip.orbit.hcteams.shops.buyshop;

import org.bukkit.inventory.ItemStack;
import rip.orbit.hcteams.util.item.InventoryUtils;

public enum KitsBuyShopBlocks {

//    // first row
    CROWBAR(new ItemStack(InventoryUtils.CROWBAR), 1, 25000, 14);
//    Gapple(new ItemStack(Material.GOLDEN_APPLE), 1, 25000, 13);

    private final ItemStack item;
    private final int amount;
    private final double price;
    private final int slot;

    KitsBuyShopBlocks(ItemStack item, int amount, double price, int slot) {
        this.item = item;
        this.slot = slot;
        this.amount = amount;
        this.price = price;
    }

}

