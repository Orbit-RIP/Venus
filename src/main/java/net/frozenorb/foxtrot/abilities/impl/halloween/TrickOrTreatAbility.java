package net.frozenorb.foxtrot.abilities.impl.halloween;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import cc.fyre.proton.util.ItemBuilder;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TrickOrTreatAbility extends Ability {

    public TrickOrTreatAbility(Foxtrot plugin) {
        super(plugin);
    }

    public HashMap<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (!e.getAction().name().startsWith("RIGHT_CLICK_")) {
            return;
        }

        Player player = e.getPlayer();

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
                e.setCancelled(true);
                return;
            }
        }

        if (team != null && team.hasDTRBitmask(DTRBitmask.NO_ABILITY)) {
            player.sendMessage(CC.translate("&cYou cannot use this ability here."));
            e.setCancelled(true);
            return;
        }

        Long remaining = this.cooldown.containsKey(player.getUniqueId()) ? this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis() : null;

        if (remaining != null && remaining > 0) {
            player.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
            e.setCancelled(true);
            player.updateInventory();
            return;
        }

        int rand = ThreadLocalRandom.current().nextInt(0, 2);

        if (rand == 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1));
            player.sendMessage(CC.translate("  &6* &eYou have used &5&lTrick Or Treat &eand received &e&lStrength II &e& &e&lSpeed III"));
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2));
            player.sendMessage(CC.translate("  &6* &eYou have used &5&lTrick Or Treat &eand received &e&lWither II &e& &e&lSlowness III"));
        }
        this.decrementUses(player, player.getItemInHand());
        this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3L));
    }

    @Override
    public String getIdentifier() {
        return "TRICK_OR_TREAT";
    }

    @Override
    public String getDisplayName() {
        return "&5&lTrick Or Treat";
    }

    @Override
    public String getDescription() {
        return "Receive Strength 2 & Speed 3 or Wither 2 & Slowness 3";
    }

    @Override
    public String getScoreboardPrefix() {
        return "&5&lTrick Or Treat";
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.FERMENTED_SPIDER_EYE)
                .amount(amount)
                .name(getDisplayName())
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
        return this.cooldown;
    }

    public void decrementUses(Player player, ItemStack itemStack) {
        if (itemStack.getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        } else {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
    }

}
