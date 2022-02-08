package net.frozenorb.foxtrot.pvpclasses.pvpclasses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.pvpclasses.PvPClass;
import net.frozenorb.foxtrot.pvpclasses.PvPClassHandler;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.nametag.FrozenNametagHandler;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.nebula.Nebula;

public class StingerClass extends PvPClass {

	private static final int TAG_SECONDS = 6;
	private static final int HIT_COOLDOWN_SECONDS = 20;
	private static final int MISS_COOLDOWN_SECONDS = 10;

	private static Map<String, Long> lastSpeedUsage = new HashMap<>();
	private static Map<String, Long> lastJumpUsage = new HashMap<>();
	private static Map<UUID, Long> throwCooldown = new HashMap<>();
	@Getter private static Map<UUID, Long> markedPlayers = new ConcurrentHashMap<>();

	public StingerClass() {
		super("Stinger", 5, Arrays.asList(Material.SUGAR, Material.FEATHER));
	}

	@Override
	public boolean qualifies(PlayerInventory armor) {
		return wearingAllArmor(armor) &&
		       armor.getHelmet().getType() == Material.IRON_HELMET &&
		       armor.getChestplate().getType() == Material.GOLD_CHESTPLATE &&
		       armor.getLeggings().getType() == Material.GOLD_LEGGINGS &&
		       armor.getBoots().getType() == Material.IRON_BOOTS;
	}

