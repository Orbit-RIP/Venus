package rip.orbit.hcteams.map.stats;

import cc.fyre.proton.Proton;
import cc.fyre.proton.command.param.ParameterType;
import cc.fyre.proton.serialization.LocationSerializer;
import cc.fyre.proton.util.UUIDUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.com.google.common.collect.Iterables;
import net.minecraft.util.com.google.common.reflect.TypeToken;
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
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.commands.staff.SOTWCommand;
import rip.orbit.hcteams.commands.staff.SOTWCommand;
import rip.orbit.hcteams.map.stats.command.StatsTopCommand;
import rip.orbit.hcteams.map.stats.task.StatsTopTask;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.util.CC;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

import java.util.*;
import java.util.stream.Collectors;

public class StatsHandler implements Listener {

    private Map<UUID, StatsEntry> stats = Maps.newConcurrentMap();
    @Getter private Map<Integer, Location> topFactionHologram = Maps.newHashMap();

    @Getter @Setter
    private Location killTopHologram;


    @Getter private Map<Location, Integer> leaderboardSigns = Maps.newHashMap();
    @Getter private Map<Location, Integer> leaderboardHeads = Maps.newHashMap();

    @Getter private Map<Location, StatsTopCommand.StatsObjective> objectives = Maps.newHashMap();

    @Getter private Map<Integer, UUID> topKills = Maps.newConcurrentMap();

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
                    StatsTopCommand.StatsObjective obj = StatsTopCommand.StatsObjective.valueOf(objective.split("----")[1]);

