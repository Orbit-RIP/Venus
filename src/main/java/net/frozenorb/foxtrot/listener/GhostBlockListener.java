package net.frozenorb.foxtrot.listener;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MobEffectList;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;

public class GhostBlockListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {

        if (!this.isSuperSpeed(event.getPlayer())) {
            return;
        }

        event.getBlock().getState().update(true);
        event.getPlayer().sendBlockChange(event.getBlock().getLocation(),event.getBlock().getType(),event.getBlock().getData());
    }

    public boolean isSuperSpeed(Player player) {

        if (player == null) {
            return false;
        }

        if (player.getItemInHand() == null) {
            return false;
        }

        if (player.getItemInHand().getItemMeta() == null) {
            return false;
        }

        if (!player.getItemInHand().getItemMeta().hasEnchant(Enchantment.DIG_SPEED)) {
            return false;
        }

        if (player.getItemInHand().getType().name().contains("PICKAXE")) {

            if (player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) > 5) {
                return true;
            }

            final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();

            if (entityPlayer.hasEffect(MobEffectList.FASTER_DIG) && entityPlayer.getEffect(MobEffectList.FASTER_DIG).getAmplifier() < 2 && player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) > 5) {
                return true;
            }

            if (entityPlayer.hasEffect(MobEffectList.FASTER_DIG) && entityPlayer.getEffect(MobEffectList.FASTER_DIG).getAmplifier() >= 2 && player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) >= 5) {
                return true;
            }

        } else if (player.getItemInHand().getType().name().contains("SPADE")) {

            if (!player.hasPotionEffect(PotionEffectType.FAST_DIGGING) && player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) >= 4) {
                return true;
            }

            if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {

                final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();

                if (entityPlayer.hasEffect(MobEffectList.FASTER_DIG) && entityPlayer.getEffect(MobEffectList.FASTER_DIG).getAmplifier() < 2 && player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) >= 3) {
                    return true;
                }

                if (entityPlayer.hasEffect(MobEffectList.FASTER_DIG) && entityPlayer.getEffect(MobEffectList.FASTER_DIG).getAmplifier() >= 2 && player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) >= 2) {
                    return true;
                }
            }

        }

        return false;
    }
}
