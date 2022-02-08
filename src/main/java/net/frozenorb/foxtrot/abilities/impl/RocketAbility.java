package net.frozenorb.foxtrot.abilities.impl;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.util.ItemBuilder;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by PVPTUTORIAL | Created on 02/04/2020
 */

public class RocketAbility extends Ability implements Runnable {
    public RocketAbility(Foxtrot plugin) {
        super(plugin);
        plugin.getServer().getScheduler().runTaskTimer(plugin, this, 50, 50);
    }

    public HashMap<UUID, Long> cooldown = new HashMap<>();
    public Set<UUID> rockets = new HashSet<>();

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (!event.getAction().name().startsWith("RIGHT_CLICK_")) {
            return;
        }

        Player player = event.getPlayer();

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            return;
        }

        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }

        Team playerSpawn = LandBoard.getInstance().getTeam(player.getLocation());

        if (playerSpawn != null) {
            if (playerSpawn.getName().equalsIgnoreCase("War")) {
                player.sendMessage(CC.translate("&cYou cannot use this ability here."));
                event.setCancelled(true);
                return;
            }
        }

        if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.NO_ABILITY)) {
            player.sendMessage(CC.translate("&cYou cannot use this ability here."));
            event.setCancelled(true);
            return;
        }

        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) != null) {
            long remaining = this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();

            if (remaining > 0L) {
                player.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                event.setCancelled(true);
                player.updateInventory();
                return;
            }
        }

        event.setCancelled(true);

        this.decrementUses(player, player.getItemInHand());

        if (player.isOnGround()) {
            player.setVelocity(player.getLocation().getDirection().multiply(2.3));
        } else {
            player.setVelocity(player.getLocation().getDirection().multiply(2.3).setY(1));
        }
        player.playSound(player.getLocation(), Sound.EXPLODE, 1, 10);

        player.updateInventory();

        player.sendMessage(CC.translate("&7&m--*--------------------------------------*--"));
        player.sendMessage(CC.translate(" &6» &eYou have used the &4Rocket Boost Ability&e."));
        player.sendMessage(CC.translate("   &6* &eYou have been launched up into the sky."));
        player.sendMessage(CC.translate("&7&m--*--------------------------------------*--"));

        this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2L));
        this.rockets.add(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Player player = (Player) entity;
            if (rockets.remove(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    public void decrementUses(Player player, ItemStack itemStack) {
        if (itemStack.getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        } else {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
    }

    @Override
    public String getIdentifier() {
        return "ROCKET";
    }

    @Override
    public String getDisplayName() {
        return "&4Rocket";
    }

    @Override
    public String getDescription() {
        return "Launch yourself 12 blocks in the air.";
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.FIREWORK)
                .name(getDisplayName())
                .amount(amount)
                .enchant(Enchantment.DURABILITY, 10)
                .addToLore("&7" + getDescription())
                .build();
    }

    @Override
    public HashMap<UUID, Long> getCooldown() {
        return this.cooldown;
    }

    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public String getScoreboardPrefix() {
        return "&4&lRocket";
    }

    @Override
    public void run() {
        Iterator<UUID> iterator = this.rockets.iterator();
        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            Player player = Bukkit.getPlayer(uuid);

            if (player != null && (player.isOnGround())) {
                iterator.remove();
            }
        }
    }
}
