package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class OptimisationListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
            Team team = LandBoard.getInstance().getTeam(player.getLocation());
            if (team != null && team.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
                event.getItemDrop().remove();
            }
        }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.WEB) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (event.getBlock().getType() == Material.WEB) {
                        event.getBlock().setType(Material.AIR);
                    }
                }
            }.runTaskLater(Foxtrot.getInstance(), 20*60*5);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getEntity().remove();
                }
            }.runTaskLater(Foxtrot.getInstance(), 20*60);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event){
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG && event.getEntityType() == EntityType.CHICKEN) {
            event.setCancelled(true);
        }

    }

}
