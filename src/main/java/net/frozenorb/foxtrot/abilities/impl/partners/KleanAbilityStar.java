package net.frozenorb.foxtrot.abilities.impl.partners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import cc.fyre.proton.util.ItemBuilder;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class KleanAbilityStar extends Ability {

	public HashMap<UUID, Long> cooldown = new HashMap<>();

	private final HashMap<Player, Location> locations = new HashMap<>();
	private final HashMap<Player, Player> players = new HashMap<>();

	public KleanAbilityStar(Foxtrot plugin) {
		super(plugin);
	}

	@EventHandler
	public void on(PlayerInteractEvent event) {
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

		if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
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

		if (locations.get(player) == null) {
			player.sendMessage(ChatColor.RED + "You havent been hit in the last 5 seconds.");
			event.setCancelled(true);
			return;
		}

		decrementUses(player, player.getItemInHand());
		player.sendMessage("");
		player.sendMessage(CC.translate(" &6» &eYou have just used a &e&lTeleportation Star&e."));
		player.sendMessage(CC.translate(" &6» &eYou will be teleported in &e5 seconds&e."));
		player.sendMessage("");
		players.get(player).sendMessage(CC.translate(""));
		players.get(player).sendMessage(CC.translate(" &6» " + player.getName() + " &ewill be teleported to you in &65 seconds&e."));
		players.get(player).sendMessage(CC.translate(""));
		Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> player.teleport(players.get(player).getLocation()), 100);
		//Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> player.sendMessage(CC.translate("&6 » &eYou have used &b&lKlean's Teleportation Star")), 100);
		Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> players.remove(player), 1000);
		Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> locations.remove(player), 1000);
		this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5L));
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
		Team playerFaction = Foxtrot.getInstance().getTeamHandler().getTeam(attacker);
		Team targetFaction = Foxtrot.getInstance().getTeamHandler().getTeam(victim);

		if (targetFaction != null && playerFaction != null) {
			if (targetFaction == playerFaction || targetFaction.getAllies().contains(playerFaction.getUniqueId()))
				return;
		}

		if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(victim.getUniqueId()) || Foxtrot.getInstance().getPvPTimerMap().hasTimer(attacker.getUniqueId()))
			return;


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
		locations.putIfAbsent(victim, attacker.getLocation());
		players.putIfAbsent(victim, attacker);
		locations.put(victim, attacker.getLocation());
		players.put(victim, attacker);
	}


	@Override
	public String getIdentifier() {
		return "TELEPORT_STAR";
	}

	@Override
	public String getDisplayName() {
		return "&e&lTeleportation Star";
	}

	@Override
	public String getDescription() {
		return "&7Teleport to the last player who hit you within 5 seconds.";
	}

	@Override
	public String getScoreboardPrefix() {
		return "&e&lTeleportation Star";
	}

	@Override
	public ItemStack getItem(int amount) {
		return ItemBuilder.of(Material.NETHER_STAR)
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
