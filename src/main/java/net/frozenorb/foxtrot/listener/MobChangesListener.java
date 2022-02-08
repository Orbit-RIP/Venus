package net.frozenorb.foxtrot.listener;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class MobChangesListener implements Listener {

    @EventHandler
    public void onExplode(CreeperPowerEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreeperExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof Creeper) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(ExplosionPrimeEvent e) {
        if (e.getEntity() instanceof Creeper) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEndermanDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Enderman || event.getDamager() instanceof Creeper || event.getDamager() instanceof MagmaCube || event.getDamager() instanceof Slime) {
            event.setCancelled(true);
        }
    }

}