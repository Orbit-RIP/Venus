package net.frozenorb.foxtrot.abilities.impl;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.pvpclasses.PvPClass;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Emilxyz (langgezockt@gmail.com)
 * 12.09.2020 / 22:04
 * HCF / net.frozenorb.foxtrot.abilities.impl.partners
 */

public class CocaineAbility extends Ability {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    public CocaineAbility(Foxtrot plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            return;
        }

        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }

        if (player.getItemInHand().getAmount() > 1) {
            player.sendMessage(CC.translate("&cYou have to unstack this item to be able to use it."));
            event.setCancelled(true);
            return;
        }

        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId()))
            return;

        if (this.getRemainingUses(player.getItemInHand()) <= 0) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
            player.setItemInHand(new ItemStack(Material.AIR, 1));
            return;
        }

        Team playerSpawn = LandBoard.getInstance().getTeam(player.getLocation());

        if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            event.setCancelled(true);
            return;
        }

        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) != null) {
            long remaining = this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();

            if (remaining > 0L) {
                player.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                event.setCancelled(true);
                return;
            }
        }

        PvPClass.smartAddPotion(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300, 0));
        PvPClass.smartAddPotion(player, new PotionEffect(PotionEffectType.SPEED, 300, 2));

        player.sendMessage(String.format(
                CC.translate("&6» &eYou have used a %s&e."),
                CC.translate(getDisplayName())
        ));

        if (decrementUses(player, player.getItemInHand())) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        }

        cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
    }

    @Override
    public String getIdentifier() {
        return "COCAINE";
    }

    @Override
    public String getDisplayName() {
        return "&f&lCocaine";
    }

    @Override
    public String getDescription() {
        return "Gives you Resistance 1 and Speed 3 for 15 seconds";
    }

    @Override
    public String getScoreboardPrefix() {
        return "&f&lCocaine";
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.FERMENTED_SPIDER_EYE)
                .name(getDisplayName())
                .amount(amount)
                .enchant(Enchantment.DURABILITY, 10)
                .addToLore("&7" + getDescription())
                .build();
    }

    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public HashMap<UUID, Long> getCooldown() {
        return cooldown;
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
}
