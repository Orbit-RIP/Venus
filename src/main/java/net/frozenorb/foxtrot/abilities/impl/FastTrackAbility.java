package net.frozenorb.foxtrot.abilities.impl;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.util.ItemBuilder;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by PVPTUTORIAL | Created on 01/04/2020
 */

public class FastTrackAbility extends Ability {

    public FastTrackAbility(Foxtrot plugin) {
        super(plugin);
    }

    public HashMap<UUID, Long> cooldown = new HashMap<>();

    public static ArrayList<UUID> tping = new ArrayList<>();

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (!event.getAction().name().startsWith("RIGHT_CLICK_")) {
            return;
        }

        Player player = event.getPlayer();

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            return;
        }

        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }

        Team playerSpawn = LandBoard.getInstance().getTeam(player.getLocation());

        if (playerSpawn != null) {
            if (playerSpawn.getName().equalsIgnoreCase("War")) {
                player.sendMessage(CC.translate("&cYou cannot do this in a Faction War."));
                event.setCancelled(true);
                return;
            }
        }

        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) != null) {
            long remaining = this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();

            if (remaining > 0L) {
                player.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                event.setCancelled(true);
                player.updateInventory();
                return;
            }
        }

        Team playerFaction = Foxtrot.getInstance().getTeamHandler().getTeam(player);

        boolean insideTerritory = false;

        if (playerFaction != null && LandBoard.getInstance().getTeam(player.getLocation()) != null && LandBoard.getInstance().getTeam(player.getLocation()).getName().equalsIgnoreCase(playerFaction.getName())) {
            insideTerritory = true;
        }

        if (LandBoard.getInstance().getTeam(player.getLocation()) != null && LandBoard.getInstance().getTeam(player.getLocation()).hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            insideTerritory = true;
        }


        if (!insideTerritory) {
            player.sendMessage(CC.translate("&cYou must be inside your faction claim or a Safezone to use this ability item."));
            event.setCancelled(true);
            player.updateInventory();
            return;
        }

        boolean teleported = false;
        for (Event koth : Foxtrot.getInstance().getEventHandler().getEvents()) {
            if (koth.isActive()) {
                Team team = Foxtrot.getInstance().getTeamHandler().getTeam(koth.getName());

                if (team != null) {
                    World world = Bukkit.getWorld(team.getClaims().get(0).getWorld());
                    int x = team.getClaims().get(0).getX1();
                    int z = team.getClaims().get(0).getZ1();
                    int y = world.getHighestBlockYAt(x, z) + 1;

                    player.teleport(new Location(world, x,y,z).add(0.5, 0.5, 0.5));
                    teleported = true;
                    break;
                }
            }
        }

        if (teleported) {
            this.decrementUses(player, player.getItemInHand());

            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3L));
        }
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
        return "FAST_TRACK";
    }

    @Override
    public String getDisplayName() {
        return "&9&lFast Track";
    }

    @Override
    public String getDescription() {
        return "Teleport to the active event.";
    }

    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.WATCH)
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
        return "&9&lFast Track";
    }
}