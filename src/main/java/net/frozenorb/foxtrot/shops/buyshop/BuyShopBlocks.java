package net.frozenorb.foxtrot.shops.buyshop;

import lombok.Getter;
import net.frozenorb.foxtrot.util.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaikoX
 */


@Getter
public enum BuyShopBlocks {

    // Second Row
    BEACON(new ItemStack(Material.BEACON), 1, 30000, 12),
    XP(new ItemStack(Material.EXP_BOTTLE), 16, 5000, 13),
    CROWBAR(new ItemStack(InventoryUtils.CROWBAR), 1, 25000, 14),

    // Third Row
    FIRE_CHARGE(new ItemStack(Material.FIREBALL), 16, 500, 20),
    INK_SACK(new ItemStack(Material.INK_SACK), 16, 1000, 21),
    SLIME_BALL(new ItemStack(Material.SLIME_BALL), 16, 500, 22),
    COW_EGG(new ItemStack(Material.MONSTER_EGG, 1, (short) 92), 2, 100, 23),
    POTATO(new ItemStack(Material.POTATO), 16, 500, 24),

    // Fourth Row
    NETHER_WARTS(new ItemStack(Material.NETHER_STALK), 16, 500, 29),
    SPIDER_EYE(new ItemStack(Material.SPIDER_EYE), 16, 500, 30),
    END_PORTAL(new ItemStack(Material.ENDER_PORTAL_FRAME), 1, 5000, 31),
    GHAST(new ItemStack(Material.GHAST_TEAR), 16, 1000, 32),
    BLAZE_ROD(new ItemStack(Material.BLAZE_ROD), 16, 500, 33),

    // Fifth Row
    SUGAR_CANE(new ItemStack(Material.SUGAR_CANE), 16, 500, 38),
    CARROT(new ItemStack(Material.CARROT), 16, 500, 39),
    MELON_SEEDS(new ItemStack(Material.MELON_SEEDS), 16, 500, 40),
    FEATHER(new ItemStack(Material.FEATHER), 16, 500, 41),
    GLISTERING_MELON(new ItemStack(Material.SPECKLED_MELON), 16, 500, 42);

    // LAST Row

    private final ItemStack item;
    private final int amount;
    private final double price;
    private final int slot;

    BuyShopBlocks(ItemStack item, int amount, double price, int slot) {
        this.item = item;
        this.slot = slot;
        this.amount = amount;
        this.price = price;
    }
}