	@Override
	public void apply(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2), true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0), true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0), true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0), true);
	}

	@Override
	public void tick(Player player) {
		if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
		}

		if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
		}
	}

	@Override
	public boolean itemConsumed(Player player, Material material) {
		if (material == Material.SUGAR) { // SPEED
			if (lastSpeedUsage.containsKey(player.getName()) && lastSpeedUsage.get(player.getName()) > System.currentTimeMillis()) {
				long millisLeft = lastSpeedUsage.get(player.getName()) - System.currentTimeMillis();
				String msg = TimeUtils.formatIntoDetailedString((int) millisLeft / 1000);
				player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
				return (false);
			}

			lastSpeedUsage.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 3), true);
			return (true);
		} else { // JUMP BOOST
			if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
				player.sendMessage(ChatColor.RED + "You can't use this in spawn!");
				return (false);
			}

			if (lastJumpUsage.containsKey(player.getName()) && lastJumpUsage.get(player.getName()) > System.currentTimeMillis()) {
				long millisLeft = lastJumpUsage.get(player.getName()) - System.currentTimeMillis();
				String msg = TimeUtils.formatIntoDetailedString((int) millisLeft / 1000);
				player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
				return (false);
			}

			lastJumpUsage.put(player.getName(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 10, 3));
			return (true);
		}
	}

	@EventHandler
	public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof Egg && event.getEntity().getShooter() instanceof Player) {
			final Egg egg = (Egg) event.getEntity();
			final Player shooter = (Player) event.getEntity().getShooter();

			// Don't process if the player isn't in the Ranger class
			//if (!PvPClassHandler.hasKitOn(shooter, this)) {
			//	return;
			//}

			// Don't process if the player is in a safe-zone
			if (DTRBitmask.SAFE_ZONE.appliesAt(shooter.getLocation())) {
				shooter.sendMessage(ChatColor.RED + "You can't use this in spawn!");
				return;
			}

			long cooldown = throwCooldown.getOrDefault(shooter.getUniqueId(), 0L);

			if (cooldown > System.currentTimeMillis()) {
				event.setCancelled(true);
				shooter.sendMessage(ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD + TimeUtils.formatIntoDetailedString((int) (cooldown - System.currentTimeMillis()) / 1000) + ChatColor.RED + ".");
				return;
			}

			// Set snowball distance meta
			egg.setMetadata("ShotFromDistance", new FixedMetadataValue(Foxtrot.getInstance(), egg.getLocation()));

			// Add miss cooldown by default (gets set to hit cooldown if they hit a player)
			throwCooldown.put(shooter.getUniqueId(), System.currentTimeMillis() + (MISS_COOLDOWN_SECONDS * 1000L));
		}
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Egg)) {
			return;
		}

		final Egg egg = (Egg) event.getDamager();

		if (event.getEntity() instanceof Player && egg.getShooter() instanceof Player) {
			final Player shooter = (Player) ((Egg) event.getDamager()).getShooter();
			final Player victim = (Player) event.getEntity();


			// Don't process if the player isn't in the String class
			if (!PvPClassHandler.hasKitOn(shooter, this)) {
				return;
			}

			if (!canUseMark(shooter)) {
				return;
			}

			Team shooterTeam = Foxtrot.getInstance().getTeamHandler().getTeam(shooter);

			if (shooterTeam != null) {
				if (shooterTeam.isOwner(victim.getUniqueId()) || shooterTeam.isCoLeader(victim.getUniqueId()) || shooterTeam.isCaptain(victim.getUniqueId()) || shooterTeam.isMember(victim.getUniqueId())) {
					shooter.sendMessage(ChatColor.RED + "You cannot stun a player on your team!");
					event.setCancelled(true);
					return;
				}
			}

			// Don't process if the victim is in a safe-zone
			if (DTRBitmask.SAFE_ZONE.appliesAt(victim.getLocation())) {
				shooter.sendMessage(ChatColor.RED + "You can't stun a player who is in spawn!");
				return;
			}

			// Don't process if the shooter is in a safe-zone
			if (DTRBitmask.SAFE_ZONE.appliesAt(shooter.getLocation())) {
				shooter.sendMessage(ChatColor.RED + "You can't use this in spawn!");
				return;
			}

			// Add spawn-tag to both players
			SpawnTagHandler.addOffensiveSeconds(victim, SpawnTagHandler.getMaxTagTime());
			SpawnTagHandler.addOffensiveSeconds(shooter, SpawnTagHandler.getMaxTagTime());

			int distance = (int) ((Location) egg.getMetadata("ShotFromDistance").get(0).value()).distance(victim.getLocation());

			// Send shooter feedback
			if (PvPClassHandler.hasKitOn(victim, this)) {
				shooter.sendMessage(CC.translate(" &6» &fYou have stunned " + Nebula.getInstance().getProfileHandler().fromUuid(victim.getUniqueId()).getFancyName() + " &ffrom &6" + distance + " blocks&f."));
			}

			victim.sendMessage(CC.translate(" &6» &fYou have been stunned by a &6Stinger &ffor &6" + TAG_SECONDS + " &fseconds."));

			victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * TAG_SECONDS, 1));
			victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * TAG_SECONDS, 3));

			// Add 45 second throw cooldown (because the shooter hit another player)
			throwCooldown.put(shooter.getUniqueId(), System.currentTimeMillis() + (HIT_COOLDOWN_SECONDS * 1000L));

			// Mark the victim with a blue name
			// (handled in FoxtrotNametagProvider)
			markedPlayers.put(victim.getUniqueId(), System.currentTimeMillis() + (TAG_SECONDS * 1000L));

			// Trigger a nametag update and schedule one for when the tag is finished
			FrozenNametagHandler.reloadPlayer(victim);

			new BukkitRunnable() {
				@Override
				public void run() {
					if (victim.isOnline()) {
						FrozenNametagHandler.reloadPlayer(victim);
					}
				}
			}.runTaskLaterAsynchronously(Foxtrot.getInstance(), 20L * TAG_SECONDS);
		}
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		lastJumpUsage.remove(event.getPlayer().getName());
		lastSpeedUsage.remove(event.getPlayer().getName());
		throwCooldown.remove(event.getPlayer().getUniqueId());
		markedPlayers.remove(event.getPlayer().getUniqueId());
	}

	private boolean canUseMark(Player player) {
		if (Foxtrot.getInstance().getTeamHandler().getTeam(player) != null) {
			Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);

			int amount = 0;

			for (Player member : team.getOnlineMembers()) {
				if (PvPClassHandler.hasKitOn(member, this)) {
					amount++;

					if (amount > 2) {
						break;
					}
				}
			}

			if (amount > 2) {
				player.sendMessage(ChatColor.RED + "Your team has too many stingers.");
				return false;
			}
		}

		return true;
	}

}
