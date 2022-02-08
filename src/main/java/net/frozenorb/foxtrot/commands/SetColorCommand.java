package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.ColorUtil;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by PVPTUTORIAL | Created on 03/05/2020
 */

//public class SetColorCommand {
//    public static List<Material> list = Arrays.asList(Material.GLASS, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE, Material.THIN_GLASS, Material.HARD_CLAY, Material.STAINED_CLAY, Material.WOOL, Material.CARPET);
//
//    @Command(names={ "setcolor" }, permission="foxtrot.setcolor")
//    public static void setColor(Player player, @Parameter(name = "color") String string) {
//        if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) {
//            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
//                    "&cYou are not holding a colorable item."));
//            return;
//        }
//
//        if (string.equalsIgnoreCase("reset")) {
//            if (player.getItemInHand().getType() == Material.STAINED_CLAY) {
//                player.getItemInHand().setType(Material.HARD_CLAY);
//            } else if (player.getItemInHand().getType() == Material.STAINED_GLASS) {
//                player.getItemInHand().setType(Material.GLASS);
//            } else if (player.getItemInHand().getType() == Material.STAINED_GLASS_PANE) {
//                player.getItemInHand().setType(Material.THIN_GLASS);
//            }
//
//            player.getItemInHand().setDurability((short) 0);
//            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
//                    " &6» &fThe color of your §b" + ItemUtils.getName(player.getItemInHand())
//                            + " §fhas been reset."));
//            return;
//        }
//
//        if (getColor(string) == null) {
//            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
//                    "&cThe color you provided is invalid."));
//            return;
//        }
//
//        if (list.contains(player.getItemInHand().getType())) {
//            if (player.getItemInHand().getType() == Material.HARD_CLAY) {
//                player.getItemInHand().setType(Material.STAINED_CLAY);
//            } else if (player.getItemInHand().getType() == Material.GLASS) {
//                player.getItemInHand().setType(Material.STAINED_GLASS);
//            } else if (player.getItemInHand().getType() == Material.THIN_GLASS) {
//                player.getItemInHand().setType(Material.STAINED_GLASS_PANE);
//            }
//
//            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
//                    " &6» &fThe color of your §b" + ItemUtils.getName(player.getItemInHand())
//                            + " &fhas been set to &f" + getColor(string).name() + "&f."));
//            player.getItemInHand().setDurability(getColor(string).getData());
//            return;
//        }
//
//        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
//                "&cThe item you are holding cannot be colored."));
//    }
//
//    public static DyeColor getColor(final String s) {
//        DyeColor c;
//        try {
//            c = DyeColor.valueOf(s.toUpperCase());
//        } catch (Exception e) {
//            c = null;
//        }
//        return c;
//    }
//}
