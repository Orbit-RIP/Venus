package rip.orbit.hcteams.shops.blockshop.menu.type;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;



@Getter
public enum StainedGlassBlocks {

    // First Row
    MAGENTA(new ItemStack(Material.STAINED_GLASS, 1, (short) 2), 16, 300, 12),
    LIGHT_BLUE(new ItemStack(Material.STAINED_GLASS, 1, (short) 3), 16, 300, 13),
    YELLOW(new ItemStack(Material.STAINED_GLASS, 1, (short) 4), 16, 300, 14),
    // Second Row
    ORANGE(new ItemStack(Material.STAINED_GLASS, 1, (short) 1), 16, 300, 20),
    PINK(new ItemStack(Material.STAINED_GLASS, 1, (short) 6), 16, 300, 21),
    LIGHT_GRAY(new ItemStack(Material.STAINED_GLASS, 1, (short) 8), 16, 300, 22),
    GRAY(new ItemStack(Material.STAINED_GLASS, 1, (short) 1), 16, 300, 23),
    WHITE(new ItemStack(Material.STAINED_GLASS, 1, (short) 1), 16, 300, 24),
    // Third Row
    CYAN(new ItemStack(Material.STAINED_GLASS, 1, (short) 9), 16, 300, 29),
    BLUE(new ItemStack(Material.STAINED_GLASS, 1, (short) 11), 16, 300, 30),
    PURPLE(new ItemStack(Material.STAINED_GLASS, 1, (short) 10), 16, 300, 31),
    GREEN(new ItemStack(Material.STAINED_GLASS, 1, (short) 13), 16, 300, 32),
    BROWN(new ItemStack(Material.STAINED_GLASS, 1, (short) 12), 16, 300, 33),
    // LAST Row
    RED(new ItemStack(Material.STAINED_GLASS, 1, (short) 14), 16, 300, 39),
    LIME(new ItemStack(Material.STAINED_GLASS, 1, (short) 5), 16, 300, 40),
    BLACK(new ItemStack(Material.STAINED_GLASS, 1, (short) 15), 16, 300, 41);

    private final ItemStack item;
    private final int amount;
    private final double price;
    private final int slot;

    StainedGlassBlocks(ItemStack item, int amount, double price, int slot) {
        this.item = item;
        this.slot = slot;
        this.amount = amount;
        this.price = price;
    }
}