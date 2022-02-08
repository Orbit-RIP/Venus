package net.frozenorb.foxtrot.team;

import cc.fyre.proton.Proton;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

import com.lunarclient.bukkitapi.object.LCWaypoint;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import lombok.Getter;
import lombok.Setter;
import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.region.oremtn.OreHandler;
import net.frozenorb.foxtrot.events.region.glowmtn.GlowHandler;
import net.frozenorb.foxtrot.persist.maps.DeathbanMap;
import net.frozenorb.foxtrot.persist.maps.KillsMap;
import net.frozenorb.foxtrot.team.claims.Claim;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.claims.Subclaim;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.team.dtr.DTRHandler;
import net.frozenorb.foxtrot.team.event.TeamPointsChangeEvent;
import net.frozenorb.foxtrot.team.mission.MissionType;
import net.frozenorb.foxtrot.team.track.TeamActionTracker;
import net.frozenorb.foxtrot.team.track.TeamActionType;
import net.frozenorb.foxtrot.util.ChatMode;
import net.frozenorb.foxtrot.util.CuboidRegion;
import cc.fyre.proton.serialization.LocationSerializer;
import cc.fyre.proton.util.TimeUtils;
import cc.fyre.proton.util.UUIDUtils;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Team {

    // Constants //
    public static final DecimalFormat DTR_FORMAT = new DecimalFormat("0.00");
    public static final String GRAY_LINE = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 53);
    public static final ChatColor ALLY_COLOR = ChatColor.BLUE;
    public static final int MAX_CLAIMS = 2;
    public static final int MAX_FORCE_INVITES = 5;

    // Internal //
    @Getter
    private boolean needsSave = false;
    @Getter
    private boolean loading = false;

    // Persisted //
    @Getter
    @Setter
    private ObjectId uniqueId;
    @Getter
    private String name;
    @Getter
    private Location HQ;
    @Getter
    private double balance;
    @Getter
    private double DTR;
    @Getter
    private long DTRCooldown;
    @Getter
    private List<Claim> claims = new ArrayList<>();
    @Getter
    private List<Subclaim> subclaims = new ArrayList<>();
    @Getter
    private UUID owner = null;
    @Getter
    private Set<UUID> members = new HashSet<>();
    @Getter
    private Set<UUID> captains = new HashSet<>();
    @Getter
    private Set<UUID> coleaders = new HashSet<>();
    @Getter
    private Set<UUID> invitations = new HashSet<>();
    @Getter
    private Set<ObjectId> allies = new HashSet<>();
    @Getter
    private Set<ObjectId> requestedAllies = new HashSet<>();
    @Getter
    private String announcement;
    @Getter
    private int maxOnline = -1;
    @Getter
    private boolean powerFaction = false;
    @Getter
    @Setter
    private boolean friendlyFire;
    @Getter
    @Setter
    private boolean claimLocked;
    @Getter
    private int lives = 0;
    @Getter
    private int points = 0;
    @Getter
    private int kills = 0;
    @Getter
    private int kothCaptures = 0;
    @Getter
    private int conquestsCapped = 0;
    @Getter
    private int diamondsMined = 0;
    @Getter
    private int deaths = 0;
    @Getter
    private int citadelsCapped = 0;
    @Getter
    private int killstreakPoints = 0;
    @Getter
    private int playtimePoints = 0;
    @Getter
    private int addedPoints = 0;
    @Getter
    private int removedPoints = 0;

    @Getter
    private int spawnersInClaim = 0;
    @Getter
    private int spentPoints = 0; // points spent on faction upgrades (kinda aids)

    @Getter
    private int tokens = 0;
    @Getter
    private AtomicInteger enderManKills = new AtomicInteger(0);

    @Getter
    private Map<String, Integer> upgradeToTier = new HashMap<>();
    @Getter
    private List<MissionType> completedMissions = new ArrayList<>();

    @Getter
    private int forceInvites = MAX_FORCE_INVITES;
    @Getter
    private Set<UUID> historicalMembers = new HashSet<>(); // this will store all players that were once members

    // Not persisted //
    @Getter
    @Setter
    private UUID focused;
    @Getter
    @Setter
    private Team focusedTeam;
    @Getter
    @Setter
    private Location rally;
    @Getter
    @Setter
    private Player rallyPlayer;
    @Getter
    @Setter
    private long lastRequestReport;

    @Getter
    @Setter
    private int bards;
    @Getter
    @Setter
    private int archers;
    @Getter
    @Setter
    private int rogues;

    // 325k - 8/15/21
    @Getter
    @Setter
    private LCWaypoint RallyPoint;
    @Getter
    @Setter
    private LCWaypoint focusedWaypoint;
    @Getter
    @Setter
    private LCWaypoint HQWaypoint;

    public Team(String name) {
        this.name = name;
    }

    public void setDTR(double newDTR) {
        setDTR(newDTR, null);
    }

    public void setDTR(double newDTR, Player actor) {
        if (DTR == newDTR) {
            return;
        }

        if (DTR <= 0 && newDTR > 0) {
            TeamActionTracker.logActionAsync(this, TeamActionType.TEAM_NO_LONGER_RAIDABLE, ImmutableMap.of());
        }

        if (0 < DTR && newDTR <= 0) {
            TeamActionTracker.logActionAsync(this, TeamActionType.TEAM_NOW_RAIDABLE, actor == null ? ImmutableMap.of() : ImmutableMap.of("actor", actor.getName()));
        }

        if (!isLoading()) {

            if (actor != null) {
                Foxtrot.getInstance().getLogger().info("[DTR Change] " + getName() + ": " + DTR + " --> " + newDTR + ". Actor: " + actor.getName());
            } else {
                Foxtrot.getInstance().getLogger().info("[DTR Change] " + getName() + ": " + DTR + " --> " + newDTR);
            }

        }

        this.DTR = newDTR;
        flagForSave();
    }

    public void setName(String name) {
        this.name = name;
        flagForSave();
    }

    public String getName(Player player) {
        if (name.equals(OreHandler.getOreTeamName()) && this.getMembers().size() == 0) {
            return ChatColor.BLUE + "Ore Mountain";
        }
        if (name.equals(GlowHandler.getGlowTeamName()) && this.getMembers().size() == 0) {
            return ChatColor.GOLD + "Glowstone Mountain"; // override team name
        } else if (owner == null) {
            if (hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
                switch (player.getWorld().getEnvironment()) {
                    case NETHER:
                        return (ChatColor.YELLOW + "Nether Spawn");
                    case THE_END:
                        return (ChatColor.YELLOW + "The End Safezone");
                }

                return (ChatColor.GREEN + "Spawn");
            } else if (hasDTRBitmask(DTRBitmask.KOTH)) {
                return (ChatColor.BLUE + getName());
            } else if (hasDTRBitmask(DTRBitmask.CITADEL)) {
                return (ChatColor.DARK_PURPLE + "Citadel");
            } else if (hasDTRBitmask(DTRBitmask.ROAD)) {
                switch (player.getWorld().getEnvironment()) {
                    case NORMAL:
                        return (ChatColor.YELLOW + getName().replace("Road", " Road"));
                    case NETHER:
                        return (ChatColor.YELLOW + getName().replace("Road", " Road"));
                }
                return (ChatColor.GOLD + getName().replace("Road", " Road"));
            } else if (hasDTRBitmask(DTRBitmask.CONQUEST)) {
                return (ChatColor.BLUE + "Conquest");
            } else if (name.equalsIgnoreCase("warzone")) {
                return (ChatColor.DARK_RED + "Warzone");
            } else if (name.equalsIgnoreCase("Hell")) {
                return (ChatColor.RED + "Hell");
            }
        }

        if (isMember(player.getUniqueId())) {
            return (ChatColor.GREEN + getName());
        } else if (isAlly(player.getUniqueId())) {
            return (Team.ALLY_COLOR + getName());
        } else {
            return (ChatColor.RED + getName());
        }
    }

    public void addMember(UUID member) {
        if (members.add(member)) {
            historicalMembers.add(member);

            if (this.loading) return;
            TeamActionTracker.logActionAsync(this, TeamActionType.PLAYER_JOINED, ImmutableMap.of(
                    "playerId", member
            ));

            flagForSave();
        }
    }

    public void addCaptain(UUID captain) {
        if (captains.add(captain) && !this.isLoading()) {
            TeamActionTracker.logActionAsync(this, TeamActionType.PROMOTED_TO_CAPTAIN, ImmutableMap.of(
                    "playerId", captain
            ));

            flagForSave();
        }
    }

    public void addCoLeader(UUID co) {
        if (coleaders.add(co) && !this.isLoading()) {
            TeamActionTracker.logActionAsync(this, TeamActionType.PROMOTED_TO_CO_LEADER, ImmutableMap.of(
                    "playerId", co
            ));

            flagForSave();
        }
    }

    public void setBalance(double balance) {
        this.balance = balance;
        flagForSave();
    }

    public void setDTRCooldown(long dtrCooldown) {
        this.DTRCooldown = dtrCooldown;
        flagForSave();
    }

    public void removeCaptain(UUID captain) {
        if (captains.remove(captain)) {
            TeamActionTracker.logActionAsync(this, TeamActionType.DEMOTED_FROM_CAPTAIN, ImmutableMap.of(
                    "playerId", captain
            ));

            flagForSave();
        }
    }

    public void removeCoLeader(UUID co) {
        if (coleaders.remove(co)) {
            TeamActionTracker.logActionAsync(this, TeamActionType.DEMOTED_FROM_CO_LEADER, ImmutableMap.of(
                    "playerId", co
            ));

            flagForSave();
        }
    }

    public void setOwner(UUID owner) {
        this.owner = owner;

        if (owner != null) {
            members.add(owner);
            coleaders.remove(owner);
            captains.remove(owner);
        }

        if (this.loading) return;
        TeamActionTracker.logActionAsync(this, TeamActionType.LEADER_CHANGED, ImmutableMap.of(
                "playerId", owner
        ));

        flagForSave();
    }

    public void setMaxOnline(int maxOnline) {
        this.maxOnline = maxOnline;
        flagForSave();
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;

        if (this.loading) return;
        TeamActionTracker.logActionAsync(this, TeamActionType.ANNOUNCEMENT_CHANGED, ImmutableMap.of(
                "newAnnouncement", announcement
        ));

        flagForSave();
    }

    public void setHQ(Location hq) {

        final String oldHQ = this.HQ == null ? "None" : (getHQ().getBlockX() + ", " + getHQ().getBlockY() + ", " + getHQ().getBlockZ());
        final String newHQ = hq == null ? "None" : (hq.getBlockX() + ", " + hq.getBlockY() + ", " + hq.getBlockZ());

        this.HQ = hq;

        if (this.loading) return;

        TeamActionTracker.logActionAsync(this, TeamActionType.HEADQUARTERS_CHANGED, ImmutableMap.of(
                "oldHq", oldHQ,
                "newHq", newHQ
        ));

        flagForSave();
    }

    public void setPowerFaction(boolean bool) {
        this.powerFaction = bool;
        if (bool) {
            TeamHandler.addPowerFaction(this);
        } else {
            TeamHandler.removePowerFaction(this);
        }

        if (this.loading) return;
        TeamActionTracker.logActionAsync(this, TeamActionType.POWER_FAC_STATUS_CHANGED, ImmutableMap.of(
                "powerFaction", bool
        ));

        flagForSave();
    }

    public void setLives(int lives) {
        this.lives = lives;
        flagForSave();
    }

    public boolean addLives(int lives) {
        if (lives < 0) {
            return false;
        }
        this.lives += lives;
        flagForSave();
        return true;
    }

    public boolean removeLives(int lives) {
        if (this.lives < lives || lives < 0) {
            return false; //You twat.
        }
        this.lives -= lives;
        flagForSave();
        return true;
    }

    public void disband() {
        try {
            if (owner != null) {
                double refund = balance;

                for (Claim claim : claims) {
                    refund += Claim.getPrice(claim, this, false);
                }

                Proton.getInstance().getEconomyHandler().deposit(owner, refund);
                Foxtrot.getInstance().getWrappedBalanceMap().setBalance(owner, Proton.getInstance().getEconomyHandler().getBalance(owner));
                Foxtrot.getInstance().getLogger().info("Economy Logger: Depositing " + refund + " into " + UUIDUtils.name(owner) + "'s account: Disbanded team");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (ObjectId allyId : getAllies()) {

            Team ally = Foxtrot.getInstance().getTeamHandler().getTeam(allyId);

            if (ally != null) {
                ally.getAllies().remove(getUniqueId());
            }

        }

        for (UUID uuid : members) {
            Foxtrot.getInstance().getChatModeMap().setChatMode(uuid, ChatMode.PUBLIC);
        }

        Foxtrot.getInstance().getTeamHandler().removeTeam(this);
        LandBoard.getInstance().clear(this);

        new BukkitRunnable() {

            public void run() {
                Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {
                    redis.del("fox_teams." + name.toLowerCase());
                    return (null);
                });

                DBCollection teamsCollection = Foxtrot.getInstance().getMongoPool().getDB(Foxtrot.MONGO_DB_NAME).getCollection("Teams");
                teamsCollection.remove(getJSONIdentifier());
            }

        }.runTaskAsynchronously(Foxtrot.getInstance());

        needsSave = false;
    }

    public void rename(String newName) {
        final String oldName = name;

        Foxtrot.getInstance().getTeamHandler().removeTeam(this);

        this.name = newName;

        Foxtrot.getInstance().getTeamHandler().setupTeam(this);

        Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {
            redis.del("fox_teams." + oldName.toLowerCase());
            return (null);
        });

        // We don't need to do anything here as all we're doing is changing the name, not the Unique ID (which is what Mongo uses)
        // therefore, Mongo will be notified of this once the 'flagForSave()' down below gets processed.

        for (Claim claim : getClaims()) {
            claim.setName(claim.getName().replaceAll(oldName, newName));
        }

        flagForSave();
    }

    public void setForceInvites(int forceInvites) {
        this.forceInvites = forceInvites;
        flagForSave();
    }

    public void setPoints(int points) {
//        if (!CustomTimerCreateCommand.isDoublePoints()) {
        this.points = points;
        new TeamPointsChangeEvent(this).call();
        flagForSave();
    }

    public void setKills(int kills) {
        this.kills = kills;
        recalculatePoints();
        flagForSave();
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
        recalculatePoints();
        flagForSave();
    }

    public void setKothCaptures(int kothCaptures) {
        this.kothCaptures = kothCaptures;
        recalculatePoints();
        flagForSave();
    }

    public void setConquestsCapped(int conquestsCapped) {
        this.conquestsCapped = conquestsCapped;
        recalculatePoints();
        flagForSave();
    }

    public void setAddedPoints(int addedPoints) {
        this.addedPoints = addedPoints;
        recalculatePoints();
        flagForSave();
    }

    public void setRemovedPoints(int addedPoints) {
        this.removedPoints = removedPoints;
        recalculatePoints();
        flagForSave();
    }

    public void setDiamondsMined(int diamondsMined) {
        this.diamondsMined = diamondsMined;
        recalculatePoints();
        flagForSave();
    }

    public void setCitadelsCapped(int citadels) {
        this.citadelsCapped = citadels;
        recalculatePoints();
        flagForSave();
    }

    public void setKillstreakPoints(int killstreakPoints) {
        this.killstreakPoints = killstreakPoints;
        recalculatePoints();
        flagForSave();
    }

    public void addKillstreakPoints(int killstreakPoints) {
        this.killstreakPoints += killstreakPoints;
        recalculatePoints();
        flagForSave();
    }

    public void setPlaytimePoints(int playtimePoints) {
        this.playtimePoints = playtimePoints;
        recalculatePoints();
        flagForSave();
    }

    public void addPlaytimePoints(int playtimePoints) {
        this.playtimePoints += playtimePoints;
        recalculatePoints();
        flagForSave();
    }

    public void addSpawnersInClaim(int amount) {
        spawnersInClaim += amount;

        if (spawnersInClaim < 0) {
            spawnersInClaim = 0;
        }

        recalculatePoints();
        flagForSave();
    }

    public void removeSpawnersInClaim(int amount) {
        spawnersInClaim -= amount;

        if (spawnersInClaim < 0) {
            spawnersInClaim = 0;
        }

        recalculatePoints();
        flagForSave();
    }

    public void setSpawnersInClaim(int amount) {
        if (amount < 0) {
            amount = 0;
        }

        spawnersInClaim = amount;
        recalculatePoints();
        flagForSave();
    }

    public void recalculateSpawnersInClaims() {
        new BukkitRunnable() {
            @Override
            public void run() {
                setSpawnersInClaim(findSpawners().size());
            }
        }.runTaskAsynchronously(Foxtrot.getInstance());
    }

    public List<CreatureSpawner> findSpawners() {
        List<CreatureSpawner> list = new ArrayList<>();

        // Iterate through chunks' tile entities rather than every block
        for (Claim claim : getClaims()) {
            final World world = Bukkit.getWorld(claim.getWorld());
            final Location minPoint = claim.getMinimumPoint();
            final Location maxPoint = claim.getMaximumPoint();
            final int minChunkX = ((int) minPoint.getX()) >> 4;
            final int minChunkZ = ((int) minPoint.getZ()) >> 4;
            final int maxChunkX = ((int) maxPoint.getX()) >> 4;
            final int maxChunkZ = ((int) maxPoint.getZ()) >> 4;

            for (int chunkX = minChunkX; chunkX < maxChunkX + 1; chunkX++) {
                for (int chunkZ = minChunkZ; chunkZ < maxChunkZ + 1; chunkZ++) {
                    Chunk chunk = world.getChunkAt(chunkX, chunkZ);

                    for (BlockState blockState : chunk.getTileEntities()) {
                        // Check if the block is a mob spawner
                        if (blockState instanceof CreatureSpawner) {
                            // Even though we're iterating through chunks' tile entities
                            // we need to make sure that the block's location is within
                            // the claim (because claims don't have to align with chunks)
                            final Location loc = blockState.getLocation();

                            if (loc.getX() >= minPoint.getX() && loc.getZ() >= minPoint.getZ() &&
                                    loc.getX() <= maxPoint.getX() && loc.getZ() <= maxPoint.getZ()) {
                                list.add((CreatureSpawner) blockState);
                            }
                        }
                    }
                }
            }
        }

        return list;
    }

    public void spendPoints(int points) {
        spentPoints += points;
        recalculatePoints();
        flagForSave();
    }

    public void setSpentPoints(int points) {
        spentPoints = points;
        recalculatePoints();
        flagForSave();
    }

    public void recalculatePoints() {
        int basePoints = 0;

        basePoints += kills;
        basePoints -= deaths;
        basePoints += kothCaptures * 10;
        basePoints += citadelsCapped * 125;
        basePoints += conquestsCapped * 75;
        basePoints += playtimePoints;
        basePoints += addedPoints;
        basePoints -= removedPoints;
        basePoints -= spentPoints;

        if (basePoints < 0) {
            basePoints = 0;
        }

        this.points = basePoints;
    }

    public String[] getPointBreakDown() {
        int basePoints = 0;

        basePoints += kills;
        basePoints -= deaths;
        basePoints += kothCaptures * 10;
        basePoints += citadelsCapped * 125;
        basePoints += conquestsCapped * 75;
        basePoints += playtimePoints;
        basePoints += addedPoints;
        basePoints -= removedPoints;
        basePoints -= spentPoints;

        if (basePoints < 0) {
            basePoints = 0;
        }

        return new String[]{
                "Base Points: " + basePoints,
                "Kills Points: (" + kills + " kills) * 1 = " + (kills),
                "Deaths Points: (" + deaths + " deaths) * 1 = " + (deaths),
                "KOTH Captures Points: (" + kothCaptures + " caps) * 10 = " + (kothCaptures * 10),
                "Citadel Captures Points: (" + citadelsCapped + " caps) * 125 = " + (citadelsCapped * 125),
                "Conquest Captures Points: (" + conquestsCapped + " caps) * 75 = " + (citadelsCapped * 75),
                "Playtime Points: " + playtimePoints,
                "Extra Added Points: " + addedPoints,
                "Extra Removed Points: " + removedPoints,
                "Spent Points: " + spentPoints
        };
    }

    public void setEnderManKills(int kills) {
        this.enderManKills.set(kills);
        this.flagForSave();
    }

    public void addEnderManKill() {
        this.enderManKills.incrementAndGet();
        this.flagForSave();
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
        this.flagForSave();
    }

    public void flagForSave() {
        needsSave = true;
    }

    public boolean isOwner(UUID check) {
        return (check.equals(owner));
    }

    public boolean isMember(UUID check) {
        return members.contains(check);
    }

    public boolean isCaptain(UUID check) {
        return captains.contains(check);
    }

    public boolean isCoLeader(UUID check) {
        return coleaders.contains(check);
    }

    public void validateAllies() {
        Iterator<ObjectId> allyIterator = getAllies().iterator();

        while (allyIterator.hasNext()) {
            ObjectId ally = allyIterator.next();
            Team checkTeam = Foxtrot.getInstance().getTeamHandler().getTeam(ally);

            if (checkTeam == null) {
                allyIterator.remove();
            }
        }
    }

    public boolean isAlly(UUID check) {
        Team checkTeam = Foxtrot.getInstance().getTeamHandler().getTeam(check);
        return (checkTeam != null && isAlly(checkTeam));
    }

    public boolean isAlly(Team team) {
        return (getAllies().contains(team.getUniqueId()));
    }

    public boolean ownsLocation(Location location) {
        return (LandBoard.getInstance().getTeam(location) == this);
    }

    public boolean ownsClaim(Claim claim) {
        return (claims.contains(claim));
    }

    public boolean removeMember(UUID member) {
        members.remove(member);
        captains.remove(member);
        coleaders.remove(member);

        // If the owner leaves (somehow)
        if (isOwner(member)) {
            Iterator<UUID> membersIterator = members.iterator();
            this.owner = membersIterator.hasNext() ? membersIterator.next() : null;
        }

        try {
            for (Subclaim subclaim : subclaims) {
                if (subclaim.isMember(member)) {
                    subclaim.removeMember(member);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (DTR > getMaxDTR()) {
            DTR = getMaxDTR();
        }

        if (this.loading) return false;
        TeamActionTracker.logActionAsync(this, TeamActionType.MEMBER_REMOVED, ImmutableMap.of(
                "playerId", member
        ));

        flagForSave();
        return (owner == null || members.size() == 0);
    }

    public boolean hasDTRBitmask(DTRBitmask bitmaskType) {
        if (getOwner() != null) {
            return (false);
        }

        int dtrInt = (int) DTR;
        return (((dtrInt & bitmaskType.getBitmask()) == bitmaskType.getBitmask()));
    }

    public int getOnlineMemberAmount() {
        int amt = 0;

        for (UUID member : getMembers()) {
            Player exactPlayer = Foxtrot.getInstance().getServer().getPlayer(member);

            if (exactPlayer != null && !exactPlayer.hasMetadata("invisible")) {
                amt++;
            }
        }

        return (amt);
    }

    public Collection<Player> getOnlineMembers() {
        List<Player> players = new ArrayList<>();

        for (UUID member : getMembers()) {
            Player exactPlayer = Foxtrot.getInstance().getServer().getPlayer(member);

            if (exactPlayer != null && !exactPlayer.hasMetadata("invisible")) {
                players.add(exactPlayer);
            }
        }

        return (players);
    }

    public Collection<UUID> getOfflineMembers() {
        List<UUID> players = new ArrayList<>();

        for (UUID member : getMembers()) {
            Player exactPlayer = Foxtrot.getInstance().getServer().getPlayer(member);

            if (exactPlayer == null || exactPlayer.hasMetadata("invisible")) {
                players.add(member);
            }
        }

        return (players);
    }

    public Subclaim getSubclaim(String name) {
        for (Subclaim subclaim : subclaims) {
            if (subclaim.getName().equalsIgnoreCase(name)) {
                return (subclaim);
            }
        }

        return (null);
    }

    public Subclaim getSubclaim(Location location) {
        for (Subclaim subclaim : subclaims) {
            if (new CuboidRegion(subclaim.getName(), subclaim.getLoc1(), subclaim.getLoc2()).contains(location)) {
                return (subclaim);
            }
        }

        return (null);
    }

    public int getSize() {
        return (getMembers().size());
    }

    public boolean isRaidable() {
        return (DTR <= 0);
    }

    public void playerDeath(String playerName, double dtrLoss) {
        double newDTR = Math.max(DTR - dtrLoss, -.99);

        TeamActionTracker.logActionAsync(this, TeamActionType.MEMBER_DEATH, ImmutableMap.of(
                "playerName", playerName,
                "dtrLoss", dtrLoss,
                "oldDtr", DTR,
                "newDtr", newDTR
        ));

        for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
//            if (player.isOp() && !DTRDebugCommand.disabled.contains(player) && !isMember(player.getUniqueId())) { // for debugging
//                player.sendMessage(ChatColor.RED + "User Death: " + ChatColor.WHITE + playerName);
//                player.sendMessage(ChatColor.RED + "Old DTR: " + ChatColor.WHITE + DTR_FORMAT.format(DTR));
//                player.sendMessage(ChatColor.RED + "New DTR: " + ChatColor.WHITE + DTR_FORMAT.format(newDTR));
//            }

            if (isMember(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Member Death: " + ChatColor.WHITE + playerName);
                player.sendMessage(ChatColor.RED + "Old DTR: " + ChatColor.WHITE + DTR_FORMAT.format(DTR));
                player.sendMessage(ChatColor.RED + "New DTR: " + ChatColor.WHITE + DTR_FORMAT.format(newDTR));
            }
        }

        Foxtrot.getInstance().getLogger().info("[TeamDeath] " + name + " > " + "Player death: [" + playerName + "] [" + DTR + "] [" + newDTR + "]");
        setDTR(newDTR);

        if (isRaidable()) {
            TeamActionTracker.logActionAsync(this, TeamActionType.TEAM_NOW_RAIDABLE, ImmutableMap.of());
            DTRCooldown = System.currentTimeMillis() + Foxtrot.getInstance().getMapHandler().getRegenTimeRaidable();
        } else {
            DTRCooldown = System.currentTimeMillis() + Foxtrot.getInstance().getMapHandler().getRegenTimeDeath();
        }

        DTRHandler.markOnDTRCooldown(this);
    }

    public double getDTRIncrement() {
        return (getDTRIncrement(getOnlineMemberAmount()));
    }

    public double getDTRIncrement(int playersOnline) {
        double dtrPerHour = DTRHandler.getBaseDTRIncrement(getSize()) * playersOnline;
        return (dtrPerHour / 60);
    }

    public double getMaxDTR() {
        return (DTRHandler.getMaxDTR(getSize()));
    }

    public void load(BasicDBObject obj) {
        loading = true;
        setUniqueId(obj.getObjectId("_id"));
        setOwner(obj.getString("Owner") == null ? null : UUID.fromString(obj.getString("Owner")));
        if (obj.containsKey("CoLeaders"))
            for (Object coLeader : (BasicDBList) obj.get("CoLeaders")) addCoLeader(UUID.fromString((String) coLeader));
        if (obj.containsKey("Captains"))
            for (Object captain : (BasicDBList) obj.get("Captains")) addCaptain(UUID.fromString((String) captain));
        if (obj.containsKey("Members"))
            for (Object member : (BasicDBList) obj.get("Members")) addMember(UUID.fromString((String) member));
        if (obj.containsKey("Invitations")) for (Object invite : (BasicDBList) obj.get("Invitations"))
            getInvitations().add(UUID.fromString((String) invite));
        if (obj.containsKey("DTR")) setDTR(obj.getDouble("DTR"));
        if (obj.containsKey("DTRCooldown")) setDTRCooldown(obj.getDate("DTRCooldown").getTime());
        if (obj.containsKey("Balance")) setBalance(obj.getDouble("Balance"));
        if (obj.containsKey("MaxOnline")) setMaxOnline(obj.getInt("MaxOnline"));
        if (obj.containsKey("HQ")) setHQ(LocationSerializer.deserialize((BasicDBObject) obj.get("HQ")));
        if (obj.containsKey("Announcement")) setAnnouncement(obj.getString("Announcement"));
        if (obj.containsKey("PowerFaction")) setPowerFaction(obj.getBoolean("PowerFaction"));
        if (obj.containsKey("Lives")) setLives(obj.getInt("Lives"));
        if (obj.containsKey("Claims"))
            for (Object claim : (BasicDBList) obj.get("Claims")) getClaims().add(Claim.fromJson((BasicDBObject) claim));
        if (obj.containsKey("Subclaims")) for (Object subclaim : (BasicDBList) obj.get("Subclaims"))
            getSubclaims().add(Subclaim.fromJson((BasicDBObject) subclaim));
        if (obj.containsKey("PlaytimePoints")) setPlaytimePoints(obj.getInt("PlaytimePoints"));
        if (obj.containsKey("SpentPoints")) setSpentPoints(obj.getInt("SpentPoints"));
        if (obj.containsKey("SpawnersInClaim")) setSpawnersInClaim(obj.getInt("SpawnersInClaim"));
        if (obj.containsKey("EnderManKills")) setEnderManKills(obj.getInt("EnderManKills"));
        if (obj.containsKey("Tokens")) setTokens(obj.getInt("tokens"));
        if (obj.containsKey("CompletedMission")) for (Object mission : (BasicDBList) obj.get("CompletedMissions"))
            this.completedMissions.add(MissionType.valueOf(((BasicDBObject) mission).getString("Mission")));

        // Load team upgrades if they exist
        if (obj.containsKey("Upgrades")) for (Object upgrade : (BasicDBList) obj.get("Upgrades"))
            upgradeToTier.put(((BasicDBObject) upgrade).getString("UpgradeName"), ((BasicDBObject) upgrade).getInt("Tier"));

        loading = false;
    }

    public void load(String str) {
        load(str, false);
    }

    public void load(String str, boolean forceSave) {
        loading = true;
        String[] lines = str.split("\n");

        for (String line : lines) {
            if (line.indexOf(':') == -1) {
                System.out.println("Found an invalid line... `" + line + "`");
                continue;
            }

            String identifier = line.substring(0, line.indexOf(':'));
            String[] lineParts = line.substring(line.indexOf(':') + 1).split(",");

            if (identifier.equalsIgnoreCase("Owner")) {
                if (!lineParts[0].equals("null")) {
                    setOwner(UUID.fromString(lineParts[0].trim()));
                }
            } else if (identifier.equalsIgnoreCase("UUID")) {
                uniqueId = new ObjectId(lineParts[0].trim());
            } else if (identifier.equalsIgnoreCase("Members")) {
                for (String name : lineParts) {
                    if (name.length() >= 2) {
                        addMember(UUID.fromString(name.trim()));
                    }
                }
            } else if (identifier.equalsIgnoreCase("CoLeaders")) {
                for (String name : lineParts) {
                    if (name.length() >= 2) {
                        addCoLeader(UUID.fromString(name.trim()));
                    }
                }
            } else if (identifier.equalsIgnoreCase("Captains")) {
                for (String name : lineParts) {
                    if (name.length() >= 2) {
                        addCaptain(UUID.fromString(name.trim()));
                    }
                }
            } else if (identifier.equalsIgnoreCase("Invited")) {
                for (String name : lineParts) {
                    if (name.length() >= 2) {
                        getInvitations().add(UUID.fromString(name.trim()));
                    }
                }
            } else if (identifier.equalsIgnoreCase("HistoricalMembers")) {
                for (String name : lineParts) {
                    if (name.length() >= 2) {
                        getHistoricalMembers().add(UUID.fromString(name.trim()));
                    }
                }
            } else if (identifier.equalsIgnoreCase("HQ")) {
                setHQ(parseLocation(lineParts));
            } else if (identifier.equalsIgnoreCase("DTR")) {
                setDTR(Double.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("Balance")) {
                setBalance(Double.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("MaxOnline")) {
                setMaxOnline(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("ForceInvites")) {
                setForceInvites(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("DTRCooldown")) {
                setDTRCooldown(Long.parseLong(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("FriendlyName")) {
                setName(lineParts[0]);
            } else if (identifier.equalsIgnoreCase("Claims")) {
                for (String claim : lineParts) {
                    claim = claim.replace("[", "").replace("]", "");

                    if (claim.contains(":")) {
                        String[] split = claim.split(":");

                        int x1 = Integer.parseInt(split[0].trim());
                        int y1 = Integer.parseInt(split[1].trim());
                        int z1 = Integer.parseInt(split[2].trim());
                        int x2 = Integer.parseInt(split[3].trim());
                        int y2 = Integer.parseInt(split[4].trim());
                        int z2 = Integer.parseInt(split[5].trim());
                        String name = split[6].trim();
                        String world = split[7].trim();

                        Claim claimObj = new Claim(world, x1, y1, z1, x2, y2, z2);
                        claimObj.setName(name);

                        getClaims().add(claimObj);
                    }
                }
            } else if (identifier.equalsIgnoreCase("Allies")) {
                // Just cancel loading of allies if they're disabled (for switching # of allowed allies mid-map)
                if (Foxtrot.getInstance().getMapHandler().getAllyLimit() == 0) {
                    continue;
                }

                for (String ally : lineParts) {
                    ally = ally.replace("[", "").replace("]", "");

                    if (ally.length() != 0) {
                        allies.add(new ObjectId(ally.trim()));
                    }
                }
            } else if (identifier.equalsIgnoreCase("RequestedAllies")) {
                // Just cancel loading of allies if they're disabled (for switching # of allowed allies mid-map)
                if (Foxtrot.getInstance().getMapHandler().getAllyLimit() == 0) {
                    continue;
                }

                for (String requestedAlly : lineParts) {
                    requestedAlly = requestedAlly.replace("[", "").replace("]", "");

                    if (requestedAlly.length() != 0) {
                        requestedAllies.add(new ObjectId(requestedAlly.trim()));
                    }
                }
            } else if (identifier.equalsIgnoreCase("Subclaims")) {
                for (String subclaim : lineParts) {
                    subclaim = subclaim.replace("[", "").replace("]", "");

                    if (subclaim.contains(":")) {
                        String[] split = subclaim.split(":");

                        int x1 = Integer.parseInt(split[0].trim());
                        int y1 = Integer.parseInt(split[1].trim());
                        int z1 = Integer.parseInt(split[2].trim());
                        int x2 = Integer.parseInt(split[3].trim());
                        int y2 = Integer.parseInt(split[4].trim());
                        int z2 = Integer.parseInt(split[5].trim());
                        String name = split[6].trim();
                        String membersRaw = "";

                        if (split.length >= 8) {
                            membersRaw = split[7].trim();
                        }

                        Location location1 = new Location(Foxtrot.getInstance().getServer().getWorld("world"), x1, y1, z1);
                        Location location2 = new Location(Foxtrot.getInstance().getServer().getWorld("world"), x2, y2, z2);
                        List<UUID> members = new ArrayList<>();

                        for (String uuidString : membersRaw.split(", ")) {
                            if (uuidString.isEmpty()) {
                                continue;
                            }

                            members.add(UUID.fromString(uuidString.trim()));
                        }

                        Subclaim subclaimObj = new Subclaim(location1, location2, name);
                        subclaimObj.setMembers(members);

                        getSubclaims().add(subclaimObj);
                    }
                }
            } else if (identifier.equalsIgnoreCase("Announcement")) {
                setAnnouncement(lineParts[0]);
            } else if (identifier.equalsIgnoreCase("PowerFaction")) {
                setPowerFaction(Boolean.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("Lives")) {
                setLives(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("Kills")) {
                setKills(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("Deaths")) {
                setDeaths(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("KothCaptures")) {
                setKothCaptures(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("ConquestCaptures")) {
                setConquestsCapped(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("AddedPoints")) {
                setAddedPoints(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("RemovedPoints")) {
                setRemovedPoints(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("DiamondsMined")) {
                setDiamondsMined(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("CitadelsCapped")) {
                setCitadelsCapped(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("KillstreakPoints")) {
                setKillstreakPoints(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("PlaytimePoints")) {
                setPlaytimePoints(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("Points")) {
                setPoints(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("SpentPoints")) {
                setSpentPoints(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("SpawnersInClaim")) {
                setSpawnersInClaim(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("EnderManKills")) {
                setEnderManKills(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("Upgrades")) {
                for (String name : lineParts) {
                    if (name.length() >= 2) {
                        String[] nameSplit = name.split(";");
                        upgradeToTier.put(nameSplit[0].trim(), Integer.valueOf(nameSplit[1].trim()));
                    }
                }
            } else if (identifier.equalsIgnoreCase("Tokens")) {
                setTokens(Integer.valueOf(lineParts[0]));
            } else if (identifier.equalsIgnoreCase("CompletedMissions")) {

                for (String name : lineParts) {

                    final String[] nameSplit = name.split(";");

                    this.completedMissions.add(MissionType.valueOf(nameSplit[0]));
                }

            }
        }

        for (UUID member : members) {
            Proton.getInstance().getUuidCache().ensure(member);
        }

        if (uniqueId == null) {
            uniqueId = new ObjectId();
            Foxtrot.getInstance().getLogger().info("Generating UUID for team " + getName() + "...");
        }

        loading = false;
        needsSave = forceSave;
    }

    public String saveString(boolean toJedis) {
        if (toJedis) {
            needsSave = false;
        }

        if (loading) {
            return (null);
        }

        StringBuilder teamString = new StringBuilder();

        StringBuilder members = new StringBuilder();
        StringBuilder captains = new StringBuilder();
        StringBuilder coleaders = new StringBuilder();
        StringBuilder invites = new StringBuilder();
        StringBuilder historicalMembers = new StringBuilder();

        for (UUID member : getMembers()) {
            members.append(member.toString()).append(", ");
        }

        for (UUID captain : getCaptains()) {
            captains.append(captain.toString()).append(", ");
        }

        for (UUID co : getColeaders()) {
            coleaders.append(co.toString()).append(", ");
        }

        for (UUID invite : getInvitations()) {
            invites.append(invite.toString()).append(", ");
        }

        for (UUID member : getHistoricalMembers()) {
            historicalMembers.append(member.toString()).append(", ");
        }

        if (members.length() > 2) {
            members.setLength(members.length() - 2);
        }

        if (captains.length() > 2) {
            captains.setLength(captains.length() - 2);
        }

        if (invites.length() > 2) {
            invites.setLength(invites.length() - 2);
        }

        if (historicalMembers.length() > 2) {
            historicalMembers.setLength(historicalMembers.length() - 2);
        }

        StringBuilder upgrades = new StringBuilder();

        for (Map.Entry<String, Integer> entry : upgradeToTier.entrySet()) {
            upgrades.append(entry.getKey()).append(";").append(entry.getValue()).append(", ");
        }

        if (upgrades.length() > 2) {
            upgrades.setLength(upgrades.length() - 2);
        }

        final StringBuilder missions = new StringBuilder();

        for (MissionType completedMission : this.completedMissions) {
            missions.append(completedMission.name()).append(";");
        }

        teamString.append("UUID:").append(getUniqueId().toString()).append("\n");
        teamString.append("Owner:").append(getOwner()).append('\n');
        teamString.append("CoLeaders:").append(coleaders.toString()).append('\n');
        teamString.append("Captains:").append(captains.toString()).append('\n');
        teamString.append("Members:").append(members.toString()).append('\n');
        teamString.append("Invited:").append(invites.toString().replace("\n", "")).append('\n');
        teamString.append("Subclaims:").append(getSubclaims().toString().replace("\n", "")).append('\n');
        teamString.append("Claims:").append(getClaims().toString().replace("\n", "")).append('\n');
        teamString.append("Allies:").append(getAllies().toString()).append('\n');
        teamString.append("RequestedAllies:").append(getRequestedAllies().toString()).append('\n');
        teamString.append("HistoricalMembers:").append(historicalMembers.toString()).append('\n');
        teamString.append("DTR:").append(getDTR()).append('\n');
        teamString.append("Balance:").append(getBalance()).append('\n');
        teamString.append("MaxOnline:").append(getMaxOnline()).append('\n');
        teamString.append("ForceInvites:").append(getForceInvites()).append('\n');
        teamString.append("DTRCooldown:").append(getDTRCooldown()).append('\n');
        teamString.append("FriendlyName:").append(getName().replace("\n", "")).append('\n');
        teamString.append("Announcement:").append(String.valueOf(getAnnouncement()).replace("\n", "")).append("\n");
        teamString.append("PowerFaction:").append(isPowerFaction()).append("\n");
        teamString.append("ClaimLocked:").append(isClaimLocked()).append("\n");
        teamString.append("Lives:").append(getLives()).append("\n");
        teamString.append("Kills:").append(getKills()).append("\n");
        teamString.append("Deaths:").append(getDeaths()).append("\n");
        teamString.append("DiamondsMined:").append(getDiamondsMined()).append("\n");
        teamString.append("KothCaptures:").append(getKothCaptures()).append("\n");
        teamString.append("ConquestCaptures:").append(getConquestsCapped()).append("\n");
        teamString.append("AddedPoints:").append(getAddedPoints()).append("\n");
        teamString.append("RemovedPoints:").append(getRemovedPoints()).append("\n");
        teamString.append("CitadelsCapped:").append(getCitadelsCapped()).append("\n");
        teamString.append("KillstreakPoints:").append(getKillstreakPoints()).append("\n");
        teamString.append("PlaytimePoints:").append(getPlaytimePoints()).append("\n");
        teamString.append("Points:").append(getPoints()).append("\n");
        teamString.append("SpentPoints:").append(getSpentPoints()).append("\n");
        teamString.append("SpawnersInClaim:").append(getSpawnersInClaim()).append("\n");
        teamString.append("EnderManKills:").append(getEnderManKills().get()).append("\n");
        teamString.append("Tokens:").append(getTokens()).append("\n");
        teamString.append("CompletedMissions").append(missions.toString()).append("\n");
        teamString.append("Upgrades:").append(upgrades.toString()).append("\n");

        if (getHQ() != null) {
            teamString.append("HQ:").append(getHQ().getWorld().getName()).append(",").append(getHQ().getX()).append(",").append(getHQ().getY()).append(",").append(getHQ().getZ()).append(",").append(getHQ().getYaw()).append(",").append(getHQ().getPitch()).append('\n');
        }

        return (teamString.toString());
    }

    public BasicDBObject toJSON() {
        BasicDBObject dbObject = new BasicDBObject();

        dbObject.put("Owner", getOwner() == null ? null : getOwner().toString());
        dbObject.put("CoLeaders", UUIDUtils.uuidsToStrings(getColeaders()));
        dbObject.put("Captains", UUIDUtils.uuidsToStrings(getCaptains()));
        dbObject.put("Members", UUIDUtils.uuidsToStrings(getMembers()));
        dbObject.put("Invitations", UUIDUtils.uuidsToStrings(getInvitations()));
        dbObject.put("Allies", getAllies());
        dbObject.put("RequestedAllies", getRequestedAllies());
        dbObject.put("DTR", getDTR());
        dbObject.put("DTRCooldown", new Date(getDTRCooldown()));
        dbObject.put("Balance", getBalance());
        dbObject.put("MaxOnline", getMaxOnline());
        dbObject.put("Name", getName());
        dbObject.put("HQ", LocationSerializer.serialize(getHQ()));
        dbObject.put("Announcement", getAnnouncement());
        dbObject.put("PowerFaction", isPowerFaction());
        dbObject.put("ClaimLocked", isClaimLocked());
        dbObject.put("Lives", getLives());

        BasicDBList claims = new BasicDBList();
        BasicDBList subclaims = new BasicDBList();

        for (Claim claim : getClaims()) {
            claims.add(claim.json());
        }

        for (Subclaim subclaim : getSubclaims()) {
            subclaims.add(subclaim.json());
        }

        dbObject.put("Claims", claims);
        dbObject.put("Subclaims", subclaims);
        dbObject.put("Kills", this.kills);
        dbObject.put("Deaths", this.deaths);
        dbObject.put("DiamondsMined", this.diamondsMined);
        dbObject.put("CitadelsCaptured", this.citadelsCapped);
        dbObject.put("KillstreakPoints", this.killstreakPoints);
        dbObject.put("PlaytimePoints", this.playtimePoints);
        dbObject.put("KothCaptures", this.kothCaptures);
        dbObject.put("ConquestCaptures", this.conquestsCapped);
        dbObject.put("AddedPoints", this.addedPoints);
        dbObject.put("RemovedPoints", this.removedPoints);
        dbObject.put("Points", this.points);
        dbObject.put("SpentPoints", this.spentPoints);
        dbObject.put("SpawnersInClaim", this.spawnersInClaim);
        dbObject.put("EnderManKills", this.enderManKills.get());
        dbObject.put("Tokens", this.tokens);

        final BasicDBList missions = new BasicDBList();

        for (MissionType mission : this.completedMissions) {

            final BasicDBObject missionDBObject = new BasicDBObject();

            missionDBObject.put("Mission", mission.name());

            missions.add(missionDBObject);
        }


        dbObject.put("CompletedMission", missions);

        final BasicDBList upgrades = new BasicDBList();

        for (Map.Entry<String, Integer> entry : upgradeToTier.entrySet()) {
            BasicDBObject upgradeDBObject = new BasicDBObject();
            upgradeDBObject.put("UpgradeName", entry.getKey());
            upgradeDBObject.put("Tier", entry.getValue());

            upgrades.add(upgradeDBObject);
        }

        dbObject.put("Upgrades", upgrades);

        return (dbObject);
    }

    public BasicDBObject getJSONIdentifier() {
        return (new BasicDBObject("_id", getUniqueId().toHexString()));
    }

    private Location parseLocation(String[] args) {
        if (args.length != 6) {
            return (null);
        }

        World world = Foxtrot.getInstance().getServer().getWorld(args[0]);
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);
        float yaw = Float.parseFloat(args[4]);
        float pitch = Float.parseFloat(args[5]);

        return (new Location(world, x, y, z, yaw, pitch));
    }

    public void sendMessage(String message) {
        for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
            if (isMember(player.getUniqueId())) {
                player.sendMessage(message);
            }
        }
    }

    public void sendTeamInfo(Player player) {
        // Don't make our null teams have DTR....
        // @HCFactions
        if (getOwner() == null) {
            player.sendMessage(GRAY_LINE);
            player.sendMessage(getName(player));

            if (HQ != null && HQ.getWorld().getEnvironment() != World.Environment.NORMAL) {
                String world = HQ.getWorld().getEnvironment() == World.Environment.NETHER ? "Nether" : "End"; // if it's not the nether, it's the end
                player.sendMessage(ChatColor.YELLOW + "Location: " + ChatColor.WHITE + (HQ == null ? "None" : HQ.getBlockX() + ", " + HQ.getBlockZ() + " (" + world + ")"));
            } else {
                player.sendMessage(ChatColor.YELLOW + "Location: " + ChatColor.WHITE + (HQ == null ? "None" : HQ.getBlockX() + ", " + HQ.getBlockZ()));
            }

            if (getName().equalsIgnoreCase("Citadel")) {
                Set<ObjectId> cappers = Foxtrot.getInstance().getCitadelHandler().getCappers();
                Set<String> capperNames = new HashSet<>();

                for (ObjectId capper : cappers) {
                    Team capperTeam = Foxtrot.getInstance().getTeamHandler().getTeam(capper);

                    if (capperTeam != null) {
                        capperNames.add(capperTeam.getName());
                    }
                }

                if (!cappers.isEmpty()) {
                    player.sendMessage(ChatColor.YELLOW + "Currently captured by: " + ChatColor.RED + Joiner.on(", ").join(capperNames));
                }
            }

            player.sendMessage(GRAY_LINE);
            return;
        }

        KillsMap killsMap = Foxtrot.getInstance().getKillsMap();
        DeathbanMap deathbanMap = Foxtrot.getInstance().getDeathbanMap();
        Player owner = Foxtrot.getInstance().getServer().getPlayer(getOwner());
        StringBuilder allies = new StringBuilder();

        FancyMessage coleadersJson = new FancyMessage("Co-Leaders: ").color(ChatColor.WHITE);

        FancyMessage captainsJson = new FancyMessage("Captains: ").color(ChatColor.WHITE);

        if (player.hasPermission("foxtrot.manage")) {
            captainsJson.command("/manageteam demote " + getName()).tooltip("§eClick to demote captains");
        }

        FancyMessage membersJson = new FancyMessage("Members: ").color(ChatColor.WHITE);

        if (player.hasPermission("foxtrot.manage")) {
            membersJson.command("/manageteam promote " + getName()).tooltip("§eClick to promote members");
        }

        int onlineMembers = 0;

        for (ObjectId allyId : getAllies()) {
            Team ally = Foxtrot.getInstance().getTeamHandler().getTeam(allyId);

            if (ally != null) {
                allies.append(ally.getName(player)).append(ChatColor.WHITE).append("[").append(ChatColor.GREEN).append(ally.getOnlineMemberAmount()).append("/").append(ally.getSize()).append(ChatColor.YELLOW).append("]").append(ChatColor.GRAY).append(", ");
            }
        }


        for (Player onlineMember : getOnlineMembers()) {
            onlineMembers++;

            // There can only be one owner, so we special case it.
            if (isOwner(onlineMember.getUniqueId())) {
                continue;
            }

            FancyMessage appendTo = membersJson;
            if (isCoLeader(onlineMember.getUniqueId())) {
                appendTo = coleadersJson;
            } else if (isCaptain(onlineMember.getUniqueId())) {
                appendTo = captainsJson;
            }

            if (!ChatColor.stripColor(appendTo.toOldMessageFormat()).endsWith("s: ")) {
                appendTo.then(", ").color(ChatColor.GRAY);
            }

            appendTo.then(onlineMember.getName()).color(ChatColor.GREEN).then("[").color(ChatColor.WHITE);
            appendTo.then(killsMap.getKills(onlineMember.getUniqueId()) + "").color(ChatColor.GREEN);
            appendTo.then("]").color(ChatColor.WHITE);
        }

        for (UUID offlineMember : getOfflineMembers()) {
            if (isOwner(offlineMember)) {
                continue;
            }

            FancyMessage appendTo = membersJson;
            if (isCoLeader(offlineMember)) {
                appendTo = coleadersJson;
            } else if (isCaptain(offlineMember)) {
                appendTo = captainsJson;
            }

            if (!ChatColor.stripColor(appendTo.toOldMessageFormat()).endsWith("s: ")) {
                appendTo.then(", ").color(ChatColor.GRAY);
            }

            appendTo.then(UUIDUtils.name(offlineMember)).color(deathbanMap.isDeathbanned(offlineMember) ? ChatColor.RED : ChatColor.GRAY);
            appendTo.then("[").color(ChatColor.WHITE).then("" + killsMap.getKills(offlineMember)).color(ChatColor.GREEN);
            appendTo.then("]").color(ChatColor.WHITE);

        }

        // Now we can actually send all that info we just processed.
        player.sendMessage(GRAY_LINE);

        FancyMessage teamLine = new FancyMessage();

        teamLine.text(ChatColor.YELLOW + getName()).link("http://www." + Foxtrot.getInstance().getServerHandler().getStatsWebsiteRoot() + "/hcf/" + getName()).tooltip(ChatColor.GREEN + "Click to view team on the " + Foxtrot.getInstance().getServerHandler().getServerName() + " website!");
        teamLine.then().text(ChatColor.GRAY + " [" + onlineMembers + "/" + getSize() + "]" + ChatColor.WHITE + " - ");
        teamLine.then().text(ChatColor.WHITE + "HQ: " + ChatColor.GRAY + (HQ == null ? "None" : "(" + HQ.getBlockX() + ", " + HQ.getBlockZ() + ")"));

        if (HQ != null && player.hasPermission("pivot.staff")) {
            teamLine.command("/tppos " + HQ.getBlockX() + " " + HQ.getBlockY() + " " + HQ.getBlockZ());
            teamLine.tooltip("§aClick to warp to HQ");
        }

        if (player.hasPermission("pivot.staff")) {
            teamLine.then().text("§f - §a[Manage]").color(ChatColor.YELLOW).command("/manageteam manage " + getName()).tooltip("§eClick to manage team");
        }

        teamLine.send(player);

        if (allies.length() > 2) {
            allies.setLength(allies.length() - 2);
            player.sendMessage(ChatColor.WHITE + "Allies: " + ChatColor.RED + allies.toString());
        }

        FancyMessage leader = new FancyMessage(ChatColor.WHITE + "Leader: " + (owner == null || owner.hasMetadata("invisible") ? (deathbanMap.isDeathbanned(getOwner()) ? ChatColor.RED : ChatColor.GRAY) : ChatColor.GREEN) + UUIDUtils.name(getOwner()) + ChatColor.WHITE + "[" + ChatColor.GREEN + killsMap.getKills(getOwner()) + ChatColor.WHITE + "]");


        if (player.hasPermission("pivot.staff")) {
            leader.command("/manageteam leader " + getName()).tooltip("§eClick to change leader");
        }

        leader.send(player);

        if (!ChatColor.stripColor(coleadersJson.toOldMessageFormat()).endsWith("s: ")) {
            coleadersJson.send(player);
        }

        if (!ChatColor.stripColor(captainsJson.toOldMessageFormat()).endsWith("s: ")) {
            captainsJson.send(player);
        }


        if (!ChatColor.stripColor(membersJson.toOldMessageFormat()).endsWith("s: ")) {
            membersJson.send(player);
        }


        FancyMessage balance = new FancyMessage(ChatColor.WHITE + "Balance: " + ChatColor.GRAY + "$" + Math.round(getBalance()));

        if (player.hasPermission("foxtrot.manage")) {
            balance.command("/manageteam balance " + getName()).tooltip("§eClick to modify team balance");
        }

        balance.send(player);


        FancyMessage dtrMessage = new FancyMessage(ChatColor.WHITE + "Deaths Until Raidable: " + getDTRColor() + DTR_FORMAT.format(getDTR()) + getDTRSuffix());


        if (player.hasPermission("foxtrot.manage")) {
            dtrMessage.command("/manageteam dtr " + getName()).tooltip("§eClick to modify team DTR");
        }

        dtrMessage.send(player);

        if (isMember(player.getUniqueId()) || player.hasPermission("foxtrot.manage")) {
            player.sendMessage(ChatColor.WHITE + "Points: " + ChatColor.GRAY + getPoints());
            player.sendMessage(ChatColor.WHITE + "KOTH Captures: " + ChatColor.GRAY + getKothCaptures());
            if (!Foxtrot.getInstance().getMapHandler().isKitMap()) {
                if (player.hasPermission("foxtrot.staff")) {
                    player.sendMessage(ChatColor.WHITE + "Spawners: " + ChatColor.GRAY + getSpawnersInClaim());
                }
            }
        }

        if (DTRHandler.isOnCooldown(this)) {
            if (!player.hasPermission("foxtrot.setdtrregen")) {
                player.sendMessage(ChatColor.WHITE + "DTR Regen: " + ChatColor.RED + TimeUtils.formatIntoDetailedString(((int) (getDTRCooldown() - System.currentTimeMillis())) / 1000).trim());
            } else {
                FancyMessage message = new FancyMessage(ChatColor.WHITE + "DTR Regen: ")
                        .tooltip(ChatColor.GREEN + "Click to remove regeneration timer").command("/startdtrregen " + getName());

                message.then(TimeUtils.formatIntoDetailedString(((int) (getDTRCooldown() - System.currentTimeMillis())) / 1000)).color(ChatColor.RED)
                        .tooltip(ChatColor.GREEN + "Click to remove regeneration timer").command("/startdtrregen " + getName());

                message.send(player);
            }
        }

        // Only show this if they're a member.
        if (isMember(player.getUniqueId()) && announcement != null && !announcement.equals("null")) {
            player.sendMessage(ChatColor.WHITE + "Announcement: " + ChatColor.LIGHT_PURPLE + announcement);
        }

        player.sendMessage(GRAY_LINE);
        // .... and that is how we do a /f who.
    }


    @Override
    public int hashCode() {
        return uniqueId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Team)) {
            return false;
        }

        Team other = (Team) obj;
        return other.uniqueId.equals(uniqueId);
    }

    public ChatColor getDTRColor() {
        ChatColor dtrColor = ChatColor.GREEN;

        if (DTR / getMaxDTR() <= 0.25) {
            if (isRaidable()) {
                dtrColor = ChatColor.DARK_RED;
            } else {
                dtrColor = ChatColor.YELLOW;
            }
        }

        return (dtrColor);
    }

    public String getDTRSuffix() {
        if (DTRHandler.isRegenerating(this)) {
            if (getOnlineMemberAmount() == 0) {
                return (ChatColor.GRAY + "◀");
            } else {
                return (ChatColor.GREEN + "▲");
            }
        } else if (DTRHandler.isOnCooldown(this)) {
            return (ChatColor.RED + "■");
        } else {
            return (ChatColor.GREEN + "◀");
        }
    }

    public List<MissionType> getCompletedMissions() {
        return Arrays.stream(MissionType.values()).filter(missionType -> missionType.hasCompleted(this)).collect(Collectors.toList());
    }
}
