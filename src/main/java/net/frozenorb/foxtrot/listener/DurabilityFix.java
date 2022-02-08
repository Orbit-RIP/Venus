package net.frozenorb.foxtrot.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

/**
 * Created by PVPTUTORIAL | Created on 04/05/2020
 */

public class DurabilityFix implements Listener {
    @EventHandler
    public void onDurLoss(final PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        int damage = event.getDamage();

        event.setDamage((int) Math.floor(damage / 3));
    }
}
