package net.frozenorb.foxtrot.misc.creditshop;

import lombok.Getter;
import net.frozenorb.foxtrot.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@Getter
public enum CreditShop {

    SNOWPORT(
            30,
            24,
            new ItemBuilder(Material.BONE).displayName("&E&LExotic Bone")
                    .enchant(Enchantment.DURABILITY, 10, true)
                    .loreLine("&7Hit a player 3 times to remove their ability to build.")
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f30"),
            "ability give %s SWAPPER_AXE 3"),

    CHRISTMAS_CRACKER(
            100,
            33,
            new ItemBuilder(Material.BLAZE_ROD).displayName("&a&lC&c&lH&a&lR&c&lI&a&lS&c&lT&a&lM&c&lA&a&lS &c&lC&a&lR&c&lA&a&lC&c&lK&a&lE&c&lR")
                    .enchant(Enchantment.DURABILITY, 10, true)
                    .loreLine("&7Crack open this item to receive some goodies!")
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f100"),
            "cracker %s 1"),
    ABILITY_PACKAGE(
            100,
            42,
            new ItemBuilder(Material.ENDER_CHEST).displayName("&a&lAbility Package")
                    .enchant(Enchantment.DURABILITY, 10, true)
                    .loreLine("&7Right click to open your package!")
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f100"),
            "package %s 1"),

    WINTER_KEYS(
            100,
            16,
            new ItemBuilder(Material.SNOW_BALL).displayName("&f&kI&b&lWINTER&f&kI&r &7Key")
                    .enchant(Enchantment.DURABILITY, 10, true)
                    .loreLine("&7Open this key in spawn!")
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f100"),
            "cr givekey %s winter 1"),
    PARTNER_KEYS(
            100,
            25,
            new ItemBuilder(Material.INK_SACK, 1, (byte) 12).displayName("&b&lPartner Key")
                    .enchant(Enchantment.DURABILITY, 10, true)
                    .loreLine("&7You can redeem this crate key")
                    .loreLine("&7using the NPC located in spawn")
                    .loreLine("&7")
                    .loreLine("&7Left click to view crate rewards.")
                    .loreLine("&7Right click to open the crate")
                    .loreLine("&7")
                    .loreLine("&fshop.vexor.cc")
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f100"),
            "cr givkey %s partner 1"),

    SWAPPER_AXE(
            30,
            15,
            new ItemBuilder(Material.GOLD_AXE).displayName("&6&lSwapper Axe")
                    .enchant(Enchantment.DURABILITY, 10, true)
                    .loreLine("&7Removes the opponents helmet")
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f30"),
            "ability give %s Swapper_Axe 1"),
    //
    SWORD(
            30,
            11,
            new ItemBuilder(Material.DIAMOND_SWORD).displayName("&a&lC&c&lh&a&lr&c&li&a&ls&c&lt&a&lm&c&la&a&ls &a&lS&c&lw&a&Lo&c&Lr&a&Ld")
                    .enchant(Enchantment.DURABILITY, 3, true)
                    .enchant(Enchantment.DAMAGE_ALL, 2, true)
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f30"),
//                    .enchant(Enchantment.FIRE_ASPECT, 2, true),
            ""),

