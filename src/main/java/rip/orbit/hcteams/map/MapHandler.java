package rip.orbit.hcteams.map;

import cc.fyre.proton.Proton;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.events.EventHandler;
import rip.orbit.hcteams.events.koth.KOTH;
import rip.orbit.hcteams.listener.BorderListener;
import rip.orbit.hcteams.map.deathban.DeathbanListener;
import rip.orbit.hcteams.map.duel.DuelHandler;
import rip.orbit.hcteams.map.game.GameHandler;
import rip.orbit.hcteams.map.game.arena.select.SelectionListeners;
import rip.orbit.hcteams.map.game.impl.ffa.FFAListeners;
import rip.orbit.hcteams.map.game.impl.spleef.SpleefListeners;
import rip.orbit.hcteams.map.game.impl.sumo.SumoListeners;
import rip.orbit.hcteams.map.game.listener.GameListeners;
import rip.orbit.hcteams.map.killstreaks.KillstreakHandler;
import rip.orbit.hcteams.map.kits.KitManager;
import rip.orbit.hcteams.map.stats.StatsHandler;
import rip.orbit.hcteams.scoreboard.FoxtrotScoreboardConfiguration;
import rip.orbit.hcteams.server.Deathban;
import rip.orbit.hcteams.server.ServerHandler;
import rip.orbit.hcteams.team.track.TeamActionTracker;
import rip.orbit.hcteams.util.CC;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MapHandler {

	private transient File mapInfo;

	private MapConfigFile configFile;
	@Getter
	private boolean kitMap;
	@Getter
	private int allyLimit;
	@Getter
	private int teamSize;
	@Getter
	private long regenTimeDeath;
	@Getter
	private long regenTimeRaidable;
	@Getter
	private String scoreboardTitle;
	@Getter
	private String mapStartedString;
	@Getter
	private double baseLootingMultiplier;
	@Getter
	private double level1LootingMultiplier;
	@Getter
	private double level2LootingMultiplier;
	@Getter
	private double level3LootingMultiplier;
	@Getter
	private boolean craftingGopple;
	@Getter
	private boolean craftingReducedMelon;
	@Getter
	private int goppleCooldown;
	@Getter
	private int minForceInviteMembers = 10;
	@Getter
	private String endPortalLocation;
	@Getter
	private boolean fastSmeltEnabled;
	@Getter
	@Setter
	private int netherBuffer;
	@Getter
	@Setter
	private int worldBuffer;
	@Getter
	private float dtrIncrementMultiplier;

	// Kit-Map only stuff:
	@Getter
	private StatsHandler statsHandler;
	@Getter
	private KillstreakHandler killstreakHandler;
	@Getter
	private KitManager kitManager;
	@Getter
	private DuelHandler duelHandler;
	@Getter
	private GameHandler gameHandler;

	// Other stuff:

	public MapHandler() {
	}

	public void load() {

		this.configFile = new MapConfigFile(HCF.getInstance(), "mapConfig", HCF.getInstance().getDataFolder().getAbsolutePath());

		reloadConfig();

		Proton.getInstance().getScoreboardHandler().setConfiguration(FoxtrotScoreboardConfiguration.create());

		Iterator<Recipe> recipeIterator = HCF.getInstance().getServer().recipeIterator();

		while (recipeIterator.hasNext()) {
			Recipe recipe = recipeIterator.next();

			// Disallow the crafting of gopples.
			if (!craftingGopple && recipe.getResult().getDurability() == (short) 1 && recipe.getResult().getType() == org.bukkit.Material.GOLDEN_APPLE) {
				recipeIterator.remove();
			}

			// Remove vanilla glistering melon recipe
			if (craftingReducedMelon && recipe.getResult().getType() == Material.SPECKLED_MELON) {
				recipeIterator.remove();
			}

			if (HCF.getInstance().getConfig().getBoolean("rodPrevention") && recipe.getResult().getType() == Material.FISHING_ROD) {
				recipeIterator.remove();
			}

			if (recipe.getResult().getType() == Material.EXPLOSIVE_MINECART) {
				recipeIterator.remove();
			}
		}

		// add our glistering melon recipe
		if (craftingReducedMelon) {
			HCF.getInstance().getServer().addRecipe(new ShapelessRecipe(new ItemStack(Material.SPECKLED_MELON)).addIngredient(Material.MELON).addIngredient(Material.GOLD_NUGGET));
		}

		ShapedRecipe nametagRecipe = new ShapedRecipe(new ItemStack(Material.NAME_TAG));
		ShapedRecipe saddleRecipe = new ShapedRecipe(new ItemStack(Material.SADDLE));
		ShapedRecipe horseArmorRecipe = new ShapedRecipe(new ItemStack(Material.DIAMOND_BARDING));

		nametagRecipe.shape(" I ",
				" P ",
				" S ");
		nametagRecipe.setIngredient('I', Material.INK_SACK);
		nametagRecipe.setIngredient('P', Material.PAPER);
		nametagRecipe.setIngredient('S', Material.STRING);

		saddleRecipe.shape("  L",
				"LLL",
				"B B");
		saddleRecipe.setIngredient('L', Material.LEATHER);
		saddleRecipe.setIngredient('B', Material.LEASH);

		horseArmorRecipe.shape(" SD",
				"BBL",
				"LL ");
		horseArmorRecipe.setIngredient('S', Material.SADDLE);
		horseArmorRecipe.setIngredient('D', Material.DIAMOND);
		horseArmorRecipe.setIngredient('B', Material.DIAMOND_BLOCK);
		horseArmorRecipe.setIngredient('L', Material.LEATHER);

		HCF.getInstance().getServer().addRecipe(nametagRecipe);
		HCF.getInstance().getServer().addRecipe(saddleRecipe);
		HCF.getInstance().getServer().addRecipe(horseArmorRecipe);

		statsHandler = new StatsHandler();
		duelHandler = new DuelHandler();
		gameHandler = new GameHandler();
		if (isKitMap() || HCF.getInstance().getServerHandler().isVeltKitMap()) {

			killstreakHandler = new KillstreakHandler();
			kitManager = new KitManager();

			// start a KOTH after 5 minutes of uptime
			Bukkit.getScheduler().runTaskLater(HCF.getInstance(), () -> {
				EventHandler kothHandler = HCF.getInstance().getEventHandler();
				List<KOTH> koths = new ArrayList<>(kothHandler.getEvents().stream().filter(e -> e instanceof KOTH).map(e -> (KOTH) e).collect(Collectors.toList()));

				if (koths.isEmpty()) {
					return;
				}

				KOTH selected = koths.get(Proton.RANDOM.nextInt(koths.size()));
				selected.activate();
			}, 5 * 60 * 20);

			TeamActionTracker.setDatabaseLogEnabled(false);
		}

		HCF.getInstance().getServer().getPluginManager().registerEvents(new GameListeners(), HCF.getInstance());
		HCF.getInstance().getServer().getPluginManager().registerEvents(new FFAListeners(), HCF.getInstance());
		HCF.getInstance().getServer().getPluginManager().registerEvents(new SumoListeners(), HCF.getInstance());
		HCF.getInstance().getServer().getPluginManager().registerEvents(new SpleefListeners(), HCF.getInstance());
		HCF.getInstance().getServer().getPluginManager().registerEvents(new SelectionListeners(), HCF.getInstance());
		HCF.getInstance().getServer().getPluginManager().registerEvents(new DeathbanListener(), HCF.getInstance());
	}

	public void save() {

	}

	public void reloadConfig() {

		try {
			YamlConfiguration config = this.configFile.getConfiguration();

			this.kitMap = config.getBoolean("kitmap-mode");
			this.allyLimit = config.getInt("ally-limit", 0);
			this.teamSize = config.getInt("team-size");
			this.regenTimeDeath = TimeUnit.MINUTES.toMillis(config.getInt("regenTimeDeath", 60));
			this.regenTimeRaidable = TimeUnit.MINUTES.toMillis(config.getInt("regenTimeRaidable", 60));
			this.scoreboardTitle = CC.translate(HCF.getInstance().getConfig().getString("scoreboard.tittle", "change this shit"));
			this.mapStartedString = config.getString("mapStartedString");
			ServerHandler.WARZONE_RADIUS = config.getInt("warzone", 1000);
			BorderListener.BORDER_SIZE = config.getInt("border", 3000);
			this.goppleCooldown = config.getInt("goppleCooldown");
			this.netherBuffer = config.getInt("netherBuffer");
			this.worldBuffer = config.getInt("worldBuffer");
			this.endPortalLocation = config.getString("endPortalLocation");
			this.fastSmeltEnabled = config.getBoolean("fastSmeltEnabled", true);

			this.baseLootingMultiplier = config.getDouble("looting.base");
			this.level1LootingMultiplier = config.getDouble("looting.level1");
			this.level2LootingMultiplier = config.getDouble("looting.level2");
			this.level3LootingMultiplier = config.getDouble("looting.level3");

			this.craftingGopple = config.getBoolean("crafting.gopple", false);
			this.craftingReducedMelon = config.getBoolean("crafting.reducedMelon", true);

			if (config.contains("deathban")) {
				Deathban.load(config, config.getConfigurationSection("deathban"));
			}

			if (config.contains("minForceInviteMembers")) {
				minForceInviteMembers = config.getInt("minForceInviteMembers", 5);
			}

			this.dtrIncrementMultiplier = (float) config.getDouble("dtrIncrementMultiplier", 4.5F);
		} catch (Exception ignored) {
			getDefaults();
		}

	}

	private void getDefaults() {

		YamlConfiguration config = this.configFile.getConfiguration();

		config.set("kitMap", false);
		config.set("allyLimit", 0);
		config.set("teamSize", 8);
		config.set("regenTimeDeath", 60);
		config.set("regenTimeRaidable", 60);
		config.set("scoreboardTitle", "edit on config.yml");
		config.set("mapStartedString", "Map 3 - Started January 31, 2015");
		config.set("warzone", 1000);
		config.set("netherBuffer", 150);
		config.set("worldBuffer", 300);
		config.set("endPortalLocation", "1000, 1000");
		config.set("border", 3000);
		config.set("goppleCooldown", TimeUnit.HOURS.toMinutes(4));
		config.set("fastSmeltEnabled", true);
		config.set("looting.base", 1D);
		config.set("looting.level1", 1.2D);
		config.set("looting.level2", 1.4D);
		config.set("looting.level3", 2D);
		config.set("crafting.gopple", true);
		config.set("crafting.reducedMelon", true);
		config.set("deathban.Partner", 120);
		config.set("deathban.Famous", 120);
		config.set("deathban.Orbit", 120);
		config.set("deathban.Youtuber", 120);
		config.set("deathban.Platinum", 120);
		config.set("deathban.Diamond", 120);
		config.set("deathban.Gold", 120);
		config.set("deathban.Iron", 120);
		config.set("deathban.DEFAULT", 120);
		config.set("minForceInviteMembers", 5);
		config.set("dtrIncrementMultiplier", 4.5F);

		this.configFile.save();
	}

	public void saveBorder() {
		YamlConfiguration config = this.configFile.getConfiguration();

		try {
			config.set("border", BorderListener.BORDER_SIZE); // update the border
			this.configFile.save();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveNetherBuffer() {
		YamlConfiguration config = this.configFile.getConfiguration();

		try {
			config.set("netherBuffer", HCF.getInstance().getMapHandler().getNetherBuffer()); // update the nether buffer
			this.configFile.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveWorldBuffer() {

		YamlConfiguration config = this.configFile.getConfiguration();

		try {
			config.set("worldBuffer", HCF.getInstance().getMapHandler().getWorldBuffer()); // update the world buffer
			this.configFile.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}