                    objectives.put(location, obj);
                }

                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Kit Map] Loaded " + objectives.size() + " objectives.");
            }
            if (redis.exists(Bukkit.getServerName() + ":" + "topFacsHolo")) {
                List<String> serializedTopFacs = Proton.PLAIN_GSON.fromJson(redis.get(Bukkit.getServerName() + ":" + "topFacsHolo"), new TypeToken<List<String>>() {}.getType());

                for (String topFac : serializedTopFacs) {
                   Location location = LocationSerializer.deserialize((BasicDBObject) JSON.parse(topFac.split("----")[0]));
                    int pos = Integer.parseInt(topFac.split("----")[1]);

                    topFactionHologram.put(pos, location);
               }
           }

           if (redis.exists(Bukkit.getServerName() + ":" + "killTopLocation")) {
               killTopHologram = LocationSerializer.deserialize((BasicDBObject) JSON.parse(redis.get(Bukkit.getServerName() + ":" + "killTopLocation")));
            }
            return null;
        });

        Bukkit.getPluginManager().registerEvents(this, HCF.getInstance());

        Proton.getInstance().getCommandHandler().registerPackage(HCF.getInstance(), "rip.orbit.hcteams.map.stats.command");

        Proton.getInstance().getCommandHandler().registerParameterType(StatsTopCommand.StatsObjective.class, new ParameterType<StatsTopCommand.StatsObjective>() {


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
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(HCF.getInstance(), this::save, 30 * 20L, 30 * 20L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(HCF.getInstance(), this::updateTopKillsMap, 30 * 20L, 30 * 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(HCF.getInstance(), this::updatePhysicalLeaderboards, 60 * 20L, 60 * 20L);
    }

    public void save() {
        Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {
            List<String> serializedSigns = leaderboardSigns.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue()).collect(Collectors.toList());
            List<String> serializedHeads = leaderboardHeads.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue()).collect(Collectors.toList());
            List<String> serializedObjectives = objectives.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getKey()).toString() + "----" + entry.getValue().name()).collect(Collectors.toList());
            List<String> serializedTopFacs = topFactionHologram.entrySet().stream().map(entry -> LocationSerializer.serialize(entry.getValue()).toString() + "----" + entry.getValue()).collect(Collectors.toList());
            String killTopLocation = LocationSerializer.serialize(killTopHologram).toString();
            redis.set(Bukkit.getServerName() + ":" + "leaderboardSigns", Proton.PLAIN_GSON.toJson(serializedSigns));
            redis.set(Bukkit.getServerName() + ":" + "leaderboardHeads", Proton.PLAIN_GSON.toJson(serializedHeads));
            redis.set(Bukkit.getServerName() + ":" + "objectives", Proton.PLAIN_GSON.toJson(serializedObjectives));
            redis.set(Bukkit.getServerName() + ":" + "topFacsHolo", Proton.PLAIN_GSON.toJson(serializedTopFacs));
            redis.set(Bukkit.getServerName() + ":" + "killTopLocation", killTopLocation);
            // stats
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

    public void updateTopKillsMap() {

        new BukkitRunnable() {
            @Override
            public void run() {
                StatsTopTask.updateHolograms();
            }
        }.runTask(HCF.getInstance());

        UUID oldFirstPlace = this.topKills.get(1);
        UUID oldSecondPlace = this.topKills.get(2);
        UUID oldThirdPlace = this.topKills.get(3);

        UUID newFirstPlace = get(StatsTopCommand.StatsObjective.KILLS, 1).getOwner();
        UUID newSecondPlace = get(StatsTopCommand.StatsObjective.KILLS, 2).getOwner();
        UUID newThirdPlace = get(StatsTopCommand.StatsObjective.KILLS, 3).getOwner();

        if (!SOTWCommand.isSOTWTimer() || SOTWCommand.isMOTWTimer()) {
            if (firstUpdateComplete) {
                if (newFirstPlace != oldFirstPlace) {
                    if (newFirstPlace != null) {
                        Profile newProf = Nebula.getInstance().getProfileHandler().fromUuid(newFirstPlace, true);
                        Profile oldProf = Nebula.getInstance().getProfileHandler().fromUuid(oldFirstPlace, true);
                        Bukkit.broadcastMessage(CC.translate("&6" + newProf.getFancyName() + "&f has surpassed &6" + oldProf.getFancyName() + "&f for &6#1&f in kills!"));
                    }
                }

                if (newSecondPlace != oldSecondPlace) {
                    if (newSecondPlace != null) {
                        Profile newProf = Nebula.getInstance().getProfileHandler().fromUuid(newSecondPlace, true);
                        Profile oldProf = Nebula.getInstance().getProfileHandler().fromUuid(oldSecondPlace, true);
                        Bukkit.broadcastMessage(CC.translate("&6" + newProf.getFancyName() + "&f has surpassed &6" + oldProf.getFancyName() + "&f for &6#2&f in kills!"));
                    }
                }

                if (newThirdPlace != oldThirdPlace) {
                    if (newThirdPlace != null) {
                        Profile newProf = Nebula.getInstance().getProfileHandler().fromUuid(newThirdPlace, true);
                        Profile oldProf = Nebula.getInstance().getProfileHandler().fromUuid(oldThirdPlace, true);
                        Bukkit.broadcastMessage(CC.translate("&6" + newProf.getFancyName() + "&f has surpassed &6" + oldProf.getFancyName() + "&f for &6#3&f in kills!"));
                    }
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
            sign.setLine(1, trim(ChatColor.WHITE
                    .toString() + ChatColor.UNDERLINE + UUIDUtils.name(stats.getOwner())));

            sign.setLine(2, ChatColor.YELLOW.toString() + stats.get(objectives.get(entry.getKey())));
            sign.setLine(3, "");

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
        StatsTopCommand.StatsObjective objective = objectives.get(location);

        switch (objective) {
            case DEATHS:
                return "Top Deaths";
            case HIGHEST_KILLSTREAK:
                return "Top KillStrk";
            case KD:
                return "Top KDR";
            case KILLS:
                return "Top Kills";
            default:
                return "Error";

        }
    }

    private String trim(String name) {
        return name.length() <= 15 ? name : name.substring(0, 15);
    }

    public StatsEntry get(StatsTopCommand.StatsObjective objective, int place) {
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

            if (entry.getKey().getDeaths() < 10 && objective == StatsTopCommand.StatsObjective.KD) {
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
        Bukkit.getScheduler().scheduleAsyncDelayedTask(HCF.getInstance(), this::save);
    }

    public void clearLeaderboards() {
        leaderboardHeads.clear();
        leaderboardSigns.clear();
        objectives.clear();

        Bukkit.getScheduler().scheduleAsyncDelayedTask(HCF.getInstance(), this::save);
    }

    public Map<StatsEntry, String> getLeaderboards(StatsTopCommand.StatsObjective objective, int range) {
        if (objective != StatsTopCommand.StatsObjective.KD) {
            Map<StatsEntry, Number> base = Maps.newHashMap();

            for (StatsEntry entry : stats.values()) {
                base.put(entry, entry.get(objective));
            }

            TreeMap<StatsEntry, Number> ordered = new TreeMap<>((Comparator<StatsEntry>) (first, second) -> {
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

        if (leaderboardHeads.containsKey(event.getBlock().getLocation())) {
            leaderboardHeads.remove(event.getBlock().getLocation());
            player.sendMessage(ChatColor.YELLOW + "Removed this skull from leaderboards.");

            Bukkit.getScheduler().scheduleAsyncDelayedTask(HCF.getInstance(), this::save);
        }

        if (leaderboardSigns.containsKey(event.getBlock().getLocation())) {
            leaderboardSigns.remove(event.getBlock().getLocation());
            player.sendMessage(ChatColor.YELLOW + "Removed this sign from leaderboards.");

            Bukkit.getScheduler().scheduleAsyncDelayedTask(HCF.getInstance(), this::save);
        }
    }

    public void setupHologram(Location location, StatsTopCommand.StatsObjective objective) {



    }
}
