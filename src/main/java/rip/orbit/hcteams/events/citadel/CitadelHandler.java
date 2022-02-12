package rip.orbit.hcteams.events.citadel;

import cc.fyre.proton.Proton;
import com.mongodb.BasicDBObject;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.events.citadel.events.CitadelCapturedEvent;
import rip.orbit.hcteams.events.citadel.file.CitadelConfigFile;
import rip.orbit.hcteams.events.citadel.listeners.CitadelListener;
import rip.orbit.hcteams.events.citadel.tasks.CitadelSaveTask;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.claims.Claim;
import rip.orbit.hcteams.team.dtr.DTRBitmask;
import rip.orbit.hcteams.util.CC;
import rip.orbit.hcteams.util.CuboidRegion;

import java.util.*;

@Getter
public class CitadelHandler {

	public static String PREFIX = ChatColor.DARK_PURPLE + "[Citadel]";
	
	private Set<ObjectId> cappers;
	private Date lootable;
	private long cappedAt = 0;
	private CitadelConfigFile configFile;

	private Set<Location> citadelChests = new HashSet<>();
	private List<ItemStack> citadelLoot = new ArrayList<>();

	public CitadelHandler() {
		configFile = new CitadelConfigFile(HCF.getInstance(), "citadelloot", HCF.getInstance().getDataFolder().getAbsolutePath());
		loadCitadelInfo();
		HCF.getInstance().getServer().getPluginManager().registerEvents(new CitadelListener(), HCF.getInstance());

		(new CitadelSaveTask()).runTaskTimerAsynchronously(HCF.getInstance(), 0L, 20 * 60 * 5);
	}

