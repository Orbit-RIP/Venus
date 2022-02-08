package net.frozenorb.foxtrot;

import cc.fyre.proton.Proton;
import com.comphenix.protocol.ProtocolLibrary;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.abilities.managers.AbilityManager;
//import net.frozenorb.foxtrot.chat.ChatHandler;
//import net.frozenorb.foxtrot.crate.CrateHandler;
import net.frozenorb.foxtrot.map.bounty.BountyListener;
import net.frozenorb.foxtrot.map.kits.listener.KitEditorListener;
import net.frozenorb.foxtrot.events.region.oremtn.OreHandler;
import net.frozenorb.foxtrot.deathmessage.DeathMessageHandler;
import net.frozenorb.foxtrot.events.EventHandler;
import net.frozenorb.foxtrot.events.citadel.CitadelHandler;
import net.frozenorb.foxtrot.events.conquest.ConquestHandler;
import net.frozenorb.foxtrot.events.ktk.KillTheKing;
import net.frozenorb.foxtrot.events.ktk.gui.KillTheKingGUI;
import net.frozenorb.foxtrot.events.ktk.listener.KillTheKingListener;
import net.frozenorb.foxtrot.events.region.carepackage.CarePackageHandler;
import net.frozenorb.foxtrot.events.region.glowmtn.GlowHandler;
import net.frozenorb.foxtrot.listener.*;
import net.frozenorb.foxtrot.misc.crackers.Cracker;
import net.frozenorb.foxtrot.misc.crackers.listener.CrackerListener;
import net.frozenorb.foxtrot.misc.game.arena.select.SelectionListeners;
import net.frozenorb.foxtrot.misc.game.impl.ffa.FFAListeners;
import net.frozenorb.foxtrot.misc.game.impl.shuffle.ShuffleListeners;
import net.frozenorb.foxtrot.misc.game.impl.spleef.SpleefListeners;
import net.frozenorb.foxtrot.misc.game.impl.sumo.SumoListeners;
import net.frozenorb.foxtrot.misc.game.listener.GameListeners;
import net.frozenorb.foxtrot.lunar.LunarHandler;
import net.frozenorb.foxtrot.map.MapHandler;
import net.frozenorb.foxtrot.misc.coupons.CouponHandler;
import net.frozenorb.foxtrot.misc.creditshop.listener.CreditShopListener;
import net.frozenorb.foxtrot.misc.giveaway.GiveawayHandler;
import net.frozenorb.foxtrot.misc.poll.PollHandler;
import net.frozenorb.foxtrot.partner.Partner;
import net.frozenorb.foxtrot.partner.listener.PartnerListener;
import net.frozenorb.foxtrot.redeem.RedeemHandler;
import net.frozenorb.foxtrot.server.DiscordLogger;
import net.frozenorb.foxtrot.task.ClearItemTask;
import net.frozenorb.foxtrot.team.NoFactionFocusFaction;
import net.frozenorb.foxtrot.util.*;
import net.frozenorb.foxtrot.persist.RedisSaveTask;
import net.frozenorb.foxtrot.persist.maps.*;
import net.frozenorb.foxtrot.persist.maps.statistics.*;
import net.frozenorb.foxtrot.pvpclasses.PvPClassHandler;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.server.ServerHandler;
import net.frozenorb.foxtrot.tab.FoxtrotTabLayoutProvider;
import net.frozenorb.foxtrot.team.TeamHandler;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.commands.team.TeamClaimCommand;
import net.frozenorb.foxtrot.team.commands.team.subclaim.TeamSubclaimCommand;
import net.frozenorb.foxtrot.team.dtr.DTRHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.ScoreboardManager;
import rip.orbit.nebula.Nebula;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
public class Foxtrot extends JavaPlugin {

    public static String MONGO_DB_NAME = "HCTeams";

    private static Foxtrot instance;

    private MongoClient mongoPool;

    private PvPClassHandler pvpClassHandler;
    private ScoreboardManager scoreboardManager;
    private TeamHandler teamHandler;
    private ServerHandler serverHandler;
    private MapHandler mapHandler;
    private CitadelHandler citadelHandler;
    private EventHandler eventHandler;
    private ConquestHandler conquestHandler;
    private GlowHandler glowHandler;
    private OreHandler oreHandler;
    private LunarHandler lunarHandler;
//    private CrateHandler crateHandler;
//    private ChatHandler chatHandler;
    private DiscordLogger discordLogger;

