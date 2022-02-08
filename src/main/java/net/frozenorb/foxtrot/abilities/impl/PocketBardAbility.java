package net.frozenorb.foxtrot.abilities.impl;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;

import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.util.ItemBuilder;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by PVPTUTORIAL | Created on 01/04/2020
 */

public class PocketBardAbility extends Ability {

    public PocketBardAbility(Foxtrot plugin) {
        super(plugin);
    }

    public HashMap<UUID, Long> cooldown = new HashMap<>();

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

        this.decrementUses(player, player.getItemInHand());

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 160, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 240, 1));

        player.updateInventory();

        player.sendMessage(CC.translate("&7&m--*--------------------------------------*--"));
        player.sendMessage(CC.translate(" &6» &eYou have used the &6Pocket Bard Ability&e."));
        player.sendMessage(CC.translate("   &6* &eYou have received &6Strength II&e."));
        player.sendMessage(CC.translate("   &6* &eYou have received &6Resistance III&e."));
        player.sendMessage(CC.translate("   &6* &eYou have received &6Regeneration III&e."));
        player.sendMessage(CC.translate(""));
        player.sendMessage(CC.translate("   &6* &eYou have been placed on a &62 minute &ecooldown."));
        player.sendMessage(CC.translate("&7&m--*--------------------------------------*--"));

        this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2L));
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
        return "POCKET_BARD";
    }

    @Override
    public String getDisplayName() {
        return "&6&lPocket Bard";
    }

    @Override
    public String getDescription() {
        return "Receive three positive bard effects.";
    }

    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.INK_SACK)
                .amount(amount)
                .name(getDisplayName())
                .enchant(Enchantment.DURABILITY, 10)
                .addToLore("&7" + getDescription())
                .data((short)14)
                .build();
    }
    @Override
    public HashMap<UUID, Long> getCooldown() {
        return this.cooldown;
    }

    @Override
    public String getScoreboardPrefix() {
        return "&6&lPocket Bard";
    }
}