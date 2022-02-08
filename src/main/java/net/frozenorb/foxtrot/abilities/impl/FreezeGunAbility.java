package net.frozenorb.foxtrot.abilities.impl;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.util.ItemBuilder;
import net.minecraft.util.com.google.common.primitives.Ints;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by PVPTUTORIAL | Created on 01/04/2020
 */

public class FreezeGunAbility extends Ability {

    public FreezeGunAbility(Foxtrot plugin) {
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

        Team team = LandBoard.getInstance().getTeam(player.getLocation());

        if (team != null) {
            if (team.getName().equalsIgnoreCase("War")) {
                player.sendMessage(CC.translate("&cYou cannot do this in a Faction War."));
                event.setCancelled(true);
                return;
            }
        }

//        if (targetSpawn.getName().equalsIgnoreCase("War")) {
//            attacker.sendMessage(CC.translate("&cYou cannot do this in a Faction War."));
//            event.setCancelled(true);
//            return;
//        }

        if (team != null && team.hasDTRBitmask(DTRBitmask.NO_ABILITY)) {
            player.sendMessage(CC.translate("&cYou cannot use this ability here."));
            event.setCancelled(true);
            return;
        }

        if (this.getRemainingUses(player.getItemInHand()) <= 0) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
            player.setItemInHand(new ItemStack(Material.AIR, 1));
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

        player.launchProjectile(Snowball.class);

        if (decrementUses(player, player.getItemInHand())) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        }

        player.updateInventory();

        this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3L));
    }

    @EventHandler
    public void on(ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Projectile entity = e.getEntity();
        Player player = (Player) entity.getShooter();

        if (!this.isSimilar(player.getItemInHand())) return;

        if (entity instanceof Snowball) {
            Snowball snowball = (Snowball) entity;
            snowball.setMetadata("freezegun", new FixedMetadataValue(Foxtrot.getInstance(), player.getUniqueId()));
        }
    }


    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Snowball) || !(e.getEntity() instanceof Player)) {
            return;
        }

        Player damaged = (Player) e.getEntity();
        Snowball snowball = (Snowball) e.getDamager();
        if (!snowball.hasMetadata("freezegun")) return;

        if (snowball.getShooter() instanceof Player) {
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));

            damaged.sendMessage(CC.translate("&7&m--*--------------------------------------*--"));
            damaged.sendMessage(CC.translate(" &6» &eYou have been hit with the &cFreeze Gun Ability&e."));
            damaged.sendMessage(CC.translate("   &6* &eYou now have &6Slowness 3 &efor &65 seconds&e."));
            damaged.sendMessage(CC.translate("&7&m--*--------------------------------------*--"));
        }
    }

    public boolean decrementUses(Player player, ItemStack itemStack) {
        int uses = Ints.tryParse(player.getItemInHand().getItemMeta().getDisplayName().split("Uses: §f")[1].replace("§7)", ""));

        if (uses == 1) {
            return true;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(CC.translate(this.getDisplayName() + " &7(Uses: &f" + (uses - 1) + "&7)"));
        itemStack.setItemMeta(itemMeta);

        player.setItemInHand(itemStack);

        return false;
    }

    public int getRemainingUses(ItemStack itemStack) {
        return Ints.tryParse(itemStack.getItemMeta().getDisplayName().split("Uses: §f")[1].replace("§7)", ""));
    }

    @Override
    public String getIdentifier() {
        return "FREEZE_GUN";
    }

    @Override
    public String getDisplayName() {
        return "&cFreeze Gun";
    }

    @Override
    public String getDescription() {
        return "Hit your enemy and give them Slowness III.";
    }

    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.DIAMOND_HOE)
                .amount(amount)
                .name(getDisplayName())
                .enchant(Enchantment.DURABILITY, 10)
                .addToLore("&7" + getDescription())
                .build();
    }
    @Override
    public HashMap<UUID, Long> getCooldown() {
        return this.cooldown;
    }

    @Override
    public String getScoreboardPrefix() {
        return "&c&lFreeze Gun";
    }
}