package net.frozenorb.foxtrot.abilities.impl.partners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.util.CC;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import cc.fyre.proton.util.ItemBuilder;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MedkitAbility extends Ability {
	public HashMap<UUID, Long> cooldown = new HashMap<>();
	public HashMap<Player, Double> health = new HashMap<>();

	public MedkitAbility(Foxtrot plugin) {
		super(plugin);
	}

	@EventHandler
	public void onMed(PlayerInteractEvent event){
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
		if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) != null) {
			long remaining = this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();

			if (remaining > 0L) {
				player.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
				event.setCancelled(true);
				player.updateInventory();
				return;
			}
		}


		player.sendMessage(CC.translate("&6» &eYou have used &6&lMed Kit&e."));


		player.setMetadata("medkit", new FixedMetadataValue(Foxtrot.getInstance(), "medkit"));
		decrementUses(player, player.getItemInHand());
		this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3));
		Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> {
			if (player.getHealth() + health.get(player) >= 20) {
				player.setHealth(20);
			} else {
				player.setHealth(player.getHealth() + this.health.get(player));
			}
		}, 5 * 20);
		Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> player.removeMetadata("medkit", Foxtrot.getInstance()), 5 * 20);
		Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> health.remove(player), 10 * 20);

	}

	@EventHandler
	public void onPlayerHit(final EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		if (!(event.getDamager() instanceof Player)) {
			return;
		}

		final Player attacker = (Player) event.getDamager();
		final Player victim = (Player) event.getEntity();
		if (attacker.hasMetadata("medkit")) {
			if (this.cooldown.containsKey(attacker.getUniqueId()) && this.cooldown.get(attacker.getUniqueId()) != null) {
				health.putIfAbsent(attacker, 0.0);
				health.put(attacker, health.get(attacker) + victim.getLastDamage());
			}

		}
	}


	@Override
	public String getIdentifier() {
		return "MED_KIT";
	}

	@Override
	public String getDisplayName() {
		return "&c&lMed Kit";
	}

	@Override
	public String getDescription() {
		return "Heal yourself by dealing damage to another opponent for 5 seconds.";
	}

	@Override
	public String getScoreboardPrefix() {
		return "&c&lMed Kit";
	}

	@Override
	public ItemStack getItem(int amount) {
		return ItemBuilder.of(Material.GHAST_TEAR)
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
