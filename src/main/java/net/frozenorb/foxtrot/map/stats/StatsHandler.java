package net.frozenorb.foxtrot.map.stats;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import cc.fyre.proton.Proton;
import cc.fyre.proton.command.param.ParameterType;
import lombok.Setter;
import net.frozenorb.foxtrot.commands.SOTWCommand;
import net.frozenorb.foxtrot.map.stats.hologram.HologramCache;
import net.frozenorb.foxtrot.map.stats.hologram.HologramRunnable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.map.stats.command.StatsTopCommand;
import net.frozenorb.foxtrot.map.stats.command.StatsTopCommand.StatsObjective;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.serialization.LocationSerializer;
import cc.fyre.proton.util.UUIDUtils;
import net.minecraft.util.com.google.common.collect.Iterables;
import net.minecraft.util.com.google.common.reflect.TypeToken;

public class StatsHandler implements Listener {

    private Map<UUID, StatsEntry> stats = Maps.newConcurrentMap();

    @Getter private Map<Location, Integer> leaderboardSigns = Maps.newHashMap();
    @Getter private Map<Location, Integer> leaderboardHeads = Maps.newHashMap();
    
    @Getter private Map<Location, StatsObjective> objectives = Maps.newHashMap();

    @Getter @Setter private Location topFactionHologram;

    @Getter @Setter
    private Location killTopHologram;

    @Getter private HologramRunnable hologramRunnable = new HologramRunnable();

    @Getter private HologramCache hologramCache;

    @Getter
    private Map<Integer, UUID> topKills = Maps.newConcurrentMap();

    private boolean firstUpdateComplete = false;
    
    public StatsHandler() {
        Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {
            for (String key : redis.keys(Bukkit.getServerName() + ":" + "stats:*")) {
                UUID uuid = UUID.fromString(key.split(":")[2]);
                StatsEntry entry = Proton.PLAIN_GSON.fromJson(redis.get(key), StatsEntry.class);

                stats.put(uuid, entry);
            }

            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Kit Map] Loaded " + stats.size() + " stats.");

            if (redis.exists(Bukkit.getServerName() + ":" + "leaderboardSigns")) {
                List<String> serializedSigns = Proton.PLAIN_GSON.fromJson(redis.get(Bukkit.getServerName() + ":" + "leaderboardSigns"), new TypeToken<List<String>>() {}.getType());

                for (String sign : serializedSigns) {
                    Location location = LocationSerializer.deserialize((BasicDBObject) JSON.parse(sign.split("----")[0]));
                    int place = Integer.parseInt(sign.split("----")[1]);

                    leaderboardSigns.put(location, place);
                }

                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Kit Map] Loaded " + leaderboardSigns.size() + " leaderboard signs.");
            }

