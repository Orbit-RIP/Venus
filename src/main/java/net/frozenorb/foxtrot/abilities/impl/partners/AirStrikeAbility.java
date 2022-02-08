package net.frozenorb.foxtrot.abilities.impl.partners;

import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.events.dtc.DTC;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import cc.fyre.proton.util.ItemBuilder;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AirStrikeAbility extends Ability {
	public HashMap<UUID, Long> cooldown = new HashMap<>();
	@Setter
	private Boolean enabled = false;

	public AirStrikeAbility(Foxtrot plugin) {
		super(plugin);
	}


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
		if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.CITADEL)) {
			player.sendMessage(CC.translate("&cYou cannot use this ability here."));
			event.setCancelled(true);
			return;
		}

		if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.NO_ABILITY)) {
			player.sendMessage(CC.translate("&cYou cannot use this ability here."));
			event.setCancelled(true);
			return;
		}

		if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE) || playerSpawn != null) {
			event.setCancelled(true);
			return;
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

		player.playSound(player.getLocation(), Sound.NOTE_BASS, 50, 1);
		player.getWorld().spawnEntity(event.getClickedBlock().getLocation().add(0, 25, 0), EntityType.MINECART_TNT);


		this.decrementUses(player, player.getItemInHand());
		this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2L));
		setEnabled(true);
	}

	@EventHandler
	public void explosionEvent(EntityExplodeEvent event) {
		if(enabled) {
			setEnabled(false);
			event.setCancelled(true);
			event.getLocation().getWorld().playEffect(event.getLocation(), Effect.EXPLOSION_HUGE, 20, 20);
			event.getLocation().getWorld().playSound(event.getLocation(), Sound.EXPLODE, 2, 10);

			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				if(player.getLocation().distance(event.getLocation()) < 10) {
					player.setHealth(player.getHealth() - 10);
				}
			}
		}
	}


	@Override
	public String getIdentifier() {
		return "AIR_STRIKE";
	}

	@Override
	public String getDisplayName() {
		return "&6&lAir Strike";
	}

	@Override
	public String getDescription() {
		return "Right click to kaboom.";
	}

	@Override
	public String getScoreboardPrefix() {
		return "&6&lAir Strike";
	}

	@Override
	public ItemStack getItem(int amount) {
		return ItemBuilder.of(Material.TNT)
				.amount(1)
				.enchant(Enchantment.DURABILITY, 10)
				.name(this.getDisplayName())
				.addToLore("&7" + this.getDescription())
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
