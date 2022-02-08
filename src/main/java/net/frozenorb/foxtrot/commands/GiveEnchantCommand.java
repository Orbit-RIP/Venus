package net.frozenorb.foxtrot.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GiveEnchantCommand {

    @Command(names = {"giveenchant"}, permission = "op")
    public static void chatcolor(CommandSender sender, @Parameter(name="player") Player target, @Parameter(name="piece") String piece, @Parameter(name = "type") String type) {
        ArrayList<String> helmlore = new ArrayList<>();
        ArrayList<String> chestlore = new ArrayList<>();
        ArrayList<String> legglore = new ArrayList<>();
        ArrayList<String> bootlore = new ArrayList<>();

        if (piece.equalsIgnoreCase("helmet")) {
            if (type.equalsIgnoreCase("bard")) {
                ItemStack helmet = new ItemStack(Material.GOLD_HELMET);
                ItemMeta meta2 = helmet.getItemMeta();

                helmlore.add(ChatColor.translateAlternateColorCodes('&', "&cHellforged IV"));
                helmlore.add(ChatColor.translateAlternateColorCodes('&', "&cImplants V"));
                helmlore.add(ChatColor.translateAlternateColorCodes('&', "&cFireResistance I"));
                helmlore.add(ChatColor.translateAlternateColorCodes('&', "&cRecover I"));
                helmlore.add(ChatColor.translateAlternateColorCodes('&', "&cMermaid III"));
                meta2.setLore(helmlore);

                meta2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                meta2.addEnchant(Enchantment.DURABILITY, 4, true);
                meta2.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, true);
                meta2.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
                meta2.addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true);
                helmet.setItemMeta(meta2);
                target.getInventory().addItem(helmet);
                return;
            } else {
                ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
                ItemMeta meta2 = helmet.getItemMeta();

                helmlore.add(ChatColor.translateAlternateColorCodes('&', "&cHellforged IV"));
                helmlore.add(ChatColor.translateAlternateColorCodes('&', "&cImplants V"));
                helmlore.add(ChatColor.translateAlternateColorCodes('&', "&cFireResistance I"));
                helmlore.add(ChatColor.translateAlternateColorCodes('&', "&cRecover I"));
                helmlore.add(ChatColor.translateAlternateColorCodes('&', "&cMermaid III"));
                meta2.setLore(helmlore);

                meta2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                meta2.addEnchant(Enchantment.DURABILITY, 4, true);
                meta2.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, true);
                meta2.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
                meta2.addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true);
                helmet.setItemMeta(meta2);
                target.getInventory().addItem(helmet);
                return;
            }
        }
        if (piece.equalsIgnoreCase("chestplate")) {
            if (type.equalsIgnoreCase("bard")) {
                ItemStack chestplate = new ItemStack(Material.GOLD_CHESTPLATE);
                ItemMeta meta3 = chestplate.getItemMeta();

                chestlore.add(ChatColor.translateAlternateColorCodes('&', "&cHellforged IV"));
                chestlore.add(ChatColor.translateAlternateColorCodes('&', "&cImplants V"));
                chestlore.add(ChatColor.translateAlternateColorCodes('&', "&cFireResistance I"));
                chestlore.add(ChatColor.translateAlternateColorCodes('&', "&cRecover I"));
                chestlore.add(ChatColor.translateAlternateColorCodes('&', "&cMermaid III"));
                meta3.setLore(chestlore);

                meta3.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                meta3.addEnchant(Enchantment.DURABILITY, 4, true);
                meta3.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, true);
                meta3.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
                meta3.addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true);
                chestplate.setItemMeta(meta3);
                target.getInventory().addItem(chestplate);
                return;
            } else {
                ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                ItemMeta meta3 = chestplate.getItemMeta();

                chestlore.add(ChatColor.translateAlternateColorCodes('&', "&cHellforged IV"));
                chestlore.add(ChatColor.translateAlternateColorCodes('&', "&cImplants V"));
                chestlore.add(ChatColor.translateAlternateColorCodes('&', "&cFireResistance I"));
                chestlore.add(ChatColor.translateAlternateColorCodes('&', "&cRecover I"));
                chestlore.add(ChatColor.translateAlternateColorCodes('&', "&cMermaid III"));
                meta3.setLore(chestlore);

                meta3.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                meta3.addEnchant(Enchantment.DURABILITY, 4, true);
                meta3.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, true);
                meta3.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
                meta3.addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true);
                chestplate.setItemMeta(meta3);
                target.getInventory().addItem(chestplate);
                return;
            }
        }
        if (piece.equalsIgnoreCase("leggings")) {
            if (type.equalsIgnoreCase("bard")) {
                ItemStack leggings = new ItemStack(Material.GOLD_LEGGINGS);
                ItemMeta meta4 = leggings.getItemMeta();
                meta4.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                meta4.addEnchant(Enchantment.DURABILITY, 4, true);
                meta4.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, true);
                meta4.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
                meta4.addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true);
                legglore.add(ChatColor.translateAlternateColorCodes('&', "&cHellforged IV"));
                legglore.add(ChatColor.translateAlternateColorCodes('&', "&cImplants V"));
                legglore.add(ChatColor.translateAlternateColorCodes('&', "&cFireResistance I"));
                legglore.add(ChatColor.translateAlternateColorCodes('&', "&cRecover I"));
                legglore.add(ChatColor.translateAlternateColorCodes('&', "&cMermaid III"));
                meta4.setLore(legglore);

                leggings.setItemMeta(meta4);
                target.getInventory().addItem(leggings);
                return;
            } else {
                ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                ItemMeta meta4 = leggings.getItemMeta();
                meta4.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                meta4.addEnchant(Enchantment.DURABILITY, 4, true);
                meta4.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, true);
                meta4.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
                meta4.addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true);
                legglore.add(ChatColor.translateAlternateColorCodes('&', "&cHellforged IV"));
                legglore.add(ChatColor.translateAlternateColorCodes('&', "&cImplants V"));
                legglore.add(ChatColor.translateAlternateColorCodes('&', "&cFireResistance I"));
                legglore.add(ChatColor.translateAlternateColorCodes('&', "&cRecover I"));
                legglore.add(ChatColor.translateAlternateColorCodes('&', "&cMermaid III"));
                meta4.setLore(legglore);

                leggings.setItemMeta(meta4);
                target.getInventory().addItem(leggings);
                return;
            }
        }
        if (piece.equalsIgnoreCase("boots")) {
            if (type.equalsIgnoreCase("bard")) {
                ItemStack boots = new ItemStack(Material.GOLD_BOOTS);
                ItemMeta meta5 = boots.getItemMeta();
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cHellforged IV"));
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cImplants V"));
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cFireResistance I"));
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cRecover I"));
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cMermaid III"));
                meta5.setLore(bootlore);

                meta5.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                meta5.addEnchant(Enchantment.PROTECTION_FALL, 4, true);
                meta5.addEnchant(Enchantment.DURABILITY, 4, true);
                meta5.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, true);
                meta5.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
                meta5.addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true);
                boots.setItemMeta(meta5);
                target.getInventory().addItem(boots);
            } else {
                ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
                ItemMeta meta5 = boots.getItemMeta();
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cHellforged IV"));
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cImplants V"));
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cFireResistance I"));
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cRecover I"));
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cMermaid III"));
                bootlore.add(ChatColor.translateAlternateColorCodes('&', "&cSpeed II"));
                meta5.setLore(bootlore);

                meta5.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                meta5.addEnchant(Enchantment.PROTECTION_FALL, 4, true);
                meta5.addEnchant(Enchantment.DURABILITY, 4, true);
                meta5.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, true);
                meta5.addEnchant(Enchantment.PROTECTION_FIRE, 4, true);
                meta5.addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true);
                boots.setItemMeta(meta5);

                target.getInventory().addItem(boots);
                return;
            }
        }
        return;
    }

}
