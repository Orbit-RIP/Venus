package net.frozenorb.foxtrot.abilities.impl.partners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.util.ItemBuilder;
import net.minecraft.server.v1_7_R4.ItemMilkBucket;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by PVPTUTORIAL | Created on 01/04/2020
 */

public class PortableBackstabAbility extends Ability {
    public PortableBackstabAbility(Foxtrot plugin) {
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

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if (attacker.getItemInHand() == null || attacker.getItemInHand().getType() == Material.AIR) {
            return;
        }

        if (!this.isSimilar(attacker.getItemInHand())) {
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

        Team playerSpawn = LandBoard.getInstance().getTeam(attacker.getLocation());
        Team targetSpawn = LandBoard.getInstance().getTeam(victim.getLocation());

        if (playerSpawn != null) {
            if (playerSpawn.getName().equalsIgnoreCase("War")) {
                attacker.sendMessage(CC.translate("&cYou cannot do this in a Faction War."));
                event.setCancelled(true);
                return;
            }

            if (playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
                event.setCancelled(true);
                return;
            }
        }

        if (targetSpawn != null) {
            if (targetSpawn.getName().equalsIgnoreCase("War")) {
                attacker.sendMessage(CC.translate("&cYou cannot do this in a Faction War."));
                event.setCancelled(true);
                return;
            }

            if (targetSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
                event.setCancelled(true);
                return;
            }
        }

        if (!this.isBehindPlayer(victim, attacker)) {
            attacker.sendMessage(CC.translate("&cYou must be behind this player to use this ability item."));
            return;
        }

        Team playerFaction = Foxtrot.getInstance().getTeamHandler().getTeam(attacker);
        Team targetFaction = Foxtrot.getInstance().getTeamHandler().getTeam(victim);

        if (playerFaction != null && targetFaction != null) {
            if (playerFaction.getName().equalsIgnoreCase(targetFaction.getName())) {
                attacker.sendMessage(CC.translate("&cYou cannot use this item on your faction members."));
                event.setCancelled(true);
                return;
            }
        }

        double hp = victim.getHealth();

        if (hp == 0) {
            return;
        }

        double amount = hp - 7.0;
        victim.setHealth(amount);

        attacker.sendMessage(CC.translate(" &6» &eYou have used the &e&lPortable Backstab &eon " + getPlayerName(victim) + "&e."));
        victim.sendMessage(CC.translate(" &6» &eYou have been backstabbed &eby " + getPlayerName(attacker) + "&e."));

        attacker.playSound(attacker.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
        victim.playSound(victim.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);

        this.decrementUses(attacker, attacker.getItemInHand());
        this.cooldown.put(attacker.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5L));
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
        return "PORTABLE_BACKSTAB";
    }

    @Override
    public String getDisplayName() {
        return "&c&lPortable Backstab";
    }

    @Override
    public String getDescription() {
        return "Stab your opponents with this portable rogue ability.";
    }

    @Override
    public String getScoreboardPrefix() {
        return "&c&lBackstab";
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.GOLD_SWORD)
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

    private boolean isBehindPlayer(Player player, Player attacker) {
        if (rpGetPlayerDirection(attacker).equals(rpGetPlayerDirection(player))) {
            return true;
        }
        return false;
    }

    public String rpGetPlayerDirection(Player playerSelf) {
        String dir = "";
        float y = playerSelf.getLocation().getYaw();
        if (y < 0.0F) {
            y += 360.0F;
        }
        y %= 360.0F;
        int i = (int) ((y + 8.0F) / 22.5D);
        if ((i == 0) || (i == 1) || (i == 15)) {
            dir = "west";
        } else if ((i == 4) || (i == 5) || (i == 6) || (i == 2) || (i == 3)) {
            dir = "north";
        } else if ((i == 8) || (i == 7) || (i == 9)) {
            dir = "east";
        } else if ((i == 11) || (i == 10) || (i == 12) || (i == 13) || (i == 14)) {
            dir = "south";
        } else {
            dir = "west";
        }
        return dir;
    }

    @Override
    public HashMap<UUID, Long> getCooldown() {
        return this.cooldown;
    }

    public static String getDirectionName(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "W";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NW";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "N";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "NE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "E";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SE";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "S";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "SW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "W";
        } else {
            return null;
        }
    }
}
