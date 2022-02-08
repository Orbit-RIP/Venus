package net.frozenorb.foxtrot.abilities.impl.partners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import net.minecraft.util.org.apache.commons.lang3.time.*;
import org.bukkit.enchantments.*;
import cc.fyre.proton.util.ItemBuilder;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LamboClockAbility extends Ability {
		public HashMap<UUID, Long> cooldown;

		public LamboClockAbility(final Foxtrot plugin) {
			super(plugin);
			this.cooldown = new HashMap<UUID, Long>();
		}

		@EventHandler
		public void on(final PlayerInteractEvent event) {
			if (!event.getAction().name().startsWith("RIGHT_CLICK_")) {
				return;
			}
			final Player player = event.getPlayer();
			if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
				return;
			}
			if (!this.isSimilar(player.getItemInHand())) {
				return;
			}
			Team playerSpawn = LandBoard.getInstance().getTeam(player.getLocation());

			if (playerSpawn != null) {
				if (playerSpawn.getName().equalsIgnoreCase("War")) {
					player.sendMessage(CC.translate("&cYou cannot use this here."));
					event.setCancelled(true);
					return;
				}
			}

			if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.NO_ABILITY)) {
				player.sendMessage(CC.translate("&cYou cannot use this ability here."));
				event.setCancelled(true);
				return;
			}

			if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.CITADEL)) {
				player.sendMessage(CC.translate("&cYou cannot use this ability here."));
				event.setCancelled(true);
				return;
			}

			if (playerSpawn != null && playerSpawn.getName().equalsIgnoreCase("War")) {
				player.sendMessage(CC.translate("&cYou cannot use this ability here."));
				event.setCancelled(true);
				return;
			}

			 if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE) || playerSpawn != null) {
				 event.setCancelled(true);
				return;
			}

			if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) != null) {
				final long remaining = this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
				if (remaining > 0L) {
					player.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
					event.setCancelled(true);
					player.updateInventory();
					return;
				}
			}
			EnderpearlCooldownHandler.clearEnderpearlTimer(player);
			decrementUses(player, player.getItemInHand());
			player.sendMessage(CC.translate("&eYou reset your pearl cooldown"));
			this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3L));
		}

		@Override
		public String getIdentifier() {
			return "CLOCK";
		}

		@Override
		public String getDisplayName() {
			return "&d&lClock";
		}

		@Override
		public String getDescription() {
			return "&7Right click to remove your enderpearl cooldown.";
		}

		@Override
		public String getScoreboardPrefix() {
			return "&d&lClock";
		}

		@Override
		public ItemStack getItem(final int amount) {
			return ItemBuilder.of(Material.WATCH).amount(1).enchant(Enchantment.DURABILITY, 10).name(this.getDisplayName()).addToLore(new String[] { "&7" + this.getDescription() }).build();
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
