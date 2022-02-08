package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.commands.LastInvCommand;
import cc.fyre.proton.util.TimeUtils;

import java.util.UUID;

public class DeathbanListener implements Listener {

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        LastInvCommand.recordInventory(event.getEntity());

        EnderpearlCooldownHandler.getEnderpearlCooldown().remove(event.getEntity().getName()); // cancel enderpearls

        if (Foxtrot.getInstance().getMapHandler().isKitMap() || (event.getEntity().hasPermission("foxtrot.staff"))) {
            return;
        }

        int seconds = (int) Foxtrot.getInstance().getServerHandler().getDeathban(event.getEntity());
        Foxtrot.getInstance().getDeathbanMap().deathban(event.getEntity().getUniqueId(), seconds);

        final String time = TimeUtils.formatIntoDetailedString(seconds);

        new BukkitRunnable() {

            public void run() {
                if (!event.getEntity().isOnline()) {
                    return;
                }

                if (Foxtrot.getInstance().getServerHandler().isPreEOTW()) {
                    event.getEntity().kickPlayer(ChatColor.RED + "Thank you for playing! Come back next map!");
                } else {
                    event.getEntity().kickPlayer(ChatColor.RED + "You have been deathbanned for " + time + "!");
                }
            }

        }.runTaskLater(Foxtrot.getInstance(), 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean shouldBypass = player.hasPermission("foxtrot.staff");
        UUID playerUID = player.getUniqueId();


        Foxtrot.getInstance().getDeathbanMap().revive(playerUID);
        return;

//        if (shouldBypass) {
//            Foxtrot.getInstance().getDeathbanMap().revive(playerUID);
//            return;
//        }
//
//        if (Foxtrot.getInstance().getDeathbanMap().isDeathbanned(playerUID) && Foxtrot.getInstance().getServerHandler().isEOTW()) {
//            player.kickPlayer(ChatColor.RED + "You are deathbanned for the rest of the map!\nCheck out SOTW information at:\nhcrival.com/sotw");
//            return;
//        }
//
//        if (Foxtrot.getInstance().getSoulboundLivesMap().getLives(playerUID) == 0 && Foxtrot.getInstance().getDeathbanMap().isDeathbanned(playerUID)) {
//            int seconds = (int) Foxtrot.getInstance().getServerHandler().getDeathban(player);
//            final String time = TimeUtils.formatIntoDetailedString(seconds);
//            event.getPlayer().kickPlayer(ChatColor.RED + "You are still deathbanned for " + time + "! \nYou have 0 lives. \nPurchase lives at store.hcrival.com.");
//        } else {
//            if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(playerUID)) return;
//
//            new BukkitRunnable() {
//                public void run() {
//                  Foxtrot.getInstance().getSoulboundLivesMap().setLives(playerUID, Foxtrot.getInstance().getSoulboundLivesMap().getLives(playerUID));
//                  event.getPlayer().sendMessage(CC.translate("&aYou have used a life! You now have &2" + Foxtrot.getInstance().getSoulboundLivesMap().getLives(playerUID) + " &2lives&a."));
//                }
//            }.runTaskLater(Foxtrot.getInstance(), 40L);
//            return;
//        }
    }

}