package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.util.StackedEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityStackListener implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        boolean createNew = true;

        if (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) return;

        for (LivingEntity other : entity.getWorld().getLivingEntities()) {
            if (StackedEntity.getFromEntity(other) != null) {
                StackedEntity stackedEntity = StackedEntity.getFromEntity(other);
                if (!entity.getType().equals(other.getType()) || entity.getLocation().distance(other.getLocation()) > 20.0 || entity.getType() == EntityType.ENDERMAN) continue;
                e.setCancelled(true);
                stackedEntity.setCount(stackedEntity.getCount() + 1);
                stackedEntity.updateDisplay();
                createNew = false;
            }
        }

        if (createNew) {
            new StackedEntity(entity, 1).updateDisplay();
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        StackedEntity stackedEntity = StackedEntity.getFromEntity(entity);

        if (stackedEntity != null) {
            if (stackedEntity.getCount() <= 1) return;

            Entity spawn = entity.getWorld().spawnEntity(entity.getLocation(), stackedEntity.getEntity().getType());

            if (spawn instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) spawn;
                livingEntity.setCustomName(stackedEntity.getEntity().getCustomName());
                livingEntity.setCustomNameVisible(true);
                StackedEntity stackedEntity1 = StackedEntity.getFromEntity(livingEntity);
                stackedEntity1.setCount(stackedEntity.getCount() - 1);
                stackedEntity1.updateDisplay();
            }
        }
    }
}
