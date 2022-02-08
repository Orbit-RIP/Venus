package net.frozenorb.foxtrot.abilities.impl;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.scoreboard.ScoreFunction;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.ColorUtil;
import cc.fyre.proton.util.ItemBuilder;
import net.minecraft.util.com.google.common.primitives.Ints;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by PVPTUTORIAL | Created on 01/04/2020
 */

public class SyringeAbility extends Ability {

    public SyringeAbility(Foxtrot plugin) {
        super(plugin);
    }

    public HashMap<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (attacker.getItemInHand() == null || attacker.getItemInHand().getType() == Material.AIR) {
            return;
        }

        if (!this.isSimilar(attacker.getItemInHand())) {
            return;
        }

        Team playerSpawn = LandBoard.getInstance().getTeam(attacker.getLocation());

        if (playerSpawn != null) {
            if (playerSpawn.getName().equalsIgnoreCase("War")) {
                attacker.sendMessage(CC.translate("&cYou cannot do this in a Faction War."));
                event.setCancelled(true);
                return;
            }
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

        List<PotionEffect> potionEffects = this.getAvailableEffects(victim);

        if (potionEffects.size() == 0) {
            attacker.sendMessage(CC.translate("&cThat player does not have any available effects."));
            return;
        }

        decrementUses(attacker, attacker.getItemInHand());

        List<String> lore = new ArrayList<>();

        lore.add(CC.translate("&7&m--*-----------------------*--"));
        potionEffects.forEach(effect -> lore.add(CC.translate(" &7* &e" + this.getAbilityManager().getPotionName(effect.getType()) + " " + (effect.getAmplifier() + 1) + " &7(" + ScoreFunction.TIME_FANCY.apply((float) (effect.getDuration() / 20)) + ")")));
        lore.add(CC.translate("&7&m--*-----------------------*--"));

        ItemStack bottle = ItemBuilder.of(Material.POTION)
                .name("&6Stolen Efffects: &f" + victim.getName())
                .addToLore(lore.toArray(new String[0]))
                .enchant(Enchantment.DURABILITY, 10)
                .build();

        for (PotionEffect effect : potionEffects) {
            victim.removePotionEffect(effect.getType());
        }

        attacker.getInventory().addItem(bottle);

        this.cooldown.put(attacker.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20L));
    }

    @EventHandler
    public void on(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        ItemStack item = event.getItem();

        if (item.getType() != Material.POTION || item.getDurability() != (short) 0 || !item.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
            return;
        }

        event.setCancelled(true);

        for (String string : item.getItemMeta().getLore()) {
            if (string.equalsIgnoreCase(CC.translate("&7&m--*-----------------------*--"))) {
                continue;
            }

            string = ChatColor.stripColor(string).replace(" * ", "");

            String[] strings = string.split(" ");
            String[] durations = string.split("\\(");
            String time = durations[1].replace(")", "");

            int amplifier;
            String type;

            if (Ints.tryParse(strings[1]) == null) {
                amplifier = Ints.tryParse(strings[2]);
                type = strings[0] + " " + strings[1];
            } else {
                amplifier = Ints.tryParse(strings[1]);
                type = strings[0];
            }

            int minutes = Ints.tryParse(time.split(":")[0]);
            int seconds = Ints.tryParse(time.split(":")[1]);

            int ticks = ((minutes * 60) + seconds) * 20;

            PotionEffect potionEffect = new PotionEffect(this.getAbilityManager().getPotionType(type), ticks, amplifier - 1);

            player.addPotionEffect(potionEffect);
        }
    }

    public List<PotionEffect> getAvailableEffects(Player player) {
        List<PotionEffect> potionEffectList = new ArrayList<>();

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getDuration() >= (1600 * 20)) {
                continue;
            }

            if (this.getAbilityManager().getPotionName(potionEffect.getType()).equalsIgnoreCase("None")) {
                continue;
            }

            potionEffectList.add(potionEffect);
        }

        return potionEffectList;
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
        return "SYRINGE";
    }

    @Override
    public String getDisplayName() {
        return "&2&lSyringe";
    }

    @Override
    public String getDescription() {
        return "Steal the potion effects of your enemy.";
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
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.GLASS_BOTTLE)
                .amount(amount)
                .name(getDisplayName())
                .enchant(Enchantment.DURABILITY, 10)
                .addToLore("&7" + getDescription())
                .build();
    }

    @Override
    public String getScoreboardPrefix() {
        return "&2&lSyringe";
    }

}