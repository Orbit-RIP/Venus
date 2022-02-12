package rip.orbit.hcteams.ability.generator;

import cc.fyre.proton.Proton;
import cc.fyre.proton.hologram.builder.HologramBuilder;
import cc.fyre.proton.hologram.construct.Hologram;
import cc.fyre.proton.util.TimeUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Data;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.ability.Ability;
import rip.orbit.hcteams.ability.items.NinjaStar;
import rip.orbit.hcteams.util.CC;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 14/08/2021 / 3:54 AM
 * HCTeams / rip.orbit.hcteams.ability.generator
 */

@Data
public class Generator {

	public static final MongoCollection<Document> collection = HCF.getInstance().getMongoPool().getDatabase(HCF.MONGO_DB_NAME).getCollection("generators");

	private final UUID uuid;

	private UUID owner;
	private int level = 1;
	private int tier = 1;
	private Location location;
	private Hologram hologram;

	private long lastSpawn;
	private int remaining = 0;

	private double x;
	private double y;
	private double z;
	private String world;

	public Generator(UUID uuid) {
		this.uuid = uuid;
		this.lastSpawn = System.currentTimeMillis();

		load();
	}

	public void load() {
		Document document = collection.find(Filters.eq("uuid", this.uuid.toString())).first();

		if (document == null) {
			return;
		}

		this.owner = UUID.fromString(document.getString("owner"));
		this.lastSpawn = document.getLong("lastSpawn");
		this.level = document.getInteger("level");
		this.tier = document.getInteger("tier");
		this.x = document.getDouble("x");
		this.y = document.getDouble("y");
		this.z = document.getDouble("z");
		this.world = document.getString("world");

		this.location = new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);

	}

	public void save() {
		Bukkit.getScheduler().runTaskAsynchronously(HCF.getInstance(), () -> {

			Document document = new Document();

			document.put("uuid", this.uuid.toString());
			document.put("owner", this.owner.toString());
			document.put("lastSpawn", this.lastSpawn);
			document.put("level", this.level);
			document.put("tier", this.tier);
			document.put("x", this.x);
			document.put("y", this.y);
			document.put("z", this.z);
			document.put("world", this.world);

			collection.replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new ReplaceOptions().upsert(true));

		});
	}

	public int nextPrice() {
		return this.level * 50;
	}

	public String displayName() {
		if (this.tier == 1) {
			return CC.translate("&8&lTier 1 Generator");
		} else if (this.tier == 2) {
			return CC.translate("&9&lTier 2 Generator");
		} else if (this.tier == 3) {
			return CC.translate("&e&lTier 3 Generator");
		}
		return "";
	}

	public ChatColor displayColor() {
		if (this.tier == 1) {
			return ChatColor.DARK_GRAY;
		} else if (this.tier == 2) {
			return ChatColor.BLUE;
		} else if (this.tier == 3) {
			return ChatColor.YELLOW;
		}
		return ChatColor.WHITE;
	}

	public Material displayMaterial() {
		if (this.tier == 1) {
			return Material.COAL_BLOCK;
		} else if (this.tier == 2) {
			return Material.LAPIS_BLOCK;
		} else if (this.tier == 3) {
			return Material.GOLD_BLOCK;
		}
		return Material.WOOL;
	}

	public void spawnAbilities() {
		List<Ability> abilities = new ArrayList<>();

		for (Ability ability : HCF.getInstance().getAbilityHandler().getAbilities()) {
			if (ability instanceof NinjaStar)
				continue;
			abilities.add(ability);

		}

		abilities.addAll(HCF.getInstance().getAbilityHandler().getPocketbards());

		int size = abilities.size();

		for (int i = 0; i < getLevel(); i++) {
			int random = ThreadLocalRandom.current().nextInt(size);
			Ability chosen = abilities.get(random);

			Bukkit.getWorld(getWorld()).dropItemNaturally(getLocation(), chosen.getStack().clone());

			if (HCF.getInstance().getReceiveGeneratorMessagesMap().isToggled(this.owner)) {
				Player player = Bukkit.getPlayer(this.owner);
				if (player != null) {
					player.sendMessage(CC.translate("&aYour " + displayName() + " &agenerator has just produced a " + chosen.displayName()));
				}
			}
		}
		setRemaining(60 * getDelay());
	}

	public void createHologram() {
		if (this.hologram != null) {
			this.hologram.delete();
		}

		new BukkitRunnable() {
			@Override
			public void run() {

				Location loc = Generator.this.location.clone().add(0, 1.5, 0);
				loc.setX(loc.getX() + 0.5);
				loc.setZ(loc.getZ() + 0.5);
				HologramBuilder create = Proton.getInstance().getHologramHandler().createHologram();
				create.addLines(CC.translate("&7&m------------------"));
				create.addLines(CC.translate(displayName()));
				create.addLines(CC.translate("&7&m------------------"));
				create.addLines(CC.translate("&7Spawn Time: " + TimeUtils.formatIntoMMSS(getRemaining())));
				create.addLines(CC.translate("&7Right Click to view more info."));
				create.addLines(CC.translate("&7&m------------------"));

				create.at(loc);
				Hologram hologram = create.build();
				Generator.this.setHologram(hologram);
			}
		}.runTaskLater(HCF.getInstance(), 12);
	}

	public int getDelay() {
		if (this.getTier() == 1) {
			if (this.getLevel() == 1) {
				return 15;
			} else if (this.getLevel() == 2) {
				return 14;
			} else if (this.getLevel() == 3) {
				return 13;
			} else if (this.getLevel() == 4) {
				return 12;
			} else if (this.getLevel() == 5) {
				return 10;
			}
		} else if (this.getTier() == 2) {
			if (this.getLevel() == 1) {
				return 12;
			} else if (this.getLevel() == 2) {
				return 11;
			} else if (this.getLevel() == 3) {
				return 10;
			} else if (this.getLevel() == 4) {
				return 9;
			} else if (this.getLevel() == 5) {
				return 8;
			}
		} else if (this.getTier() == 3) {
			if (this.getLevel() == 1) {
				return 10;
			} else if (this.getLevel() == 2) {
				return 9;
			} else if (this.getLevel() == 3) {
				return 8;
			} else if (this.getLevel() == 4) {
				return 7;
			} else if (this.getLevel() == 5) {
				return 6;
			}
		}
		return 15;
	}

}
