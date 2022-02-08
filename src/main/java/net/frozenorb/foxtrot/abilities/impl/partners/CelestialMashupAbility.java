package net.frozenorb.foxtrot.abilities.impl.partners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.InventoryUtils;
import net.minecraft.util.com.google.common.primitives.Ints;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import cc.fyre.proton.util.ItemBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Emilxyz (langgezockt@gmail.com)
 * 12.09.2020 / 21:00
 * HCF / net.frozenorb.foxtrot.abilities.impl.partners
 */

public class CelestialMashupAbility extends Ability {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    public CelestialMashupAbility(Foxtrot plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                && event.getItem() != null
                && isSimilar(event.getItem()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) return;

        Player attacker = event.getPlayer();
        Player victim = (Player) event.getRightClicked();

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

        if (this.getRemainingUses(attacker.getItemInHand()) <= 0) {
            attacker.playSound(attacker.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
            attacker.setItemInHand(new ItemStack(Material.AIR, 1));
            return;
        }

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

        if (this.cooldown.containsKey(attacker.getUniqueId()) && this.cooldown.get(attacker.getUniqueId()) != null) {
            long remaining = this.cooldown.get(attacker.getUniqueId()) - System.currentTimeMillis();

            if (remaining > 0L) {
                attacker.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(attacker.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                event.setCancelled(true);
                return;
            }
        }

       List<ItemStack> hotbar = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            ItemStack item = victim.getInventory().getItem(i);
            if (item == null) item = new ItemStack(Material.AIR);
            hotbar.add(item);
        }

        Collections.shuffle(hotbar);

        for (int i = 0; i < hotbar.size(); i++) {
            victim.getInventory().setItem(i, hotbar.get(i));
        }

        victim.updateInventory();

        victim.sendMessage(String.format(
                CC.translate("&6 » &eYour hotbar has been shuffled by %s&e."),
                getPlayerName(attacker)
        ));
        attacker.sendMessage(String.format(
                CC.translate("&6 » &eYou shuffled the hotbar of %s&e. You are now on a cooldown for &6%s&e."),
                getPlayerName(victim),
                "5 minutes"
        ));

        if (decrementUses(attacker, attacker.getItemInHand())) {
            attacker.playSound(attacker.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
            attacker.setItemInHand(new ItemStack(Material.AIR, 1));
        }

        cooldown.put(attacker.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
    }

    @Override
    public String getIdentifier() {
        return "HOTBAR_MASHUP";
    }

    @Override
    public String getDisplayName() {
        return "&6&lHotbar Mashup";
    }

    @Override
    public String getDescription() {
        return "Will mess up your opponents hotbar";
    }

    @Override
    public String getScoreboardPrefix() {
        return "&6&lHotbar Mashup";
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(Material.NOTE_BLOCK)
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
        return this.cooldown;
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
