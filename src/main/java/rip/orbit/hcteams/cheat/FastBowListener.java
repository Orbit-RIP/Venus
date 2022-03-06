package rip.orbit.hcteams.cheat;

import net.minecraft.server.v1_7_R4.Enchantment;
import net.minecraft.server.v1_7_R4.EnchantmentManager;
import net.minecraft.server.v1_7_R4.EntityArrow;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class FastBowListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        if (!player.hasMetadata("cheat")) return;

        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand.getType() != Material.BOW) return;

        CraftPlayer craftPlayer = ((CraftPlayer) player);

        EntityArrow entityarrow = new EntityArrow(craftPlayer.getHandle().world, craftPlayer.getHandle(), 2.0F);

        entityarrow.setCritical(true);

        int k = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, craftPlayer.getHandle().inventory.getItemInHand());

        if (k > 0) {
            entityarrow.b(entityarrow.e() + (double) k * 0.5D + 0.5D);
        }

        int l = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, craftPlayer.getHandle().inventory.getItemInHand());

        if (l > 0) {
            entityarrow.setKnockbackStrength(l);
        }

        if (EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, craftPlayer.getHandle().inventory.getItemInHand()) > 0) {
            // CraftBukkit start - call EntityCombustEvent
            EntityCombustEvent entityCombustEvent = new EntityCombustEvent(entityarrow.getBukkitEntity(), 100);
            entityarrow.world.getServer().getPluginManager().callEvent(entityCombustEvent);

            if (!entityCombustEvent.isCancelled()) {
                entityarrow.setOnFire(entityCombustEvent.getDuration());
            }
            // CraftBukkit end
        }

        // CraftBukkit start
        org.bukkit.event.entity.EntityShootBowEvent entityShootBowEvent = CraftEventFactory.callEntityShootBowEvent(craftPlayer.getHandle(), craftPlayer.getHandle().inventory.getItemInHand(), entityarrow, 1.0f);
        if (entityShootBowEvent.isCancelled()) {
            entityShootBowEvent.getProjectile().remove();
            return;
        }

        if (entityShootBowEvent.getProjectile() == entityarrow.getBukkitEntity()) {
            // Velt start
            if (!craftPlayer.getHandle().world.addEntity(entityarrow)) {
                return;
            }
            // Velt end
        }
        // CraftBukkit end

        craftPlayer.getHandle().world.makeSound(craftPlayer.getHandle(), "random.bow", 1.0F, 1.0F / (ThreadLocalRandom.current().nextFloat() * 0.4F + 1.2F) + 0.5F);
    }
}

