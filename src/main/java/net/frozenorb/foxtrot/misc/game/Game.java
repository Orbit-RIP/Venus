package net.frozenorb.foxtrot.misc.game;

import cc.fyre.proton.Proton;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.misc.game.arena.GameArena;
//import net.frozenorb.foxtrot.modsuite.ModSuite;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.foxtrot.util.InventoryUtils;
import net.frozenorb.foxtrot.util.ItemUtils;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
public abstract class Game {

    private final UUID host;
    private final GameType gameType;

    @Setter
    protected GameState state = GameState.WAITING;

    private Long nextAnnouncement = System.currentTimeMillis();

    private boolean hasExpired;
    @Setter
    private Long startedAt;
    @Setter
    private int startedWith;
    @Setter
    private int maxPlayers;

    @Setter
    private boolean gemRequiredToJoin = true;
    @Setter
    private boolean hostForceStarted;
    @Setter
    private int voteStart;

    protected Set<UUID> players = new HashSet<>();
    private Set<UUID> spectators = new HashSet<>();
    private Set<UUID> usedMessage = new HashSet<>();
    private Set<UUID> playerGems = new HashSet<>();

    private Map<GameArena, AtomicInteger> arenaOptions = new HashMap<>();
    private Map<UUID, GameArena> playerVotes = new HashMap<>();
    private GameArena votedArena;

    protected Player winningPlayer;

    public Game(UUID host, GameType gameType, List<GameArena> arenaOptions) {
        this.host = host;
        this.gameType = gameType;
        this.maxPlayers = gameType.getMaxPlayers();

        if (arenaOptions.size() == 1) {
            this.votedArena = arenaOptions.get(0);
        } else {
            for (GameArena arena : arenaOptions) {
                this.arenaOptions.put(arena, new AtomicInteger(0));
            }
        }
    }

    public void hostForceStart() {
        if (hostForceStarted || host == null) return;
        hostForceStarted = true;

        sendMessages(
                "",
                ChatColor.GREEN + "The event has been started by " + Proton.getInstance().getUuidCache().name(host) + "!",
                ""
        );
    }

    public void forceStart() {
        state = GameState.RUNNING;
        startedWith = players.size();
        gameBegun();

        sendMessages(
                "",
                ChatColor.GOLD + "The event has been forcefully started by an administrator!",
                ""
        );
    }

