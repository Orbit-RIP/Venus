    //package net.frozenorb.foxtrot.commands;
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.util.ColorUtil;
//import cc.fyre.proton.command.Command;
//import cc.fyre.proton.nametag.FrozenNametagHandler;
//import cc.fyre.proton.util.ItemBuilder;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.metadata.FixedMetadataValue;
//
//public class VanishCommand {
//
//
//    @Command(names={ "vanish", "v" }, permission="hcteams.command.vanish")
//    public static void vanish(Player player){
//        if (player.hasMetadata("vanished")){
//            player.removeMetadata("vanished", Foxtrot.getInstance());
//            player.sendMessage(ColorUtil.format("&6Vanish&7: &cDisabled"));
//            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
//                online.showPlayer(player);
//            }
//            if (player.hasMetadata("staffmode")){
//                player.getInventory().setItem(8, ItemBuilder.of(Material.INK_SACK).data((short) 8).name(ChatColor.YELLOW + "Become Invisible").build());
//                player.updateInventory();
//            }
//            FrozenNametagHandler.reloadPlayer(player);
//
//        } else if (!player.hasMetadata("vanished")){
//            player.setMetadata("vanished", new FixedMetadataValue(Foxtrot.getInstance(), "vanished"));
//            player.sendMessage(ColorUtil.format("&6Vanish&7: &aEnabled"));
//            if (player.hasMetadata("staffmode")){
//                player.getInventory().setItem(8, ItemBuilder.of(Material.INK_SACK).data((short) 10).name(ChatColor.YELLOW + "Become Visible").build());
//                player.updateInventory();
//            }
//            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
//                if (!online.hasPermission("neutron.staff") || online.hasMetadata("hidestaff")) {
//                    online.hidePlayer(player);
//                }
//            }
//            FrozenNametagHandler.reloadPlayer(player);
//
//
//        }
//
//    }
//}