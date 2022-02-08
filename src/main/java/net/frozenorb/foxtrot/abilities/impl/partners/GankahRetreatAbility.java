package net.frozenorb.foxtrot.abilities.impl.partners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import net.minecraft.util.com.google.common.primitives.Ints;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import cc.fyre.proton.util.ItemBuilder;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GankahRetreatAbility extends Ability {


	public HashMap<UUID, Long> cooldown = new HashMap<>();
	private final HashMap<Player, Location> enderpearl = new HashMap<>();


	public GankahRetreatAbility(Foxtrot plugin) {
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

		if (player.getItemInHand().getAmount() > 1) {
			event.setCancelled(true);
			player.sendMessage(CC.translate("&cYou have to unstack this item to be able to use it."));
			return;
		}

		event.setCancelled(true);

		Team playerSpawn = LandBoard.getInstance().getTeam(player.getLocation());
		if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
			event.setCancelled(true);
			return;
		}

		if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.CITADEL)) {
			player.sendMessage(CC.translate("&cYou cannot use this ability here."));
			event.setCancelled(true);
			return;
		}

		if (this.getRemainingUses(player.getItemInHand()) <= 0) {
			player.playSound(player.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
			player.setItemInHand(new ItemStack(Material.AIR, 1));
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
		if (enderpearl.get(player) == null) {
			player.sendMessage(ChatColor.RED + "You havent used any ender pearls in the last 5 seconds");
			event.setCancelled(true);
			return;
		}

		player.teleport(enderpearl.get(player));
		player.sendMessage(CC.translate("&6» &eYou have used " + getDisplayName() + "&e."));

		if (decrementUses(player, player.getItemInHand())) {
			player.playSound(player.getLocation(), Sound.ITEM_BREAK, 5F, 5F);
			player.setItemInHand(new ItemStack(Material.AIR, 1));
		}

		this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5L));
	}


	@EventHandler
	public void onPearlLand(ProjectileLaunchEvent event){
		if (!(event.getEntity().getShooter() instanceof Player)) {
			return;
		}

		final Projectile entity = event.getEntity();
		final Player player = (Player) entity.getShooter();


		if (entity instanceof EnderPearl) {
			enderpearl.put(player, player.getLocation());
			Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> enderpearl.remove(player), 300);
		}

	}

	@Override
	public String getIdentifier() {
		return "RETREAT";
	}

	@Override
	public String getDisplayName() {
		return "&c&lRetreat";
	}

	@Override
	public String getDescription() {
		return "Right click to teleport to your previous location after pearling.";
	}

	@Override
	public String getScoreboardPrefix() {
		return "&c&lRetreat";
	}

	@Override
	public ItemStack getItem(int amount) {
		return ItemBuilder.of(Material.ENDER_PEARL)
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
