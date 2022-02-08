package net.frozenorb.foxtrot.util;

import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;

@Data
public class StackedEntity {

    private LivingEntity entity;
    private int count;

    public StackedEntity(LivingEntity entity, int count) {
        this.count = count;
        this.entity = entity;
    }

    public static StackedEntity getFromEntity(LivingEntity entity) {
        if (entity.getCustomName() != null && ChatColor.stripColor(entity.getCustomName()).contains("Mobs")) {
            return new StackedEntity(entity, Integer.parseInt(ChatColor.stripColor(entity.getCustomName().replace("x Mobs", "").replace(" ", ""))));
        }
        return null;
    }

    public void updateDisplay() {
        this.entity.setCustomName(ChatColor.GOLD.toString() + ChatColor.BOLD + this.count + "x Mobs");
        this.entity.setCustomNameVisible(true);
    }

    public void addEntity() {
        ++this.count;
    }

}