            if (redis.exists(Bukkit.getServerName() + ":" + "leaderboardHeads")) {
                List<String> serializedHeads = Proton.PLAIN_GSON.fromJson(redis.get(Bukkit.getServerName() + ":" + "leaderboardHeads"), new TypeToken<List<String>>() {}.getType());

                for (String sign : serializedHeads) {
                    Location location = LocationSerializer.deserialize((BasicDBObject) JSON.parse(sign.split("----")[0]));
                    int place = Integer.parseInt(sign.split("----")[1]);

                    leaderboardHeads.put(location, place);
                }

                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Kit Map] Loaded " + leaderboardHeads.size() + " leaderboard heads.");
            }
            
            if (redis.exists(Bukkit.getServerName() + ":" + "objectives")) {
                List<String> serializedObjectives = Proton.PLAIN_GSON.fromJson(redis.get(Bukkit.getServerName() + ":" + "objectives"), new TypeToken<List<String>>() {}.getType());

                for (String objective : serializedObjectives) {
                    Location location = LocationSerializer.deserialize((BasicDBObject) JSON.parse(objective.split("----")[0]));
                    StatsObjective obj = StatsObjective.valueOf(objective.split("----")[1]);

                    objectives.put(location, obj);
                }

                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Kit Map] Loaded " + objectives.size() + " objectives.");
            }

            return null;
        });

        Bukkit.getPluginManager().registerEvents(this, Foxtrot.getInstance());

        Proton.getInstance().getCommandHandler().registerPackage(Foxtrot.getInstance(), "net.frozenorb.foxtrot.map.stats.command");

        Proton.getInstance().getCommandHandler().registerParameterType(StatsTopCommand.StatsObjective.class, new ParameterType<StatsObjective>() {

            @Override
            public StatsTopCommand.StatsObjective transform(CommandSender sender, String source) {
                for (StatsTopCommand.StatsObjective objective : StatsTopCommand.StatsObjective.values()) {
                    if (source.equalsIgnoreCase(objective.getName())) {
                        return objective;
                    }

                    for (String alias : objective.getAliases()) {
                        if (source.equalsIgnoreCase(alias)) {
                            return objective;
                        }
                    }
                }

                sender.sendMessage(ChatColor.RED + "Objective '" + source + "' not found.");
                return null;
            }

            @Override
            public List<String> tabComplete(Player sender, Set<String> flags, String source) {
                List<String> completions = Lists.newArrayList();

                obj:
                for (StatsTopCommand.StatsObjective objective : StatsTopCommand.StatsObjective.values()) {
                    if (StringUtils.startsWithIgnoreCase(objective.getName().replace(" ", ""), source)) {
                        completions.add(objective.getName().replace(" ", ""));
                        continue;
                    }

                    for (String alias : objective.getAliases()) {
                        if (StringUtils.startsWithIgnoreCase(alias, source)) {
                            completions.add(alias);
                            continue obj;
                        }
                    }
                }

                return completions;
            }
        });

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Foxtrot.getInstance(), this::save, 30 * 20L, 30 * 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Foxtrot.getInstance(), this::updatePhysicalLeaderboards, 60 * 20L, 60 * 20L);
    }

    public void save() {
        Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {
            List<String> serializedSigns = leaderboardSigns.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue()).collect(Collectors.toList());
            List<String> serializedHeads = leaderboardHeads.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue()).collect(Collectors.toList());
            List<String> serializedObjectives = objectives.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue().name()).collect(Collectors.toList());

            redis.set(Bukkit.getServerName() + ":" + "leaderboardSigns", Proton.PLAIN_GSON.toJson(serializedSigns));
            redis.set(Bukkit.getServerName() + ":" + "leaderboardHeads", Proton.PLAIN_GSON.toJson(serializedHeads));
            redis.set(Bukkit.getServerName() + ":" + "objectives", Proton.PLAIN_GSON.toJson(serializedObjectives));

            for (StatsEntry entry : stats.values()) {
                redis.set(Bukkit.getServerName() + ":" + "stats:" + entry.getOwner().toString(), Proton.PLAIN_GSON.toJson(entry));
            }
            return null;
        });
    }

    public StatsEntry getStats(Player player) {
        return getStats(player.getUniqueId());
    }

    public StatsEntry getStats(String name) {
        return getStats(UUIDUtils.uuid(name));
    }

    public StatsEntry getStats(UUID uuid) {
        stats.putIfAbsent(uuid, new StatsEntry(uuid));
        return stats.get(uuid);
    }

    private void updateTopKillsMap() {
        UUID oldFirstPlace = this.topKills.get(1);
        UUID oldSecondPlace = this.topKills.get(2);
        UUID oldThirdPlace = this.topKills.get(3);

        UUID newFirstPlace = get(StatsObjective.KILLS, 1).getOwner();
        UUID newSecondPlace = get(StatsObjective.KILLS, 2).getOwner();
        UUID newThirdPlace = get(StatsObjective.KILLS, 3).getOwner();

        if (!SOTWCommand.isSOTWTimer()) {
            if (firstUpdateComplete) {
                if (newFirstPlace != oldFirstPlace) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6" + UUIDUtils.name(newFirstPlace) + "&f has surpassed &6" + UUIDUtils.name(oldFirstPlace) + "&f for &6#1&f in kills!"));
                }

                if (newSecondPlace != oldSecondPlace) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6" + UUIDUtils.name(newSecondPlace) + "&f has surpassed &6" + UUIDUtils.name(oldSecondPlace) + "&f for &6#2&f in kills!"));
                }

                if (newThirdPlace != oldThirdPlace) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6" + UUIDUtils.name(newThirdPlace) + "&f has surpassed &6" + UUIDUtils.name(oldThirdPlace) + "&f for &6#3&f in kills!"));
                }
            }
        }

        this.topKills.put(1, newFirstPlace);
        this.topKills.put(2, newSecondPlace);
        this.topKills.put(3, newThirdPlace);

        this.firstUpdateComplete = true;
    }

    public void updatePhysicalLeaderboards() {
        Iterator<Map.Entry<Location, Integer>> iterator = leaderboardSigns.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Location, Integer> entry = iterator.next();

            StatsEntry stats = get(objectives.get(entry.getKey()), entry.getValue());

            if (stats == null) {
                continue;
            }

            if (!(entry.getKey().getBlock().getState() instanceof Sign)) {
                iterator.remove();
                continue;
            }

            Sign sign = (Sign) entry.getKey().getBlock().getState();

            sign.setLine(0, trim(ChatColor.GOLD.toString() + ChatColor.BOLD + (beautify(entry.getKey()))));
            sign.setLine(1, trim(ChatColor.YELLOW.toString() + UUIDUtils.name(stats.getOwner())));
            sign.setLine(2, trim(ChatColor.YELLOW.toString() + "Place #" + entry.getValue()));

            sign.setLine(3, ChatColor.WHITE.toString() + stats.get(objectives.get(entry.getKey())));

            sign.update();
        }

        Iterator<Map.Entry<Location, Integer>> headIterator = leaderboardHeads.entrySet().iterator();

        while (headIterator.hasNext()) {
            Map.Entry<Location, Integer> entry = headIterator.next();

            StatsEntry stats = get(objectives.get(entry.getKey()), entry.getValue());

            if (stats == null) {
                continue;
            }

            if (!(entry.getKey().getBlock().getState() instanceof Skull)) {
                headIterator.remove();
                continue;
            }

            Skull skull = (Skull) entry.getKey().getBlock().getState();

            skull.setOwner(UUIDUtils.name(stats.getOwner()));
            skull.update();
        }
    }
    
    private String beautify(Location location) {
        StatsObjective objective = objectives.get(location);
        
        switch (objective) {
        case DEATHS:
            return "DEATHS";
        case HIGHEST_KILLSTREAK:
            return "KILL STREAK";
        case KD:
            return "KD RATIO";
        case KILLS:
            return "KILLS";
        default:
            return "Error";
        
        }
    }

    private String trim(String name) {
        return name.length() <= 15 ? name : name.substring(0, 15);
    }

    public StatsEntry get(StatsObjective objective, int place) {
        Map<StatsEntry, Number> base = Maps.newHashMap();

        for (StatsEntry entry : stats.values()) {
            base.put(entry, entry.get(objective));
        }

        TreeMap<StatsEntry, Number> ordered = new TreeMap<>((first, second) -> {
            if (first.get(objective).doubleValue() >= second.get(objective).doubleValue()) {
                return -1;
            }
            return 1;
        });

        ordered.putAll(base);

        Map<StatsEntry, String> leaderboards = Maps.newLinkedHashMap();

        int index = 0;
        for (Map.Entry<StatsEntry, Number> entry : ordered.entrySet()) {
            
            if (entry.getKey().getDeaths() < 10 && objective == StatsObjective.KD) {
                continue;
            }
            
            leaderboards.put(entry.getKey(), entry.getValue() + "");

            index++;

            if (index == place + 1) {
                break;
            }
        }

        try {
            return Iterables.get(leaderboards.keySet(), place - 1);
        } catch (Exception e) {
            return null;
        }
    }

    public void clearAll() {
        stats.clear();
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Foxtrot.getInstance(), this::save);
    }

    public void clearLeaderboards() {
        leaderboardHeads.clear();
        leaderboardSigns.clear();
        objectives.clear();

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Foxtrot.getInstance(), this::save);
    }

    public Map<StatsEntry, String> getLeaderboards(StatsTopCommand.StatsObjective objective, int range) {
        if (objective != StatsTopCommand.StatsObjective.KD) {
            Map<StatsEntry, Number> base = Maps.newHashMap();

            for (StatsEntry entry : stats.values()) {
                base.put(entry, entry.get(objective));
            }

            TreeMap<StatsEntry, Number> ordered = new TreeMap<>((first, second) -> {
                if (first.get(objective).doubleValue() >= second.get(objective).doubleValue()) {
                    return -1;
                }

                return 1;
            });
            ordered.putAll(base);

            Map<StatsEntry, String> leaderboards = Maps.newLinkedHashMap();

            int index = 0;
            for (Map.Entry<StatsEntry, Number> entry : ordered.entrySet()) {
                leaderboards.put(entry.getKey(), entry.getValue() + "");

                index++;

                if (index == range) {
                    break;
                }
            }

            return leaderboards;
        } else {
            Map<StatsEntry, Double> base = Maps.newHashMap();

            for (StatsEntry entry : stats.values()) {
                base.put(entry, entry.getKD());
            }

            TreeMap<StatsEntry, Double> ordered = new TreeMap<>((first, second) -> {
                if (first.getKD() > second.getKD()) {
                    return -1;
                }

                return 1;
            });
            ordered.putAll(base);

            Map<StatsEntry, String> leaderboards = Maps.newHashMap();

            int index = 0;
            for (Map.Entry<StatsEntry, Double> entry : ordered.entrySet()) {
                if (entry.getKey().getDeaths() < 10) {
                    continue;
                }

                String kd = Team.DTR_FORMAT.format((double) entry.getKey().getKills() / (double) entry.getKey().getDeaths());

                leaderboards.put(entry.getKey(), kd);

                index++;

                if (index == range) {
                    break;
                }
            }

            return leaderboards;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) {
            if (leaderboardHeads.containsKey(event.getBlock().getLocation())) {
                leaderboardHeads.remove(event.getBlock().getLocation());
                player.sendMessage(ChatColor.RED + "Removed this skull from leaderboards.");

                Bukkit.getScheduler().scheduleAsyncDelayedTask(Foxtrot.getInstance(), this::save);
            }

            if (leaderboardSigns.containsKey(event.getBlock().getLocation())) {
                leaderboardSigns.remove(event.getBlock().getLocation());
                player.sendMessage(ChatColor.RED + "Removed this sign from leaderboards.");

                Bukkit.getScheduler().scheduleAsyncDelayedTask(Foxtrot.getInstance(), this::save);
            }
        }
    }

}
