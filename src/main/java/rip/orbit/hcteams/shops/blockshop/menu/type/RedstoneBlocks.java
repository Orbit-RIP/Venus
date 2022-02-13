package rip.orbit.hcteams.shops.blockshop.menu.type;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;



@Getter
public enum RedstoneBlocks {

    // First Row
    RESTONE_LAMP(new ItemStack(Material.REDSTONE_LAMP_OFF), 16, 200, 20),
    STICKY_PISTON(new ItemStack(Material.PISTON_STICKY_BASE), 16, 250, 21),
    DISPENSER(new ItemStack(Material.DISPENSER), 16, 250, 22),
    LEVER(new ItemStack(Material.LEVER), 16, 75, 23),
    REDSTONE_REPEATER(new ItemStack(Material.DIODE), 16, 125, 24),
    // LAST Row
    NOTE_BLOCK(new ItemStack(Material.NOTE_BLOCK), 16, 75, 29),
    PISTON(new ItemStack(Material.PISTON_BASE), 16, 150, 30),
    HOPPER(new ItemStack(Material.HOPPER), 16, 750, 31),
    TRIP_WIRE(new ItemStack(Material.TRIPWIRE_HOOK), 16, 100, 32),
    REDSTONE_COMPARATOR(new ItemStack(Material.REDSTONE_COMPARATOR), 16, 250, 33);

    private final ItemStack item;
    private final int amount;
    private final double price;
    private final int slot;

    RedstoneBlocks(ItemStack item, int amount, double price, int slot) {
        this.item = item;
        this.slot = slot;
        this.amount = amount;
        this.price = price;
    }
}