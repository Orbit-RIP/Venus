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
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.plaf.SpinnerUI;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CrystalRodAbility extends Ability {


    public HashMap<UUID, Long> cooldown = new HashMap<>();
    private HashMap<Player, Integer> hits = new HashMap<>();

    public CrystalRodAbility(Foxtrot plugin) {
        super(plugin);
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if (attacker.getItemInHand() == null || attacker.getItemInHand().getType() == Material.AIR) {
            return;
        }

        if (!this.isSimilar(attacker.getItemInHand())) {
            return;
        }

        Team playerFaction = Foxtrot.getInstance().getTeamHandler().getTeam(attacker);
        Team targetFaction = Foxtrot.getInstance().getTeamHandler().getTeam(victim);

        if (targetFaction != null && playerFaction != null) {
            if (targetFaction == playerFaction || targetFaction.getAllies().contains(playerFaction.getUniqueId())) return;
        }

        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(victim.getUniqueId()) || Foxtrot.getInstance().getPvPTimerMap().hasTimer(attacker.getUniqueId()))
            return;


        Team targetSpawn = LandBoard.getInstance().getTeam(victim.getLocation());
        Team playerSpawn = LandBoard.getInstance().getTeam(attacker.getLocation());


        if (targetSpawn != null && targetSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            event.setCancelled(true);
            return;
        }

        if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            event.setCancelled(true);
            return;
        }

        if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.CITADEL) || targetSpawn != null && targetSpawn.hasDTRBitmask(DTRBitmask.NO_ABILITY)) {
            attacker.sendMessage(CC.translate("&cYou cannot use this ability here."));
            event.setCancelled(true);
            return;
        }

        if (playerSpawn != null && playerSpawn.getName().equalsIgnoreCase("War")) {
            attacker.sendMessage(CC.translate("&cYou cannot use this ability here."));
            event.setCancelled(true);
            return;
        }

        if (this.cooldown.containsKey(attacker.getUniqueId()) && this.cooldown.get(attacker.getUniqueId()) != null) {
            long remaining = this.cooldown.get(attacker.getUniqueId()) - System.currentTimeMillis();

            if (remaining > 0L) {
                attacker.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(attacker.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                event.setCancelled(true);
                return;
            }
        }

        hits.putIfAbsent(attacker, 0);
        hits.put(attacker, hits.get(attacker) + 1);

        if (hits.get(attacker) == 3) {
            Foxtrot.getInstance().crystalrod.put(victim.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(20L));
            this.hits.remove(attacker);
            this.cooldown.put(attacker.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2L));
            attacker.sendMessage(CC.translate("&6 » &eYou have hit " + getPlayerName(victim)+ " &6with &eExotic Bone&e."));
            victim.sendMessage(CC.translate("&6 » &eYou have been hit with &eExotic Bone &eand cannot build for &620 seconds&e."));
            decrementUses(attacker, attacker.getItemInHand());
        }

        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> hits.remove(attacker), 300L);
        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> Foxtrot.getInstance().crystalrod.remove(victim.getUniqueId()), 300L);

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
        return "EXOTIC-BONE";
    }

    @Override
    public String getDisplayName() {
        return "&e&lExotic Bone";
    }

    @Override
    public String getDescription() {
        return "&7Hit a player 3 times to remove their ability to build.";
    }

    @Override
    public String getScoreboardPrefix() {
        return "&e&lExotic Bone";
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.BONE)
                .amount(1)
                .enchant(Enchantment.DURABILITY, 10)
                .name(this.getDisplayName())
                .addToLore("&7" + getDescription())
                .build();
    }

    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public HashMap<UUID, Long> getCooldown() {
        return this.cooldown;
    }

    public Integer getHits(Player player) {
        return this.hits.containsKey(player) ? this.getHits(player) : 0;
    }

    public void setHits(Player player, Integer amount) {
        this.hits.put(player, amount);
    }
}
