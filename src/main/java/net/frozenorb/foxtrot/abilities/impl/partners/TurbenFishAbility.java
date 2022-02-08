package net.frozenorb.foxtrot.abilities.impl.partners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.pvpclasses.PvPClass;
import net.frozenorb.foxtrot.pvpclasses.PvPClassHandler;
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
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TurbenFishAbility extends Ability {

    public TurbenFishAbility(Foxtrot plugin) {
        super(plugin);
    }

    public HashMap<UUID, Long> cooldown = new HashMap<>();
    private HashMap<Player, Integer> hits = new HashMap<>();

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

        if (attacker.getItemInHand().getAmount() > 1) {
            attacker.sendMessage(CC.translate("&cYou have to unstack this item to be able to use it."));
            event.setCancelled(true);
            return;
        }

        Team playerFaction = Foxtrot.getInstance().getTeamHandler().getTeam(attacker);
        Team targetFaction = Foxtrot.getInstance().getTeamHandler().getTeam(victim);

        if (targetFaction != null && playerFaction != null) {
            if (targetFaction == playerFaction || targetFaction.getAllies().contains(playerFaction.getUniqueId())) return;
        }

        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(victim.getUniqueId()) || Foxtrot.getInstance().getPvPTimerMap().hasTimer(attacker.getUniqueId()))
            return;

        Team playerSpawn = LandBoard.getInstance().getTeam(attacker.getLocation());
        Team targetSpawn = LandBoard.getInstance().getTeam(victim.getLocation());

        if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            event.setCancelled(true);
            return;
        }

        if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.CITADEL) || targetSpawn != null && targetSpawn.hasDTRBitmask(DTRBitmask.CITADEL)) {
            attacker.sendMessage(CC.translate("&cYou cannot use this ability here."));
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
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 12 * 20, 0));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 12 * 20, 0));
            this.hits.remove(attacker);

            if (decrementUses(attacker, attacker.getItemInHand())) {
                attacker.playSound(attacker.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
                attacker.setItemInHand(new ItemStack(Material.AIR, 1));
            }

            this.cooldown.put(attacker.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3L));
        }

        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> hits.remove(attacker), 100L);
    }

    @Override
    public String getIdentifier() {
        return "TURBEN_FISH";
    }

    @Override
    public String getDisplayName() {
        return "&a&lTurben's Fish";
    }

    @Override
    public String getDescription() {
        return "Hit someone 3 times and they get poison and slowness for 12 seconds.";
    }

    @Override
    public String getScoreboardPrefix() {
        return "&a&lTurben's Fish";
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.RAW_FISH).data((short) 3)
                .amount(amount)
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

//    public int getRemainingUses(ItemStack itemStack) {
//        return Ints.tryParse(itemStack.getItemMeta().getDisplayName().split("Uses: §f")[1].replace("§7)", ""));
//    }

    public boolean decrementUses(Player player, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(CC.translate(this.getDisplayName()));
        itemStack.setItemMeta(itemMeta);

        player.setItemInHand(itemStack);

        return false;
    }

}
