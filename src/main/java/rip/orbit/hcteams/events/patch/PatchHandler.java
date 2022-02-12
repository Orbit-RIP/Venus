package rip.orbit.hcteams.events.patch;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.ability.Ability;
import rip.orbit.hcteams.events.patch.listener.PatchListener;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.claims.Claim;
import rip.orbit.hcteams.util.CC;
import rip.orbit.hcteams.util.CuboidRegion;
import rip.orbit.hcteams.util.object.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 8:43 PM
 * HCTeams / rip.orbit.hcteams.events.pumpkinpatch
 */

@Getter
public class PatchHandler {

	public static String PATCH_TEAM_NAME = "FlowerFrenzy";
	public static String SEASONAL_NAME = "Flower Frenzy";
	public static String SEASONAL_MINED_NAME = "Flower";
	public static Material SEASONAL_MATERIAL = Material.RED_ROSE;
	public static String PATCH_PREFIX = CC.translate("&b&l[Flower Frenzy]");

	private List<Location> locations;
	private List<ItemStack> loot;
	private PatchConfigFile configFile;
	private long lastRespawned;

	public PatchHandler() {

		this.configFile = new PatchConfigFile(HCF.getInstance(), "patch", HCF.getInstance().getDataFolder().getAbsolutePath());
		this.locations = new ArrayList<>();
		this.loot = new ArrayList<>();

		for (Ability ability : HCF.getInstance().getAbilityHandler().getAbilities()) {
			loot.add(ability.getStack());
		}

		loot.add(new ItemBuilder(Material.DIAMOND_HELMET).name(CC.translate("&b&lFrenzy Helmet")).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
		loot.add(new ItemBuilder(Material.DIAMOND_CHESTPLATE).name(CC.translate("&b&lFrenzy Chestplate")).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
		loot.add(new ItemBuilder(Material.DIAMOND_LEGGINGS).name(CC.translate("&b&lFrenzy Leggings")).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
		loot.add(new ItemBuilder(Material.DIAMOND_BOOTS).name(CC.translate("&b&lFrenzy Boots")).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
		loot.add(new ItemBuilder(Material.DIAMOND_SWORD).name(CC.translate("&b&lFrenzy Sword")).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());

		YamlConfiguration config = this.configFile.getConfiguration();
		for (String s : config.getStringList("patches")) {
			String[] args = s.split(":");
			double x = Double.parseDouble(args[0]);
			double y = Double.parseDouble(args[1]);
			double z = Double.parseDouble(args[2]);
			String world = args[3];

			locations.add(new Location(Bukkit.getWorld(world), x, y, z));
		}

		Bukkit.getPluginManager().registerEvents(new PatchListener(), HCF.getInstance());

	}

	public void saveInfo() {
		YamlConfiguration config = this.configFile.getConfiguration();

		List<String> finished = new ArrayList<>();
		for (Location location : this.locations) {
			finished.add(location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getWorld().getName());
		}

		config.set("patches", finished);
		this.configFile.save();
	}

	public void respawn() {
		for (Location location : locations) {
			location.getBlock().setType(SEASONAL_MATERIAL);
		}
		Bukkit.broadcastMessage(CC.translate(PATCH_PREFIX + "&f All flowers have grown back."));
		PatchListener.mined = 0;
	}

	public void scanClaim() {

		Team team = HCF.getInstance().getTeamHandler().getTeam(PATCH_TEAM_NAME);

		for (Claim claim : team.getClaims()) {
			for (Location location : new CuboidRegion(PATCH_TEAM_NAME, claim.getMinimumPoint(), claim.getMaximumPoint())) {
				if (location.getBlock().getType() == SEASONAL_MATERIAL) {
					locations.add(location);
				}
			}
		}

		saveInfo();
	}

}