    private PlaytimeMap playtimeMap;
    private OppleMap oppleMap;
    private DeathbanMap deathbanMap;
    private PvPTimerMap PvPTimerMap;
    private StartingPvPTimerMap startingPvPTimerMap;
    private DeathsMap deathsMap;
    private KillsMap killsMap;
    private ChatModeMap chatModeMap;
    private FishingKitMap fishingKitMap;
    private ToggleGlobalChatMap toggleGlobalChatMap;
    private ChatSpyMap chatSpyMap;
    private DiamondMinedMap diamondMinedMap;
    private GoldMinedMap goldMinedMap;
    private IronMinedMap ironMinedMap;
    private CoalMinedMap coalMinedMap;
    private RedstoneMinedMap redstoneMinedMap;
    private LapisMinedMap lapisMinedMap;
    private EmeraldMinedMap emeraldMinedMap;
    private FirstJoinMap firstJoinMap;
    private LastJoinMap lastJoinMap;
    private SoulboundLivesMap soulboundLivesMap;
    private ReclaimMap reclaimMap;
    private FriendLivesMap friendLivesMap;
    private BaseStatisticMap enderPearlsUsedMap;
    private BaseStatisticMap expCollectedMap;
    private BaseStatisticMap itemsRepairedMap;
    private BaseStatisticMap splashPotionsBrewedMap;
    private BaseStatisticMap splashPotionsUsedMap;
    private WrappedBalanceMap wrappedBalanceMap;
    private ToggleFoundDiamondsMap toggleFoundDiamondsMap;
    private ToggleDeathMessageMap toggleDeathMessageMap;
    private TabListModeMap tabListModeMap;
    private IPMap ipMap;
    private WhitelistedIPMap whitelistedIPMap;
    private CobblePickupMap cobblePickupMap;
    private KDRMap kdrMap;
    private CreditsMap creditsMap;
    private LFFMap lffMap;
    private FactionDelayMap factionDelayMap;

    private CombatLoggerListener combatLoggerListener;

    private KillTheKing killTheKing;
    private KillTheKingGUI killTheKingGUI;

    private PollHandler pollHandler;

    private AbilityManager abilityManager;

    private GiveawayHandler giveawayHandler;
//    private DisabledKitsHandler disabledKitsHandler;
    private RedeemHandler redeemHandler;

    private CouponHandler couponHandler;

    @Getter
    private NoFactionFocusFaction noFactionFocusFaction;

    public HashMap<UUID, Long> crystalrod = new HashMap<>();
    public HashMap<UUID, Long> vampire = new HashMap<>();

    public ArrayList<Player> audiice = new ArrayList<>();

    @Getter
    @Setter
    public LCWaypoint eventWaypoint;

    private Config partnerConfig, redeemConfig, voucherConfig, crackerConfig;

    @Getter
    @Setter
    private Predicate<Player> inEventPredicate = (player) ->
            mapHandler.isKitMap() &&
                    mapHandler.getGameHandler().isOngoingGame() && mapHandler.getGameHandler().getOngoingGame().isPlaying(player.getUniqueId());

