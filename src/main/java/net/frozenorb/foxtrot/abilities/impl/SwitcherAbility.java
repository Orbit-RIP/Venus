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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by PVPTUTORIAL | Created on 01/04/2020
 */

public class SwitcherAbility extends Ability {

    public SwitcherAbility(Foxtrot plugin) {
        super(plugin);
    }

    public HashMap<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void on(ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Projectile entity = e.getEntity();
        Player player = (Player) entity.getShooter();

        if (!this.isSimilar(player.getItemInHand())) return;

        Team team = LandBoard.getInstance().getTeam(player.getLocation());

        if (team != null && team.hasDTRBitmask(DTRBitmask.NO_ABILITY)) {
            player.sendMessage(CC.translate("&cYou cannot use this ability here."));
            e.setCancelled(true);
            return;
        }
        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) != null) {
            long remaining = this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();

            if (remaining > 0L) {
                player.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                e.setCancelled(true);
                player.updateInventory();
                return;
            }
        }

        if (entity instanceof Snowball) {
            Snowball snowball = (Snowball) entity;
            snowball.setMetadata("switcher", new FixedMetadataValue(Foxtrot.getInstance(), player.getUniqueId()));
        }
    }


    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Snowball) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) ((Snowball) event.getDamager()).getShooter();

        Snowball snowball = (Snowball) event.getDamager();
        if (!snowball.hasMetadata("switcher")) {
            return;
        }

        Team playerFaction = Foxtrot.getInstance().getTeamHandler().getTeam(attacker);
        Team targetFaction = Foxtrot.getInstance().getTeamHandler().getTeam(victim);
        Team playerSpawn = LandBoard.getInstance().getTeam(attacker.getLocation());
        Team targetSpawn = LandBoard.getInstance().getTeam(victim.getLocation());

        if (playerFaction != null && targetFaction != null) {
            if (playerFaction.getName().equalsIgnoreCase(targetFaction.getName())) {
                attacker.sendMessage(CC.translate("&cYou cannot use this item on your faction members."));
                event.setCancelled(true);
                return;
            }
        }
        if (targetSpawn != null && targetSpawn.hasDTRBitmask(DTRBitmask.KOTH)) {
            attacker.sendMessage(CC.translate("&cYou cannot use this ability here."));
            event.setCancelled(true);
            return;
        }

        if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.NO_ABILITY)) {
            attacker.sendMessage(CC.translate("&cYou cannot use this ability here."));
            event.setCancelled(true);
            return;
        }

         if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE) || playerSpawn != null) {
             event.setCancelled(true);
            return;
        }

        if (playerSpawn != null && playerSpawn.getName().equalsIgnoreCase("War")) {
            attacker.sendMessage(CC.translate("&cYou cannot use this ability here."));
            event.setCancelled(true);
            return;
        }

        if (targetSpawn != null && targetSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            event.setCancelled(true);
            return;
        }

        if (!(snowball.getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) snowball.getShooter();

        if (shooter.getLocation().distance(victim.getLocation()) > 8) {
            shooter.sendMessage(ChatColor.RED + "You need to be within 8 blocks of that player!");
            return;
        }

        Location hitLocation = victim.getLocation();
        Location shooterLocation = shooter.getLocation();
        shooter.teleport(hitLocation);
        victim.teleport(shooterLocation);

        shooter.sendMessage(CC.translate(" &6» &eYou have switched positions with " + getPlayerName(victim) + " &edue to a &6Switcher Snowball&e."));
        victim.sendMessage(CC.translate(" &6» &eYou have switched positions with " + getPlayerName(shooter) + " &edue to a &6Switcher Snowball&e."));

        snowball.removeMetadata("switcher", Foxtrot.getInstance());

        this.cooldown.put(attacker.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10L));
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
        return "SWAPPER";
    }

    @Override
    public String getDisplayName() {
        return "&aSwapper";
    }

    @Override
    public String getDescription() {
        return "Hit your enemy and swap positions with them.";
    }

    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.SNOW_BALL)
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
        return "&a&lSwapper";
    }
}