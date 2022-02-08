//package net.frozenorb.foxtrot.listener;
//
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerChangedWorldEvent;
//import org.bukkit.event.player.PlayerJoinEvent;
//
//public class VanishListener implements Listener {
//    // ----------------------------------------------------------
//    @EventHandler
//    public void onPlayerJoinEvent(PlayerJoinEvent event) {
//        Player player = event.getPlayer();
//        for (Player players : Bukkit.getOnlinePlayers()) {
//            if (player.hasMetadata("vanished")) {
//                if (player.hasPermission("neutron.staff")) {
//                    player.showPlayer(players);
//                } else {
//                    player.hidePlayer(players);
//                }
//            }
//        }
//    }
//
//    // ----------------------------------------------------------
//    @EventHandler
//    public void onPlayerWorldChangeEvent(PlayerChangedWorldEvent event) {
//        Player player = event.getPlayer();
//
//        if (player.hasMetadata("vanished")) {
//            for (Player players : Bukkit.getOnlinePlayers()) {
//                if (player.hasPermission("neutron.staff")) {
//                    player.showPlayer(players);
//                } else {
//                    player.hidePlayer(players);
//                }
//            }
//        }
//    }
//}