    @Override
    public void onEnable() {

        if (Bukkit.getServerName().contains(" ")) {
            System.out.println("Invalid server-name in server.properties");
            this.getServer().shutdown();
            return;
        }

        //SpigotConfig.onlyCustomTab = true; // because I know we'll forget
        instance = this;
        saveDefaultConfig();
        loadConfigs();

        try {
            mongoPool = new MongoClient(new ServerAddress(getConfig().getString("Mongo.Host", "127.0.0.1"), 27017));
            MONGO_DB_NAME = this.getServer().getServerName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        (new DTRHandler()).runTaskTimer(this, 20L, 1200L);
        (new RedisSaveTask()).runTaskTimerAsynchronously(this, 1200L, 1200L);
        (new PacketBorderThread()).start();

        setupHandlers();
        setupPersistence();
        Partner.loadPackages();
        Cracker.loadCrackers();
        setupListeners();
        setupRankRunnables();
//        scoreboardManager = new ScoreboardManager();
//        NmsUtils.init();
        new ClearItemTask().runTaskTimer(this, 20L, 6000L);

        Proton.getInstance().getTabHandler().setLayoutProvider(new FoxtrotTabLayoutProvider());

        ProtocolLibrary.getProtocolManager().addPacketListener(new SignGUIPacketAdaper());
        ProtocolLibrary.getProtocolManager().addPacketListener(new ClientCommandPacketAdaper());
        ProtocolLibrary.getProtocolManager().addPacketListener(new SoundEffectPacketAdapter());

        for (World world : Bukkit.getWorlds()) {
            world.setThundering(false);
            world.setStorm(false);
            world.setWeatherDuration(Integer.MAX_VALUE);
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("mobGriefing", "false");
        }

        if (!Bukkit.getOnlineMode()) {
            SkinFix skinFix = new SkinFix();
            getServer().getPluginManager().registerEvents(skinFix, this);
            ProtocolLibrary.getProtocolManager().addPacketListener(skinFix);
        }


        // we just define this here while we're testing, if we actually
        // accept this feature it'll be moved to somewhere better
        new ServerFakeFreezeTask().runTaskTimerAsynchronously(this, 20L, 20L);

        // TODO: PUT THIS SOMEWHERE ELSE

    }

    private void loadConfigs() {
        partnerConfig = new Config(this,"partner", getDataFolder().getAbsolutePath());
        redeemConfig = new Config(this, "redeem_creators", getDataFolder().getAbsolutePath());
//        voucherConfig = new Config(this, "vouchers", getDataFolder().getAbsolutePath());
        crackerConfig = new Config(this, "cracker", getDataFolder().getAbsolutePath());

    }
    @Override
    public void onDisable() {
        getEventHandler().saveEvents();

        for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
            getPlaytimeMap().playerQuit(player.getUniqueId(), false);
            player.setGameMode(GameMode.SURVIVAL);
        }

        for (String playerName : PvPClassHandler.getEquippedKits().keySet()) {
            PvPClassHandler.getEquippedKits().get(playerName).remove(getServer().getPlayerExact(playerName));
        }

        for (Entity e : this.combatLoggerListener.getCombatLoggers()) {
            if (e != null) {
                e.remove();
            }
        }

//        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
//            if(ModSuite.hasStaffMode(online))
//                ModSuite.getStaffModeMap().get(online).staffModeDisable();
//        }

        RedisSaveTask.save(null, false);
        Foxtrot.getInstance().getServerHandler().save();

        if (Foxtrot.getInstance().getMapHandler().isKitMap()) {
            Foxtrot.getInstance().getMapHandler().getStatsHandler().save();
        }

        RegenUtils.resetAll();

        getCouponHandler().save();

        Proton.getInstance().getIRedisCommand().runRedisCommand((jedis) -> {
            jedis.save();
            return null;
        });
    }

    private void setupHandlers() {
        serverHandler = new ServerHandler();
        mapHandler = new MapHandler();
        mapHandler.load();

        teamHandler = new TeamHandler();
        noFactionFocusFaction = new NoFactionFocusFaction();
        LandBoard.getInstance().loadFromTeams();

        citadelHandler = new CitadelHandler();
        pvpClassHandler = new PvPClassHandler();
        eventHandler = new EventHandler();
        conquestHandler = new ConquestHandler();

        glowHandler = new GlowHandler();
        oreHandler = new OreHandler();


        killTheKingGUI = new KillTheKingGUI();
        pollHandler = new PollHandler();
        abilityManager = new AbilityManager(this);
        giveawayHandler = new GiveawayHandler();
        couponHandler = new CouponHandler();
//        disabledKitsHandler = new DisabledKitsHandler();
        lunarHandler = new LunarHandler(this);
        redeemHandler = new RedeemHandler();

        Proton.getInstance().getCommandHandler().registerParameterType(DurationParameter.class, new DurationParameter.Type());
        Proton.getInstance().getCommandHandler().registerAll(this);

        DeathMessageHandler.init();
        DTRHandler.loadDTR();
    }

    private void setupRankRunnables() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> onlineVampireUsers = getServer().getOnlinePlayers().stream().filter(o -> Nebula.getInstance().getProfileHandler().fromUuid(o.getUniqueId()).getActiveRank().getName().equals("Orbit")).map(Player::getName).collect(Collectors.toList());

