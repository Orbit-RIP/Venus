package net.frozenorb.foxtrot.util;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBuilder {

    private ItemStack stack;
    private ItemMeta meta;

    /**
     * Creates a new instance with a given material
     * and a default quantity of 1.
     *
     * @Parameter material the material to create from
     */
    public ItemBuilder(Material material) {
        this(material, 1);
    }

    /**
     * Creates a new instance with a given material and quantity.
     *
     * @Parameter material the material to create from
     * @Parameter amount   the quantity to build with
     */
    public ItemBuilder(Material material, int amount) {
        this(material, amount, (byte) 0);
    }

    /**
     * Creates a new instance with a given {@link ItemStack}.
     *
     * @Parameter stack the stack to create from
     */
    public ItemBuilder(ItemStack stack) {
        Preconditions.checkNotNull(stack, "ItemStack cannot be null");
        this.stack = stack;
    }

    /**
     * Creates a new instance with a given material, quantity and data.
     *
     * @Parameter material the material to create from
     * @Parameter amount   the quantity to build with
     * @Parameter data     the data to build with
     */
    public ItemBuilder(Material material, int amount, byte data) {
        Preconditions.checkNotNull(material, "Material cannot be null");
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        this.stack = new ItemStack(material, amount, data);
    }


    /**
     * Sets the display name of this item builder.
     *
     * @Parameter name the display name to set
     * @return this instance
     */
    public ItemBuilder displayName(String name) {
        if (this.meta == null) {
            this.meta = stack.getItemMeta();
        }

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    /**
     * Adds a line to the lore of this builder at a specific position.
     *
     * @Parameter line the line to add
     * @return this instance
     */
    public ItemBuilder loreLine(String line) {
        if (this.meta == null) {
            this.meta = stack.getItemMeta();
        }

        boolean hasLore = meta.hasLore();
        List<String> lore = hasLore ? meta.getLore() : new ArrayList<>();
        lore.add(hasLore ? lore.size() : 0, line);

        this.lore(line);
        return this;
    }

    /**
     * Sets the lore of this item builder.
     *
     * @Parameter lore the lore varargs to set
     * @return this instance
     */
    public ItemBuilder lore(String... lore) {
        if (this.meta == null) {
            this.meta = stack.getItemMeta();
        }

        meta.setLore(Arrays.stream(lore)
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList()));
        return this;
    }

    /**
     * Adds an enchantment to this item builder.
     *
     * @Parameter enchantment the enchant to add
     * @Parameter level       the level to add at
     * @Parameter unsafe      if it should use unsafe calls
     * @return this instance
     */
    public ItemBuilder enchant(Enchantment enchantment, int level, boolean unsafe) {
        meta.addEnchant(enchantment, level, unsafe);
        return this;
    }

    /**
     * Sets the data of this item builder.
     *
     * @Parameter data the data value to set
     * @return the updated item builder
     */
    public ItemBuilder data(short data) {
        stack.setDurability(data);
        return this;
    }

    /**
     * Builds this into an {@link ItemStack}.
     *
     * @return the built {@link ItemStack}
     */
    public ItemStack build() {
        if (meta != null) {
            stack.setItemMeta(meta);
        }

        return stack;
    }
}
