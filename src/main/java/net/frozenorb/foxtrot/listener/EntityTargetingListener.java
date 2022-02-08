package net.frozenorb.foxtrot.listener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.command.*;
import net.md_5.bungee.api.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

/**
 * Created by PVPTUTORIAL | Created on 09/05/2020
 */

public class EntityTargetingListener implements Listener {

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            if (event.getEntity() instanceof Creeper) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof Blaze) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof Spider) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof Skeleton) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof Zombie) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof Ghast) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof CaveSpider) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof Giant) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof MagmaCube) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof Silverfish) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof PigZombie) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof Slime) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof Skeleton) {
                Skeleton skeleton = (Skeleton)event.getEntity();
                if (skeleton.getSkeletonType().equals(Skeleton.SkeletonType.WITHER)) {
                    event.setCancelled(true);
                }
            }
            if (event.getEntity() instanceof Witch) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof EnderDragon) {
                event.setCancelled(true);
            }
            if (event.getEntity() instanceof Wither) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void whenTheyHits(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Slime) {
                event.setCancelled(true);
            }
            if (event.getDamager() instanceof PigZombie) {
                event.setCancelled(true);
            }
            if (event.getDamager() instanceof Enderman) {
                event.setCancelled(true);
            }
        }
    }

}