	public void loadCitadelInfo() {
		try {
			this.cappers = new HashSet<>();

			YamlConfiguration con = getConfigFile().getConfiguration();

			for (String s : con.getStringList("chests")) {
				String[] args = s.split(":");
				double x = Double.parseDouble(args[0]);
				double y = Double.parseDouble(args[1]);
				double z = Double.parseDouble(args[2]);
				String world = args[3];

				citadelChests.add(new Location(Bukkit.getWorld(world), x, y, z));
			}

			this.cappedAt = con.getLong("cappedAt");
			try {
				this.cappers = Collections.singleton(new ObjectId(con.getString("capper")));
			} catch (Exception ignored) {
				System.out.println("Didnt load any cappers for citadel.");
			}

			for (String sec : getConfigFile().getConfiguration().getConfigurationSection("loot").getKeys(false)) {
				String main = "loot.";
				ItemStack i = new ItemStack(Material.valueOf(con.getString(main + sec + ".Material")));
//                            .displayName(CC.chat(con.getString(main + sec + ".Name").replaceAll("§", "&")))
//                            .setLore(CC.translate(con.getStringList(main + sec + ".Lore")))
//                            .data(((short)con.getInt(main + sec + ".Data")))
//                            .enchant(Enchantment.getByName(con.getString(main + sec + ".Enchants.Type")), con.getInt(main + sec + ".Level"))
				ItemMeta meta = i.getItemMeta();
				if (con.contains("loot." + sec + ".Name")) {
					meta.setDisplayName(CC.translate(con.getString(main + sec + ".Name").replaceAll("§", "&")));
				}

				if (con.contains("loot." + sec + ".Lore")) {
					meta.setLore(CC.translate(con.getStringList(main + sec + ".Lore")));
				}
				i.setDurability(((short) con.getInt(main + sec + ".Data")));
				i.setAmount(con.getInt(main + sec + ".Amount"));
				i.setItemMeta(meta);
				if (con.contains("loot." + sec + ".Enchants")) {
					for (String s : con.getConfigurationSection(main + sec + ".Enchants").getKeys(false)) {
						try {
							i.addEnchantment(Enchantment.getByName(con.getString(main + sec + ".Enchants." + s + ".Type")), con.getInt(main + sec + ".Enchants." + s + ".Level"));
						} catch (IllegalArgumentException e) {
							i.addUnsafeEnchantment(Enchantment.getByName(con.getString(main + sec + ".Enchants." + s + ".Type")), con.getInt(main + sec + ".Enchants." + s + ".Level"));

						}
					}
				}
				citadelLoot.add(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveCitadelInfo() {
		try {
			BasicDBObject dbo = new BasicDBObject();

			List<String> chests = new ArrayList<>();
			for (Location location : citadelChests) {
				chests.add(location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getWorld().getName());
			}
			getConfigFile().getConfiguration().set("chests", chests);
			getConfigFile().getConfiguration().set("cappedAt", this.cappedAt);
			for (ObjectId capper : this.cappers) {
				getConfigFile().getConfiguration().set("capper", capper.toString());
			}

			getConfigFile().getConfiguration().set("loot", null);
			for (ItemStack lootItem : citadelLoot) {
				UUID unique = UUID.randomUUID();
				String main = "loot.";
				getConfigFile().getConfiguration().set(main + "" + unique + ".Material", lootItem.getType().name());
				getConfigFile().getConfiguration().set(main + "" + unique + ".Lore", lootItem.getItemMeta().getLore());
				getConfigFile().getConfiguration().set(main + "" + unique + ".Name", lootItem.getItemMeta().getDisplayName());
				getConfigFile().getConfiguration().set(main + "" + unique + ".Data", lootItem.getDurability());
				getConfigFile().getConfiguration().set(main + "" + unique + ".Amount", lootItem.getAmount());
				if (!lootItem.getEnchantments().isEmpty()) {
					for (Enchantment e : lootItem.getEnchantments().keySet()) {
						UUID id = UUID.randomUUID();
						getConfigFile().getConfiguration().set(main + "" + unique + ".Enchants." + id + ".Type", e.getName());
						getConfigFile().getConfiguration().set(main + "" + unique + ".Enchants." + id + ".Level", lootItem.getEnchantmentLevel(e));
					}
				}
				getConfigFile().save();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resetCappers() {
		this.cappers.clear();
	}

	public void addCapper(ObjectId capper) {
		this.cappers.add(capper);
		this.lootable = generateLootableDate();

		HCF.getInstance().getServer().getPluginManager().callEvent(new CitadelCapturedEvent(capper));
		saveCitadelInfo();
	}

	public boolean canLootCitadel(Player player) {
		Team team = HCF.getInstance().getTeamHandler().getTeam(player);
		if (this.cappedAt < System.currentTimeMillis()) {
			return false;
		}
		return ((team != null && cappers.contains(team.getUniqueId())));
	}

	// Credit to http://stackoverflow.com/a/3465656 on StackOverflow.
	private Date generateLootableDate() {
		Calendar date = Calendar.getInstance();
		int diff = Calendar.TUESDAY - date.get(Calendar.DAY_OF_WEEK);

		if (diff <= 0) {
			diff += 7;
		}

		date.add(Calendar.DAY_OF_MONTH, diff);

		// 11:59 PM
		date.set(Calendar.HOUR_OF_DAY, 23);
		date.set(Calendar.MINUTE, 59);
		date.set(Calendar.SECOND, 59);

		return (date.getTime());
	}

	public void scanLoot() {
		citadelChests.clear();

		for (Team team : HCF.getInstance().getTeamHandler().getTeams()) {
			if (team.getOwner() != null) {
				continue;
			}

			if (team.hasDTRBitmask(DTRBitmask.CITADEL)) {
				for (Claim claim : team.getClaims()) {
					for (Location location : new CuboidRegion("Citadel", claim.getMinimumPoint(), claim.getMaximumPoint())) {
						if (location.getBlock().getType() == Material.CHEST) {
							citadelChests.add(location);
						}
					}
				}
			}
		}
	}

	public int respawnCitadelChests() {
		int respawned = 0;

		for (Location chest : citadelChests) {
			if (respawnCitadelChest(chest)) {
				respawned++;
			}
		}

		return (respawned);
	}

	public boolean respawnCitadelChest(Location location) {
		BlockState blockState = location.getBlock().getState();

		if (blockState instanceof Chest) {
			Chest chest = (Chest) blockState;

			chest.getBlockInventory().clear();
			chest.getBlockInventory().addItem(citadelLoot.get(Proton.RANDOM.nextInt(citadelLoot.size())));
			chest.getBlockInventory().addItem(citadelLoot.get(Proton.RANDOM.nextInt(citadelLoot.size())));
			chest.getBlockInventory().addItem(citadelLoot.get(Proton.RANDOM.nextInt(citadelLoot.size())));
			return (true);
		} else {
			HCF.getInstance().getLogger().warning("Citadel chest defined at [" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "] isn't a chest!");
			return (false);
		}
	}

}