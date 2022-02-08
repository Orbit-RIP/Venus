package net.frozenorb.foxtrot.misc.game.impl.sumo;

import lombok.Getter;
import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.misc.game.Game;
import net.frozenorb.foxtrot.misc.game.GameState;
import net.frozenorb.foxtrot.misc.game.GameType;
import net.frozenorb.foxtrot.misc.game.GameUtils;
import net.frozenorb.foxtrot.misc.game.arena.GameArena;
import net.frozenorb.foxtrot.util.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class SumoGame extends Game {

    private Player playerA;
    private Player playerB;
    private Map<UUID, Integer> roundsPlayed = new HashMap<>();
    private int currentRound = 0;

    public SumoGame(UUID host, List<GameArena> arenaOptions) {
        super(host, GameType.SUMO, arenaOptions);
    }

    @Override
    public void startGame() {
        super.startGame();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (state == GameState.ENDED) {
                    cancel();
                    return;
                }

                if (state == GameState.RUNNING) {
                    determineNextPlayers();
                    startRound();
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Foxtrot.getInstance(), 10L, 10L);
    }

    @Override
    public void gameBegun() {
        for (Player player : getPlayers()) {
            player.teleport(getVotedArena().getSpectatorSpawn());
        }
    }

    public void startRound() {
        if (playerA == null || playerB == null) {
            throw new IllegalStateException("Cannot start round without both players");
        }

        currentRound++;
        setStartedAt(System.currentTimeMillis());

        InventoryUtils.resetInventoryNow(playerA);
        playerA.teleport(getVotedArena().getPointA());

        InventoryUtils.resetInventoryNow(playerB);
        playerB.teleport(getVotedArena().getPointB());

        new BukkitRunnable() {
            private int i = 6;

            @Override
            public void run() {
                if (state == GameState.ENDED) {
                    cancel();
                    return;
                }

                i--;

                if (i == 0) {
                    sendMessages(ChatColor.YELLOW + "The round has started!");
                    sendSound(Sound.NOTE_PLING, 1F, 2F);
                } else {
                    sendMessages(ChatColor.YELLOW + "The round is starting in " + ChatColor.LIGHT_PURPLE + i + ChatColor.YELLOW + " second" + (i == 1 ? "" : "s") + "...");
                    sendSound(Sound.NOTE_PLING, 1F, 1F);
                }

                if (i <= 0) {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Foxtrot.getInstance(), 20L, 20L);
    }

    public void endRound() {
        if (players.size() == 1) {
            endGame();
        } else {
            if (playerA != null) {
                playerA.teleport(getVotedArena().getSpectatorSpawn());
                GameUtils.resetPlayer(playerA);
                playerA = null;
            }

            if (playerB != null) {
                playerB.teleport(getVotedArena().getSpectatorSpawn());
                GameUtils.resetPlayer(playerB);
                playerB = null;
            }

            Bukkit.getServer().getScheduler().runTaskLater(Foxtrot.getInstance(), () -> {
                determineNextPlayers();
                startRound();
            }, 50L);
        }
    }

    @Override
    public void eliminatePlayer(Player player, Player killer) {
        if (state == GameState.ENDED) return;

        super.eliminatePlayer(player, killer);

        if (killer != null) {
            sendMessages(ChatColor.LIGHT_PURPLE.toString() + player.getName() + ChatColor.YELLOW + " has been eliminated by " + ChatColor.LIGHT_PURPLE + killer.getName() + ChatColor.YELLOW + "! " + ChatColor.GRAY + "(" + getPlayers().size() + "/" + getStartedWith() + " players remaining)");
        }

        if (isCurrentlyFighting(player)) {
            if (playerA.getUniqueId() == player.getUniqueId()) {
                playerA = null;
            } else if (playerB.getUniqueId() == player.getUniqueId()) {
                playerB = null;
            }

            endRound();
        }
    }

    public boolean isCurrentlyFighting(Player player) {
        return (playerA != null && playerA.getUniqueId() == player.getUniqueId()) || (playerB != null && playerB.getUniqueId() == player.getUniqueId());
    }

    public Player getOpponent(Player player) {
        if (playerA != null && playerA.getUniqueId() == player.getUniqueId()) {
            return playerB;
        }

        if (playerB != null && playerB.getUniqueId() == player.getUniqueId()) {
            return playerA;
        }

        return null;
    }

    public void determineNextPlayers() {
        List<Player> players = getPlayers().stream().sorted(Comparator.comparingInt(player -> roundsPlayed.getOrDefault(player.getUniqueId(), 0))).collect(Collectors.toList());

        playerA = players.get(0);
        playerB = players.get(1);

        roundsPlayed.putIfAbsent(playerA.getUniqueId(), 0);
        roundsPlayed.put(playerA.getUniqueId(), roundsPlayed.get(playerA.getUniqueId()) + 1);

        roundsPlayed.putIfAbsent(playerB.getUniqueId(), 0);
        roundsPlayed.put(playerB.getUniqueId(), roundsPlayed.get(playerB.getUniqueId()) + 1);

        sendMessages(ChatColor.GOLD.toString() + ChatColor.BOLD + "Next round: " + ChatColor.RESET + playerA.getName() + ChatColor.GRAY + " vs. " + ChatColor.RESET + playerB.getName());
    }

    public double getDeathHeight() {
        return Math.min(getVotedArena().getPointA().getBlockY(), getVotedArena().getPointB().getBlockY()) - 2.9;
    }

    @Override
    public void handleDamage(Player victim, Player damager, EntityDamageByEntityEvent event) {
        if (state == GameState.RUNNING) {
            if (isPlaying(victim.getUniqueId()) && isPlaying(damager.getUniqueId())) {
                event.setDamage(0.0);

                if (!isCurrentlyFighting(victim) || !isCurrentlyFighting(damager)) {
                    event.setCancelled(true);
                } else {
                    victim.setHealth(victim.getMaxHealth());
                    victim.updateInventory();
                }
            } else {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }

    @Override
    public Player findWinningPlayer() {
        return playerA == null ? playerB : playerA;
    }

    @Override
    public void getScoreboardLines(Player player, LinkedList<String> lines) {
        if (state == GameState.WAITING) {
            lines.add("&6Players: &f" + players.size() + "&7/&f" + getMaxPlayers());

            if (getVotedArena() != null) {
                lines.add("&6Map: &f" + getVotedArena().getName());
            } else {
                lines.add("");
                lines.add("&6Map Vote");

                getArenaOptions().entrySet().stream().sorted((o1, o2) -> o2.getValue().get()).forEach(entry -> {
                    lines.add("&7» " + (getPlayerVotes().getOrDefault(player.getUniqueId(), null) == entry.getKey() ? "&l" : "") + entry.getKey().getName() + " &7(" + entry.getValue().get() + ")");
                });
            }

            if (getStartedAt() == null) {
                int playersNeeded = getGameType().getMinPlayers() - getPlayers().size();
                lines.add("");
                lines.add("&e&oWaiting for " + playersNeeded + " player" + (playersNeeded == 1 ? "" : "s"));
            } else {
                float remainingSeconds = (getStartedAt() - System.currentTimeMillis()) / 1000F;
                lines.add("&a&oStarting in " + ((double) Math.round(10.0D * (double) remainingSeconds) / 10.0D) + "s");
            }
        } else if (state == GameState.RUNNING) {
            lines.add("&6Remaining: &f" + players.size() + "&7/&f" + getStartedWith());
            lines.add("&6Round: &f" + currentRound);

            if (playerA != null && playerB != null) {
                lines.add("");

                final int namesLength = playerA.getName().length() + playerB.getName().length();
                Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(playerA.getUniqueId());
                Profile pB = Nebula.getInstance().getProfileHandler().fromUuid(playerB.getUniqueId());
                if (namesLength <= 20) {
                    lines.add(profile.getFancyName() + " &7vs. &r" + pB.getFancyName());
                } else {
                    lines.add(profile.getFancyName());
                    lines.add("&7vs.");
                    lines.add(profile.getFancyName());
                }
            } else {
                lines.add("");
                lines.add("&e&oSelecting new players...");
            }
        } else {
            if (winningPlayer == null) {
                lines.add("&6Winner: &fNone");
            } else {
                lines.add("&6Winner: &f" + winningPlayer.getName());
            }

            lines.add("&6Rounds: &f" + currentRound);
        }
    }

    @Override
    public List<FancyMessage> createHostNotification() {
        return Arrays.asList(
                new FancyMessage("█████████").color(ChatColor.GRAY),
                new FancyMessage("")
                        .then("██").color(ChatColor.GRAY)
                        .then("█████").color(ChatColor.AQUA)
                        .then("██").color(ChatColor.GRAY),
                new FancyMessage("")
                        .then("██").color(ChatColor.GRAY)
                        .then("█").color(ChatColor.AQUA)
                        .then("██████").color(ChatColor.GRAY)
                        .then(" " + getGameType().getDisplayName() + " Event").color(ChatColor.AQUA).style(ChatColor.BOLD),
                new FancyMessage("")
                        .then("██").color(ChatColor.GRAY)
                        .then("█████").color(ChatColor.AQUA)
                        .then("██").color(ChatColor.GRAY)
                        .then(" Hosted by ").color(ChatColor.GRAY)
                        .then(getHostName()).color(ChatColor.AQUA),
                new FancyMessage("")
                        .then("██████").color(ChatColor.GRAY)
                        .then("█").color(ChatColor.AQUA)
                        .then("██").color(ChatColor.GRAY)
                        .then(" [").color(ChatColor.GRAY)
                        .then("Click to join event").color(ChatColor.YELLOW)
                        .command("/game join")
                        .formattedTooltip(new FancyMessage("Click here to join the event.").color(ChatColor.YELLOW))
                        .then("]").color(ChatColor.GRAY),
                new FancyMessage("")
                        .then("██").color(ChatColor.GRAY)
                        .then("█████").color(ChatColor.AQUA)
                        .then("██").color(ChatColor.GRAY),
                new FancyMessage("█████████").color(ChatColor.GRAY)
        );
    }

}