    HELMET(
            30,
            10,
            new ItemBuilder(Material.DIAMOND_HELMET).displayName("&a&lC&c&lh&a&lr&c&li&a&ls&c&lt&a&lm&c&la&a&ls &a&lH&c&le&a&Ll&c&lm&a&le&c&lt")
                    .enchant(Enchantment.DURABILITY, 4, true)
                    .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true)
                    .enchant(Enchantment.WATER_WORKER, 3, true)
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f30"),
            ""),

    CHESTPLATE(
            30,
            19,
            new ItemBuilder(Material.DIAMOND_CHESTPLATE).displayName("&a&lC&c&lh&a&lr&c&li&a&ls&c&lt&a&lm&c&la&a&ls &a&lC&c&lh&a&le&c&Ls&a&lt&c&lp&a&Ll&c&La&a&lt&c&le")
                    .enchant(Enchantment.DURABILITY, 4, true)
                    .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true)
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f30"),
            ""),

    LEGGINGS(
            30,
            28,
            new ItemBuilder(Material.DIAMOND_LEGGINGS).displayName("&a&lC&c&lh&a&lr&c&li&a&ls&c&lt&a&lm&c&la&a&ls &a&LL&c&le&a&lg&c&lg&a&li&c&Ln&a&lgs")
                    .enchant(Enchantment.DURABILITY, 4, true)
                    .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true)
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f30"),
            ""),

    BOOTS(
            30,
            37,
            new ItemBuilder(Material.DIAMOND_BOOTS).displayName("&a&lC&c&lh&a&lr&c&li&a&ls&c&lt&a&lm&c&la&a&ls &a&lB&c&lo&a&lo&c&Lt&a&Ls")
                    .enchant(Enchantment.DURABILITY, 4, true)
                    .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true)
                    .enchant(Enchantment.PROTECTION_FALL, 4, true)
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f30"),
            ""),

    AXE(
            30,
            20,
            new ItemBuilder(Material.DIAMOND_AXE).displayName("&a&lC&c&lh&a&lr&c&li&a&ls&c&lt&a&lm&c&la&a&ls &c&lF&a&li&c&lr&a&le &c&LA&a&lx&c&le")
                    .enchant(Enchantment.DURABILITY, 3, true)
                    .enchant(Enchantment.DAMAGE_ALL, 1, true)
                    .enchant(Enchantment.FIRE_ASPECT, 2, true)
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f30"),
            ""),

    BOW(
            30,
            29,
            new ItemBuilder(Material.BOW).displayName("&a&lC&c&lh&a&lr&c&li&a&ls&c&lt&a&lm&c&la&a&ls &a&lB&c&lo&a&lw")
                    .enchant(Enchantment.DURABILITY, 3, true)
                    .enchant(Enchantment.DAMAGE_ALL, 1, true)
                    .enchant(Enchantment.FIRE_ASPECT, 2, true)
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f30"),
            ""),

    KNOCKBACK(
            50,
            38,
            new ItemBuilder(Material.SNOW_BALL).displayName("&a&lC&c&lh&a&lr&c&li&a&ls&c&lt&a&lm&c&la&a&ls &a&lK&c&ln&a&lo&c&lc&a&lk&c&lb&a&la&c&lc&a&lk")
                    .enchant(Enchantment.KNOCKBACK, 3, true)
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f50"),
            ""),
    TOP_RANK(
            750,
            34,
            new ItemBuilder(Material.ENCHANTED_BOOK, 1).displayName("&b&kI&f&lSNOWMAN&b&kI&r &f&lRank &d[&a7d&d]")
                    .enchant(Enchantment.DURABILITY, 10, true)
                    .loreLine("&7Right click this voucher to redeem &b&kI&f&lSNOWMAN&b&kI&r &f&lRank&7!")
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f750"),
            "setrank %s snowman 7d"),

    HERCULES_RANK(
            500,
            43,
            new ItemBuilder(Material.ENCHANTED_BOOK, 1).displayName("&e&lHercules Rank &7(&e7d&7)")
                    .enchant(Enchantment.DURABILITY, 10, true)
                    .loreLine("&7Right click this voucher to redeem &e&lHercules Rank&7!")
                    .loreLine("&7")
                    .loreLine("&ePrice &7- &f500"),
            "setrank %s hercules 7d");


    private final int price;
    private final int slot;
    private final ItemBuilder itemStack;
    private final String command;

    CreditShop(int price, int slot, ItemBuilder itemStack, String command) {
        this.price = price;
        this.slot = slot;
        this.itemStack = itemStack;
        this.command = command;
    }

    public ItemStack toItemStack() {
        return itemStack.build();
    }

    public static CreditShop getByStack(ItemStack itemStack) {
        return Arrays.stream(values())
                .filter(creditShop -> creditShop.toItemStack().isSimilar(itemStack))
                .findFirst()
                .orElse(null);
    }
}