                if (!onlineVampireUsers.isEmpty()) {
                    getServer().broadcastMessage(ChatColor.GRAY + "");
                    getServer().broadcastMessage(CC.translate("&6Online Orbit Users &8» &f" + StringUtils.join(onlineVampireUsers, ", ")));
                    getServer().broadcastMessage(CC.translate("&7You can purchase the Vampire rank at &7&odonate.orbit.rip"));
                    getServer().broadcastMessage(ChatColor.GRAY + "");
                }
            }
        }.runTaskTimerAsynchronously(this, 600, 20 * 180);

    }

    private void setupListeners() {

        getServer().getPluginManager().registerEvents(new AntiTrapDoorListener(), this);
        getServer().getPluginManager().registerEvents(new CreditShopListener(), this);
        getServer().getPluginManager().registerEvents(new MapListener(), this);
        getServer().getPluginManager().registerEvents(new AudimaticFrostBiteListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PartnerListener(), this);
        getServer().getPluginManager().registerEvents(new CrackerListener(), this);
        getServer().getPluginManager().registerEvents(new ClientListener(), this);
//        getServer().getPluginManager().registerEvents(new ModSuiteListener(), this);

//        getServer().getPluginManager().registerEvents(new FilterListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new AntiGlitchListener(), this);
        getServer().getPluginManager().registerEvents(new BasicPreventionListener(), this);
        getServer().getPluginManager().registerEvents(new BorderListener(), this);
        getServer().getPluginManager().registerEvents((combatLoggerListener = new CombatLoggerListener()), this);
        getServer().getPluginManager().registerEvents(new CrowbarListener(), this);
        getServer().getPluginManager().registerEvents(new DeathbanListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantmentLimiterListener(), this);
        getServer().getPluginManager().registerEvents(new EnderpearlCooldownHandler(), this);
        getServer().getPluginManager().registerEvents(new EndListener(), this);
        getServer().getPluginManager().registerEvents(new FoundDiamondsListener(), this);
        getServer().getPluginManager().registerEvents(new FoxListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getServer().getPluginManager().registerEvents(new GoldenAppleListener(), this);
//        getServer().getPluginManager().registerEvents(new VanishListener(), this);
        getServer().getPluginManager().registerEvents(new PvPTimerListener(), this);
        getServer().getPluginManager().registerEvents(new PotionLimiterListener(), this);
        getServer().getPluginManager().registerEvents(new NetherPortalListener(), this);
        getServer().getPluginManager().registerEvents(new PortalTrapListener(), this);
        getServer().getPluginManager().registerEvents(new SignSubclaimListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnerTrackerListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnTagListener(), this);
        getServer().getPluginManager().registerEvents(new StaffUtilsListener(), this);
        getServer().getPluginManager().registerEvents(new TeamListener(), this);
        getServer().getPluginManager().registerEvents(new WebsiteListener(), this);
//        getServer().getPluginManager().registerEvents(new StaffModeListener(), this);
        getServer().getPluginManager().registerEvents(new TeamSubclaimCommand(), this);
        getServer().getPluginManager().registerEvents(new EntityTargetingListener(), this);
        getServer().getPluginManager().registerEvents(new CrystalRodListener(), this);
        getServer().getPluginManager().registerEvents(new TeamClaimCommand(), this);
        getServer().getPluginManager().registerEvents(new StatTrakListener(), this);
//        getServer().getPluginManager().registerEvents(new ClientListener(), this);
        getServer().getPluginManager().registerEvents(new PearlGlitchListener(), this);
        getServer().getPluginManager().registerEvents(new DurabilityFix(), this);
        getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
        getServer().getPluginManager().registerEvents(new KillTheKingListener(), this);
        getServer().getPluginManager().registerEvents(new RedstoneFixAlerts(), this);
        getServer().getPluginManager().registerEvents(new StrengthFixListener(), this);
        getServer().getPluginManager().registerEvents(new MobChangesListener(), this);
        getServer().getPluginManager().registerEvents(new EntityStackListener(), this);

        if (Foxtrot.getInstance().getMapHandler().getScoreboardTitle().contains("Staging")) {
            getServer().getPluginManager().registerEvents(new PearlDebugListener(), this);
        }

        if (getServerHandler().isReduceArmorDamage()) {
            getServer().getPluginManager().registerEvents(new ArmorDamageListener(), this);
        }

        if (getServerHandler().isBlockEntitiesThroughPortals()) {
            getServer().getPluginManager().registerEvents(new EntityPortalListener(), this);
        }

        if (getServerHandler().isBlockRemovalEnabled()) {
            getServer().getPluginManager().registerEvents(new BlockRegenListener(), this);
        }

        // Register kitmap specific listeners
        if (getMapHandler().isKitMap()) {
            getServer().getPluginManager().registerEvents(new KitMapListener(), this);
            getServer().getPluginManager().registerEvents(new CarePackageHandler(), this);
            getServer().getPluginManager().registerEvents(new GameListeners(), this);
            getServer().getPluginManager().registerEvents(new SumoListeners(), this);
            getServer().getPluginManager().registerEvents(new FFAListeners(), this);
            getServer().getPluginManager().registerEvents(new SpleefListeners(), this);
            getServer().getPluginManager().registerEvents(new ShuffleListeners(), this);
            getServer().getPluginManager().registerEvents(new SelectionListeners(), this);
            getServer().getPluginManager().registerEvents(new KitEditorListener(), this);
        }

        getServer().getPluginManager().registerEvents(new BlockConvenienceListener(), this);
        getServer().getPluginManager().registerEvents(new GhostBlockListener(), this);
        getServer().getPluginManager().registerEvents(new TeamMissionListener(), this);
//        getServer().getPluginManager().registerEvents(new StaffModeListener(), this);
        getServer().getPluginManager().registerEvents(new CreatureListener(), this);
        //   getServer().getPluginManager().registerEvents(new TokenListener(), this);
        getServer().getPluginManager().registerEvents(new FactionWarListener(), this);
//        getServer().getPluginManager().registerEvents(new EnchantsFixListener(), this);
//        getServer().getPluginManager().registerEvents(new BountyListener(), this);
    }

    public static Foxtrot getInstance() {
        return instance;
    }

    private void setupPersistence() {
        (playtimeMap = new PlaytimeMap()).loadFromRedis();
        (oppleMap = new OppleMap()).loadFromRedis();
        (deathbanMap = new DeathbanMap()).loadFromRedis();
        (PvPTimerMap = new PvPTimerMap()).loadFromRedis();
        (startingPvPTimerMap = new StartingPvPTimerMap()).loadFromRedis();
        (deathsMap = new DeathsMap()).loadFromRedis();
        (killsMap = new KillsMap()).loadFromRedis();
        (chatModeMap = new ChatModeMap()).loadFromRedis();
        (toggleGlobalChatMap = new ToggleGlobalChatMap()).loadFromRedis();
        (fishingKitMap = new FishingKitMap()).loadFromRedis();
        (soulboundLivesMap = new SoulboundLivesMap()).loadFromRedis();
        (friendLivesMap = new FriendLivesMap()).loadFromRedis();
        (chatSpyMap = new ChatSpyMap()).loadFromRedis();
        (diamondMinedMap = new DiamondMinedMap()).loadFromRedis();
        (goldMinedMap = new GoldMinedMap()).loadFromRedis();
        (ironMinedMap = new IronMinedMap()).loadFromRedis();
        (coalMinedMap = new CoalMinedMap()).loadFromRedis();
        (redstoneMinedMap = new RedstoneMinedMap()).loadFromRedis();
        (lapisMinedMap = new LapisMinedMap()).loadFromRedis();
        (emeraldMinedMap = new EmeraldMinedMap()).loadFromRedis();
        (firstJoinMap = new FirstJoinMap()).loadFromRedis();
        (lastJoinMap = new LastJoinMap()).loadFromRedis();
        (enderPearlsUsedMap = new EnderPearlsUsedMap()).loadFromRedis();
        (expCollectedMap = new ExpCollectedMap()).loadFromRedis();
        (itemsRepairedMap = new ItemsRepairedMap()).loadFromRedis();
        (splashPotionsBrewedMap = new SplashPotionsBrewedMap()).loadFromRedis();
        (splashPotionsUsedMap = new SplashPotionsUsedMap()).loadFromRedis();
        (wrappedBalanceMap = new WrappedBalanceMap()).loadFromRedis();
        (toggleFoundDiamondsMap = new ToggleFoundDiamondsMap()).loadFromRedis();
        (toggleDeathMessageMap = new ToggleDeathMessageMap()).loadFromRedis();
        (tabListModeMap = new TabListModeMap()).loadFromRedis();
        (ipMap = new IPMap()).loadFromRedis();
        (whitelistedIPMap = new WhitelistedIPMap()).loadFromRedis();
        (cobblePickupMap = new CobblePickupMap()).loadFromRedis();
        (kdrMap = new KDRMap()).loadFromRedis();
        (reclaimMap = new ReclaimMap()).loadFromRedis();
        (creditsMap = new CreditsMap()).loadFromRedis();
        (lffMap = new LFFMap()).loadFromRedis();
        (factionDelayMap = new FactionDelayMap()).loadFromRedis();
    }

}