    public void startGame() {
        List<UUID> list = new ArrayList<>(players);
        Collections.shuffle(list);
        players = new HashSet<>(list);

        for (Player player : getPlayersAndSpectators()) {
            player.teleport(votedArena.getSpectatorSpawn());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (state != GameState.WAITING) {
                    cancel();
                    return;
                }

                if (System.currentTimeMillis() > nextAnnouncement) {
                    nextAnnouncement = System.currentTimeMillis() + 15_000L;

                    List<FancyMessage> notificationMessages = createHostNotification();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        for (FancyMessage message : notificationMessages) {
                            message.send(player);
                        }
                    }

                }

                if (startedAt == null) {
                    if (players.size() >= gameType.getMinPlayers() || hostForceStarted || (voteStart >= players.size() && players.size() > 0)) {
                        startedAt = System.currentTimeMillis() + 30_000L;
                    }
                } else {
                    if (System.currentTimeMillis() > startedAt) {
                        state = GameState.RUNNING;
                        startedWith = players.size();
                        gameBegun();
                    } else {
                        if (System.currentTimeMillis() > startedAt - 5_000L && votedArena == null) {
                            votedArena = getArenaOptions().entrySet().stream().sorted((o1, o2) -> o1.getValue().get()).collect(Collectors.toList()).get(0).getKey();
                            sendMessages(ChatColor.GOLD.toString() + votedArena.getName() + ChatColor.YELLOW.toString() + " has won the map vote!");
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(Foxtrot.getInstance(), 10L, 10L);

        new BukkitRunnable() {
            private final long expiresAt = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3L);
            private final List<Integer> broadcasts = new ArrayList<>(Arrays.asList(5, 10, 15));

            @Override
            public void run() {
                if (state != GameState.WAITING || getStartedAt() != null) {
                    cancel();
                    return;
                }

                long diff = expiresAt - System.currentTimeMillis();
                int diffSeconds = (int) (diff / 1000L);

                if (diff <= 0L) {
                    sendMessages(
                            "",
                            ChatColor.RED + "The event has been cancelled because it couldn't get enough players!",
                            ""
                    );

                    hasExpired = true;
                    endGame();
                    Foxtrot.getInstance().getMapHandler().getGameHandler().endGame();

                    cancel();
                } else if (broadcasts.remove((Integer) diffSeconds)) {
                    sendMessages(
                            "",
                            ChatColor.RED + "The event will be automatically cancelled for lack of players in " + TimeUtils.formatIntoDetailedString(diffSeconds) + "!",
                            ""
                    );
                }
            }
        }.runTaskTimerAsynchronously(Foxtrot.getInstance(), 10L, 10L);
    }

    public void endGame() {
        state = GameState.ENDED;
        winningPlayer = findWinningPlayer();

        if (winningPlayer != null) {
//            winningPlayer.sendMessage(ChatColor.GREEN + "You have received " + ChatColor.DARK_GREEN + "+" + playerGems.size() + " Gems" + ChatColor.GREEN + " for winning the event!");
        }

        playerGems.clear();

        new BukkitRunnable() {
            private int i = 5;

            @Override
            public void run() {
                i--;

                if (winningPlayer != null && i <= 3 && i > 0) {
                    Bukkit.broadcastMessage(winningPlayer.getDisplayName() + ChatColor.YELLOW + " has " + ChatColor.GREEN + "won" + ChatColor.YELLOW + " the event!");
                    winningPlayer.playSound(winningPlayer.getLocation(), Sound.FIREWORK_BLAST, 2F, 1F);
                }

                if (i <= 0) {
                    cancel();

                    // this block of code should never throw errors, but just in case it does,
                    // lets wrap in a try-catch so the game gets cleared from the game handler
                    try {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Player player : getPlayers()) {
                                    removePlayer(player);
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + winningPlayer.getDisplayName() + " EVENT 3");
                                }

                                for (Player spectator : getSpectators()) {
                                    removeSpectator(spectator);
                                }
                            }
                        }.runTask(Foxtrot.getInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Foxtrot.getInstance().getMapHandler().getGameHandler().endGame();
                }
            }
        }.runTaskTimerAsynchronously(Foxtrot.getInstance(), 20L, 20L);
    }

    public boolean isPlaying(UUID player) {
        return players.contains(player);
    }

    public boolean isSpectating(UUID player) {
        return spectators.contains(player);
    }

    public boolean isPlayingOrSpectating(UUID player) {
        return isPlaying(player) || isSpectating(player);
    }

    public void addPlayer(Player player) throws IllegalStateException {
        if (state != GameState.WAITING) {
            throw new IllegalStateException("That event has already started. Try spectating instead with /game spectate.");
        }

//        if (ModSuite.hasStaffMode(player)) {
//            player.sendMessage(ChatColor.RED + "You can't join the event while in mod-mode.");
//            return;
//        }

        if (SpawnTagHandler.isTagged(player)) {
            player.sendMessage(ChatColor.RED + "You can't join the event while spawn-tagged.");
            return;
        }

        if (!ItemUtils.hasEmptyInventory(player)) {
            player.sendMessage(ChatColor.RED + "You need to have an empty inventory to join the event.");
            return;
        }


        players.add(player.getUniqueId());
//        playerGems.add(player.getUniqueId());

        GameUtils.resetPlayer(player);

        if (Foxtrot.getInstance().getMapHandler().getGameHandler().getConfig().getLobbySpawn() != null) {
            player.teleport(Foxtrot.getInstance().getMapHandler().getGameHandler().getConfig().getLobbySpawn());
        }

        Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId());
        if (!usedMessage.contains(player.getUniqueId())) {
            sendMessages(profile.getFancyName() + ChatColor.YELLOW.toString() + " has joined the " + ChatColor.LIGHT_PURPLE + gameType.getDisplayName() + ChatColor.YELLOW + " event! (" + ChatColor.LIGHT_PURPLE + players.size() + "/" + maxPlayers + ChatColor.YELLOW + ")");
        }

//        if (getVotedArena() == null && getArenaOptions().size() > 1) {
//            new MapVoteMenu(this).openMenu(player);
//        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        SpawnTagHandler.removeTag(player);

