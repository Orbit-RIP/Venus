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
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import cc.fyre.proton.util.ItemBuilder;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RottenEggAbility extends Ability {

    public HashMap<UUID, Long> cooldown = new HashMap<>();

    public RottenEggAbility(Foxtrot plugin) {
        super(plugin);
    }

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

        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) != null) {
            long remaining = this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();

            if (remaining > 0L) {
                player.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                e.setCancelled(true);
                player.updateInventory();
                return;
            }
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

        if (entity instanceof Egg) {
            Egg egg = (Egg) entity;
            egg.setMetadata("rottenegg", new FixedMetadataValue(Foxtrot.getInstance(), player.getUniqueId()));
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Egg) || !(e.getEntity() instanceof Player)) {
            return;
        }

        Player damaged = (Player) e.getEntity();
        Egg egg = (Egg) e.getDamager();
        if (!egg.hasMetadata("rottenegg")) return;

        if (egg.getShooter() instanceof Player) {
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 160, 2));
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 160, 2));
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 160, 2));
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 2));

            damaged.sendMessage(CC.translate(" &6» &eYou have been hit with the &2&lRotten Egg Ability&e."));
            damaged.sendMessage(CC.translate(" &6* &eYou now have &6Debuffs &efor &68 seconds&e."));
        }
    }

    @EventHandler
    public void onChickenSpawn(PlayerEggThrowEvent e) {
        e.setHatching(false);
    }

    @Override
    public String getIdentifier() {
        return "ROTTEN_EGG";
    }

    @Override
    public String getDisplayName() {
        return "&2&lRotten Egg";
    }

    @Override
    public String getDescription() {
        return "Gives a player debuffs when hit using the item!";
    }

    @Override
    public String getScoreboardPrefix() {
        return "&2&lRotten Egg";
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.EGG)
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
}
