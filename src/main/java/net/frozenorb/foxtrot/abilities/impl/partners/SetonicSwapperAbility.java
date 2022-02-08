package net.frozenorb.foxtrot.abilities.impl.partners;

import com.google.common.collect.ImmutableList;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by PVPTUTORIAL | Created on 01/04/2020
 */

public class SetonicSwapperAbility extends Ability {
    public SetonicSwapperAbility(Foxtrot plugin) {
        super(plugin);
    }

    public static final List<Material> materialList = ImmutableList.of(Material.WOODEN_DOOR,
            Material.WOOD_DOOR, Material.TRAP_DOOR, Material.FENCE_GATE);

    public HashMap<UUID, Long> cooldown = new HashMap<>();

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

        Team playerSpawn = LandBoard.getInstance().getTeam(attacker.getLocation());

        if (playerSpawn != null) {
            if (playerSpawn.getName().equalsIgnoreCase("War")) {
                attacker.sendMessage(CC.translate("You cannot use this ability here."));
                event.setCancelled(true);
                return;
            }
        }

        if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.NO_ABILITY)) {
            attacker.sendMessage(CC.translate("&cYou cannot use this ability here."));
            event.setCancelled(true);
            return;
        }

        if (this.cooldown.containsKey(attacker.getUniqueId()) && this.cooldown.get(attacker.getUniqueId()) != null) {
            long remaining = this.cooldown.get(attacker.getUniqueId()) - System.currentTimeMillis();

            if (remaining > 0L) {
                attacker.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(attacker.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                event.setCancelled(true);
                attacker.updateInventory();
                return;
            }
        }

        if (this.getRemainingUses(attacker.getItemInHand()) <= 0) {
            attacker.playSound(attacker.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
            attacker.setItemInHand(new ItemStack(Material.AIR, 1));
            return;
        }


        Team playerFaction = Foxtrot.getInstance().getTeamHandler().getTeam(attacker);
        Team targetFaction = Foxtrot.getInstance().getTeamHandler().getTeam(victim);
        Team targetSpawn = LandBoard.getInstance().getTeam(victim.getLocation());

        if (playerFaction != null && targetFaction != null) {
            if (playerFaction.getName().equalsIgnoreCase(targetFaction.getName())) {
                attacker.sendMessage(CC.translate("&cYou cannot use this item on your faction members."));
                event.setCancelled(true);
                return;
            }
        }

        if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            event.setCancelled(true);
            return;
        }

        if (targetSpawn != null && targetSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            event.setCancelled(true);
            return;
        }

        if (victim.getInventory().getHelmet() == null) {
            attacker.sendMessage(CC.translate("&cThat player is not wearing a helmet."));
            return;
        }

        if (victim.getInventory().getHelmet().getType() != Material.DIAMOND_HELMET) {
            attacker.sendMessage(CC.translate("&cThat player must be wearing a diamond helmet."));
            return;
        }

        if (decrementUses(attacker, attacker.getItemInHand())) {
            attacker.playSound(attacker.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
            attacker.setItemInHand(new ItemStack(Material.AIR, 1));
        }

        attacker.sendMessage(CC.translate(" &6» &eYou have used the &6Swapper Axe Ability&e."));
        attacker.sendMessage(CC.translate(" &6* &e" + getPlayerName(victim) + "&e's helmet will be removed in &63 seconds&e."));
        attacker.sendMessage(CC.translate(""));
        attacker.sendMessage(CC.translate(" &e* &eYou have been placed on a &63 minute &ebooldown."));

        victim.sendMessage(CC.translate(" &6» &eYou have been hit with the &6Swapper Axe&e."));
        victim.sendMessage(CC.translate(" &6* &eYour helmet will be removed in &63 seconds&e."));


        victim.playSound(victim.getLocation(), Sound.ITEM_BREAK, 5, 5);
        attacker.playSound(attacker.getLocation(), Sound.ITEM_BREAK, 5, 5);

        new BukkitRunnable() {
            public void run() {
                if (victim.getInventory().firstEmpty() == -1) {
                    ItemStack helmet = victim.getInventory().getHelmet();
                    victim.getWorld().dropItem(victim.getLocation(), helmet);

                    victim.getInventory().setHelmet(new ItemStack(Material.AIR));
                    victim.updateInventory();

                    victim.sendMessage(CC.translate("&cYour helmet has been dropped on the floor due to having no inventory space."));
                } else {
                    ItemStack helmet = victim.getInventory().getHelmet();

                    victim.getInventory().addItem(helmet);
                    victim.getInventory().setHelmet(new ItemStack(Material.AIR));
                    victim.updateInventory();
                }
            }
        }.runTaskLater(Foxtrot.getInstance(), 3 * 20L);

        this.cooldown.put(attacker.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5L));
    }

    public int getRemainingUses(ItemStack itemStack) {
        return Ints.tryParse(itemStack.getItemMeta().getDisplayName().split("Uses: §f")[1].replace("§7)", ""));
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

    @Override
    public String getIdentifier() {
        return "SWAPPER";
    }

    @Override
    public String getDisplayName() {
        return "&6Swapper Axe";
    }

    @Override
    public String getDescription() {
        return "Removes the opponents helmet and disables interaction for 15 seconds.";
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.GOLD_AXE)
                .amount(amount)
                .enchant(Enchantment.DURABILITY, 10)
                .name(getDisplayName())
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

    @Override
    public String getScoreboardPrefix() {
        return "&6&lSwapper Axe";
    }
}
