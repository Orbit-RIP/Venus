package net.frozenorb.foxtrot.events.ktk;

import lombok.Data;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.ColorUtil;
import net.frozenorb.foxtrot.util.ItemBuilder;
import net.frozenorb.foxtrot.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class KillTheKing {

    private UUID uuid;
    private long started;

    private List<KillTheKingAdvancement> advancementsPurchased = new ArrayList<>();

    public KillTheKing(UUID uuid) {
        this.uuid = uuid;
        this.started = System.currentTimeMillis();
        Player player = Bukkit.getServer().getPlayer(uuid);
        cancel(player);

        if (player != null) {

            Bukkit.broadcastMessage(ColorUtil.format("&7&m--*--------------------------------------*--"));
            Bukkit.broadcastMessage(ColorUtil.format("&6&lKill The King:"));
            Bukkit.broadcastMessage(ColorUtil.format(" &6» &ePlayer: "
                    + CC.getColor(player)));
            Bukkit.broadcastMessage(ColorUtil.format("&7&m--*--------------------------------------*--"));

            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                player.removePotionEffect(PotionEffectType.SPEED);
            }

            if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                player.removePotionEffect(PotionEffectType.REGENERATION);
            }

            if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }

            giveItems(player);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
        } else {
            Foxtrot.getInstance().setKillTheKing(null);
        }
    }

    public void win(Player winner) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(winner);

        if (team != null) {
            team.setPoints(team.getPoints() + 5);
        }

        Bukkit.broadcastMessage(ColorUtil.format("&7&m--*--------------------------------------*--"));
        Bukkit.broadcastMessage(ColorUtil.format("&6&lKill The King:"));
        Bukkit.broadcastMessage(ColorUtil.format("&fThe King has been killed!"));
        Bukkit.broadcastMessage(ColorUtil.format(("")));
        Bukkit.broadcastMessage(ColorUtil.format(" &6» &eWinner: &f" + winner.getDisplayName()));
        Bukkit.broadcastMessage(ColorUtil.format("&7&m--*--------------------------------------*--"));

        Foxtrot.getInstance().setKillTheKing(null);
    }

    public void cancel(Player player) {
        if (!ItemUtils.hasEmptyInventory(player)) {
            player.sendMessage(ChatColor.RED + "You need to have an empty inventory to join the event.");
        }
    }

    public void giveItems(Player player) {
        player.getInventory().setHelmet(new ItemBuilder(Material.DIAMOND_HELMET)
                .displayName(CC.YELLOW + "[Kill The King Helmet]")
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true)
                .enchant(Enchantment.DURABILITY, 5, true).build());

        player.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .displayName(CC.YELLOW + "[Kill The King Chestplate]")
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true)
                .enchant(Enchantment.DURABILITY, 5, true).build());

        player.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS)
                .displayName(CC.YELLOW + "[Kill The King Leggings]")
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true)
                .enchant(Enchantment.DURABILITY, 5, true).build());

        player.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS)
                .displayName(CC.YELLOW + "[Kill The King Boots]")
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true)
                .enchant(Enchantment.DURABILITY, 5, true)
                .enchant(Enchantment.PROTECTION_FALL, 5, true).build());

        player.getInventory().setItem(0, new ItemBuilder(Material.DIAMOND_SWORD)
                .displayName(CC.YELLOW + "[Kill The King Sword]")
                .enchant(Enchantment.DAMAGE_ALL, 5, true)
                .enchant(Enchantment.DURABILITY, 5, true).build());

        player.getInventory().setItem(1, new ItemStack(Material.ENDER_PEARL, 64));
        player.getInventory().setItem(2, new ItemBuilder(Material.PUMPKIN_PIE)
                .displayName(CC.YELLOW + "[Knockback Pie]")
                .enchant(Enchantment.KNOCKBACK, 5, true).build());



        Potion healPotion = new Potion(PotionType.INSTANT_HEAL);
        healPotion.setLevel(2);
        healPotion.setSplash(true);
        ItemStack healItem = new ItemStack(Material.POTION, 64);
        healPotion.apply(healItem);

        while (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(healItem);
        }
    }
}
