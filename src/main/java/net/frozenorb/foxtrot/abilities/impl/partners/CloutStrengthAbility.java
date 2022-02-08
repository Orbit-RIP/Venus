package net.frozenorb.foxtrot.abilities.impl.partners;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import cc.fyre.proton.util.ItemBuilder;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CloutStrengthAbility extends Ability {
	public CloutStrengthAbility(Foxtrot plugin) {
		super(plugin);
	}
	public HashMap<UUID, Long> cooldown = new HashMap<>();

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
				player.sendMessage(CC.translate("&cYou cannot use this here."));
				event.setCancelled(true);
				return;
			}
		}

		if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
			event.setCancelled(true);
			return;
		}

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

		if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) != null) {
			long remaining = this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();

			if (remaining > 0L) {
				player.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName()) + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
				event.setCancelled(true);
				player.updateInventory();
				return;
			}
		}

		this.decrementUses(player, player.getItemInHand());

		player.updateInventory();

		player.sendMessage(CC.translate(" &6» &eYou have received &c&lStrength II &efor &65 seconds&e."));
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5, 1));

		this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3L));
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
		return "STRENGTH_2";
	}

	@Override
	public String getDisplayName() {
		return "&cStrength 2";
	}

	@Override
	public String getDescription() {
		return "&7Right click to receive Strength 2 for 5 seconds!";
	}

	@Override
	public ItemStack getItem(int amount) {
		return ItemBuilder.of(Material.BLAZE_POWDER)
				.amount(amount)
				.enchant(Enchantment.DURABILITY, 10)
				.name(getDisplayName())
				.addToLore("&7" + getDescription())
				.build();
	}
	@Override
	public HashMap<UUID, Long> getCooldown() {
		return this.cooldown;
	}
	@Override
	public int getUses() {
		return -1;
	}

	@Override
	public String getScoreboardPrefix() {
		return "&4&lStrength 2";
	}
}