        InventoryUtils.resetInventoryNow(player);
        player.teleport(Foxtrot.getInstance().getServerHandler().getSpawnLocation());

        if (state == GameState.WAITING) {
            if (!usedMessage.contains(player.getUniqueId())) {
                Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId());
                usedMessage.add(player.getUniqueId());
                sendMessages(profile.getFancyName() + ChatColor.YELLOW.toString() + " has left the " + ChatColor.LIGHT_PURPLE + gameType.getDisplayName() + ChatColor.YELLOW + " event! (" + ChatColor.LIGHT_PURPLE + players.size() + "/" + 128 + ChatColor.YELLOW + ")");
            }

//            playerGems.remove(player.getUniqueId());
//            Foxtrot.getInstance().getCreditsMap().setCredits(player.getUniqueId(),+1);
        }
    }

    public void eliminatePlayer(Player player, Player killer) {
        players.remove(player.getUniqueId());

        InventoryUtils.resetInventoryNow(player);

        if (killer == null) {
            if (Bukkit.getPlayer(player.getUniqueId()) != null) {
                sendMessages(ChatColor.LIGHT_PURPLE.toString() + player.getName() + ChatColor.YELLOW + " has been eliminated! " + ChatColor.GRAY + "(" + getPlayers().size() + "/" + getStartedWith() + " players remaining)");
            } else {
                sendMessages(ChatColor.LIGHT_PURPLE.toString() + player.getName() + ChatColor.YELLOW + " has disconnected and has been disqualified! " + ChatColor.GRAY + "(" + getPlayers().size() + "/" + getStartedWith() + " players remaining)");
            }
        }

        if (killer != null) {
            addSpectator(player);
        }
    }

    public void addSpectator(Player player) {
        spectators.add(player.getUniqueId());

        Bukkit.getScheduler().runTask(Foxtrot.getInstance(), () -> {
            GameUtils.resetPlayer(player);
            player.teleport(votedArena.getSpectatorSpawn());
        });
    }

    public void removeSpectator(Player player) {
        spectators.remove(player.getUniqueId());
        SpawnTagHandler.removeTag(player);

        Bukkit.getScheduler().runTask(Foxtrot.getInstance(), () -> {
            InventoryUtils.resetInventoryNow(player);
            player.teleport(Foxtrot.getInstance().getServerHandler().getSpawnLocation());
        });
    }

    public String getHostName() {
        return Proton.getInstance().getUuidCache().getImpl().name(host);
    }

    public List<Player> getPlayers() {
        if (players.isEmpty()) {
            return ImmutableList.of();
        }

        List<Player> players = new ArrayList<>();
        for (UUID uuid : this.players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                players.add(player);
            }
        }

        return players;
    }

    public List<Player> getSpectators() {
        if (spectators.isEmpty()) {
            return ImmutableList.of();
        }

        List<Player> spectators = new ArrayList<>();
        for (UUID uuid : this.spectators) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                spectators.add(player);
            }
        }

        return spectators;
    }

    public List<Player> getPlayersAndSpectators() {
        List<Player> playersAndSpectators = new ArrayList<>();
        playersAndSpectators.addAll(getPlayers());
        playersAndSpectators.addAll(getSpectators());
        return playersAndSpectators;
    }

    public void sendMessages(String... messages) {
        for (Player player : getPlayersAndSpectators()) {
            for (String message : messages) {
                player.sendMessage(message);
            }
        }
    }

    public void sendSound(Sound sound, float volume, float pitch) {
        for (Player player : getPlayersAndSpectators()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void handleDamage(Player victim, Player damager, EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    public abstract Player findWinningPlayer();

    public abstract void getScoreboardLines(Player player, LinkedList<String> lines);

    public abstract List<FancyMessage> createHostNotification();

    public void gameBegun() {
    }

}
