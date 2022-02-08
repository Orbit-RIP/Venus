package net.frozenorb.foxtrot.scoreboard;

import cc.fyre.proton.Proton;
import cc.fyre.proton.scoreboard.construct.ScoreGetter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.abilities.impl.partners.portablebard.PortableBardAbility;
import net.frozenorb.foxtrot.commands.EOTWCommand;
import net.frozenorb.foxtrot.commands.SOTWCommand;
import net.frozenorb.foxtrot.customtimer.CustomTimer;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.EventType;
import net.frozenorb.foxtrot.events.conquest.game.ConquestGame;
import net.frozenorb.foxtrot.events.dtc.DTC;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.foxtrot.events.ktk.KillTheKing;
import net.frozenorb.foxtrot.listener.GoldenAppleListener;
import net.frozenorb.foxtrot.misc.game.Game;
import net.frozenorb.foxtrot.misc.game.GameHandler;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.ArcherClass;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.BardClass;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.commands.team.TeamStuckCommand;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.ChatMode;
import net.frozenorb.foxtrot.util.Logout;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FoxtrotScoreGetter implements ScoreGetter {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public String getChat(Player player) {
        Profile user = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId());

        ChatMode chatMode = Foxtrot.getInstance().getChatModeMap().getChatMode(player.getUniqueId());

        if (chatMode == ChatMode.TEAM) {
            return "&aTeam";
        }

        if (chatMode == ChatMode.ALLIANCE) {
            return "&eAlliance";
        }

        return "&eGlobal";
    }

    public void getScores(LinkedList<String> scores, Player player) {
        if (Foxtrot.getInstance().getMapHandler().getGameHandler() != null && Foxtrot.getInstance().getMapHandler().getGameHandler().isOngoingGame()) {
            Game ongoingGame = Foxtrot.getInstance().getMapHandler().getGameHandler().getOngoingGame();
            if (ongoingGame.isPlayingOrSpectating(player.getUniqueId())) {
                ongoingGame.getScoreboardLines(player, scores);
                scores.addFirst("&a&7&m--------------------");
                scores.add("&b&7&m--------------------");
                return;
            }
        }

        String spawnTagScore = getSpawnTagScore(player);
        String enderpearlScore = getEnderpearlScore(player);
        String pvpTimerScore = getPvPTimerScore(player);
        String archerMarkScore = getArcherMarkScore(player);
        String bardEffectScore = getBardEffectScore(player);
        String bardEnergyScore = getBardEnergyScore(player);
        String fstuckScore = getFStuckScore(player);
        String logoutScore = getLogoutScore(player);
        String homeScore = getHomeScore(player);
        String appleScore = getAppleScore(player);
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);
        ConquestGame conquest = Foxtrot.getInstance().getConquestHandler().getGame();

        if (Foxtrot.getInstance().getMapHandler().isKitMap()) {
            if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                scores.add(" &6» &fKills&7: &e" + Foxtrot.getInstance().getKillsMap().getKills(player.getUniqueId()));
                scores.add(" &6» &fDeaths&7: &e" + Foxtrot.getInstance().getDeathsMap().getDeaths(player.getUniqueId()));
                scores.add(" &6» &fTokens&7: &e" + Foxtrot.getInstance().getCreditsMap().getCredits(player.getUniqueId()));
            }
        }

        if (spawnTagScore != null) {
            scores.add("&c&lSpawn Tag&7: &c" + spawnTagScore);
        }

        if (homeScore != null) {
            scores.add("&9&lHome&7: **&c" + homeScore);
        }

        long cooldown = Foxtrot.getInstance().getOppleMap().getCooldown(player.getUniqueId());
        if (cooldown > System.currentTimeMillis()) {
            long millisLeft = cooldown - System.currentTimeMillis();
            String msg = TimeUtils.formatLongIntoHHMMSS(TimeUnit.MILLISECONDS.toSeconds(millisLeft));
            scores.add("&6&lGapple&7: **&c" + msg);
        }

        if (appleScore != null) {
            scores.add("&e&lApple&7: **&c" + appleScore);
        }

        if (enderpearlScore != null) {
            scores.add("&e&lEnderpearl&7: &c" + enderpearlScore);
        }

        if (pvpTimerScore != null) {
            if (Foxtrot.getInstance().getStartingPvPTimerMap().get(player.getUniqueId())) {
                scores.add("&a&lStarting Timer&7: &c" + pvpTimerScore);
            } else {
                scores.add("&a&lPvP Timer&7: &c" + pvpTimerScore);
            }
        }

        Iterator<Map.Entry<String, Long>> iterator = SOTWCommand.getCustomTimers().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> timer = iterator.next();
            if (timer.getValue() < System.currentTimeMillis()) {
                iterator.remove();
                continue;
            }

            if (timer.getKey().equals("&a&lS&c&lO&A&LT&C&LW")) {
                if (SOTWCommand.hasSOTWEnabled(player.getUniqueId())) {
                    scores.add(ChatColor.translateAlternateColorCodes('&', "&a&lS&c&lO&A&LT&C&LW&7: &c" + getTimerScore(timer)));
                } else {
                    scores.add(ChatColor.translateAlternateColorCodes('&', "&a&lS&c&lO&A&LT&C&LW&7: &c" + getTimerScore(timer)));
                }
            }
        }

        for (CustomTimer timer : CustomTimer.customTimers) {
            long time = timer.getTime();
            if (getTimerScore(time) != null) {
                scores.add(timer.getName() + "&7: &c" + getTimerScore(time));
            } else {
                CustomTimer.customTimers.remove(timer);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), timer.getCommand());
            }
        }

        for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
            if (!event.isActive() || event.isHidden()) {
                continue;
            }

            String displayName;

            switch (event.getName()) {
                case "EOTW":
                    displayName = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "EOTW";
                    break;
                case "Citadel":
                    displayName = ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Citadel";
                    break;
                default:
                    displayName = ChatColor.BLUE.toString() + ChatColor.BOLD + event.getName();
                    break;
            }

            if (event.getType() == EventType.DTC) {
                scores.add(displayName + "&7: &c" + ((DTC) event).getCurrentPoints());
            } else {
                scores.add(displayName + "&7: &c" + ScoreFunction.TIME_SIMPLE.apply((float) ((KOTH) event).getRemainingCapTime()));
            }
        }

        if (team != null && team.getFocusedTeam() != null
                && conquest == null) {
//            scores.add("&1&7&m--------------------");
            scores.add(CC.translate("&6&lTeam&7: &f" + team.getFocusedTeam().getName()));

            if (team.getFocusedTeam().getHQ() != null) {
                scores.add(CC.translate("&6&lHQ&7: &f" + team.getFocusedTeam().getHQ().getBlockX() + ", "
                        + team.getFocusedTeam().getHQ().getBlockZ() +
                        (player.getWorld().getEnvironment() == World.Environment.NORMAL ? " &7("
                                + ((int) team.getFocusedTeam().getHQ().distance(player.getLocation())) + "m)" : "")));
            }

            scores.add(CC.translate("&6&lDTR&7: &f" + team.getFocusedTeam().getDTRColor()
                    + decimalFormat.format(team.getFocusedTeam().getDTR())
                    + team.getFocusedTeam().getDTRSuffix()));

            scores.add(CC.translate("&6&lOnline&7: &f" +
                    team.getFocusedTeam().getOnlineMembers().size() + "&7/&f" +
                    team.getFocusedTeam().getMembers().size()));
//            scores.add("&6&7&m--------------------");


        }
        if (EOTWCommand.isFfaEnabled()) {
            long ffaEnabledAt = EOTWCommand.getFfaActiveAt();
            if (System.currentTimeMillis() < ffaEnabledAt) {
                long difference = ffaEnabledAt - System.currentTimeMillis();
                scores.add("&4&lFFA&7: &c" + ScoreFunction.TIME_SIMPLE.apply(difference / 1000F));
            }
        }

        if (archerMarkScore != null) {
            scores.add("&6&lArcher Mark&7: &c" + archerMarkScore);
        }

        if (bardEffectScore != null) {
            scores.add("&a&lEffect Cooldown&7: &c" + bardEffectScore);
        }

        if (bardEnergyScore != null) {
            scores.add("&e&lBard Energy&7: &c" + bardEnergyScore);
        }

        if (fstuckScore != null) {
            scores.add("&4&lStuck&7: &c" + fstuckScore);
        }

        if (logoutScore != null) {
            scores.add("&4&lLogout&7: &c" + logoutScore);
        }


        if (conquest != null) {
            if (scores.size() != 0) {
                scores.add("&c&7&m--------------------");
            }
            KOTH red = (KOTH) Foxtrot.getInstance().getEventHandler().getEvent("Conquest-RED");
            KOTH yellow = (KOTH) Foxtrot.getInstance().getEventHandler().getEvent("Conquest-YELLOW");
            KOTH blue = (KOTH) Foxtrot.getInstance().getEventHandler().getEvent("Conquest-BLUE");
            KOTH green = (KOTH) Foxtrot.getInstance().getEventHandler().getEvent("Conquest-GREEN");

            scores.add("&6&lConquest:");
            scores.add((ChatColor.GREEN + " \u2588 " + ChatColor.GRAY + "(" + green.getRemainingCapTime() + ChatColor.GRAY + "s) " + (green.getRemainingCapTime() < 10 ? "  " : "") + ChatColor.YELLOW + "\u2588 " + ChatColor.GRAY + "(" + yellow.getRemainingCapTime() + ChatColor.GRAY + "s)"));
            scores.add((ChatColor.BLUE + " \u2588 " + ChatColor.GRAY + "(" + blue.getRemainingCapTime() + ChatColor.GRAY + "s) " + (blue.getRemainingCapTime() < 10 ? "  " : "") + ChatColor.RED + "\u2588 " + ChatColor.GRAY + "(" + red.getRemainingCapTime() + ChatColor.GRAY + "s)"));
            AtomicInteger displayed = new AtomicInteger();
            conquest.getTeamPoints().forEach((key, value) -> {
                Team resolved = Foxtrot.getInstance().getTeamHandler().getTeam(key);
                if (displayed.get() != 3) {
                    if (resolved != null) {
                        scores.add((" " + resolved.getName(player) + "&7 (" + value) + " Point" + (value == 1 ? "" : "s") + ")");
                        displayed.getAndIncrement();
                    }
                }
                return;
            });
        }

        if (Proton.getInstance().getAutoRebootHandler().isRebooting()) {
            scores.add("&4&lRebooting&7: &c" + TimeUtils.formatIntoMMSS(Proton.getInstance().getAutoRebootHandler().getRebootSecondsRemaining()));
        }

        if (Foxtrot.getInstance().getKillTheKing() != null) {
            KillTheKing killTheKing = Foxtrot.getInstance().getKillTheKing();
            Player king = Bukkit.getServer().getPlayer(killTheKing.getUuid());

            if (king == null) {
                Foxtrot.getInstance().setKillTheKing(null);
            }

            scores.add("&6&lKill The King:");
            scores.add("  &6King: &f" + king.getDisplayName());
            scores.add("  &6Location: &r" + king.getLocation().getBlockX() + ", " + king.getLocation().getBlockZ());
            scores.add("  &6Lasted: &r" + TimeUtils.formatLongIntoHHMMSS(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - killTheKing.getStarted())));
        }

        boolean containsPortableBard = false;
        for (Ability ability : Foxtrot.getInstance().getAbilityManager().getAbilities().values()) {
            if (!ability.getCooldown().containsKey(player.getUniqueId()) || ability.getCooldown().get(player.getUniqueId()) == null) {
                continue;
            }

            if (ability.getIdentifier().equalsIgnoreCase("FAST_TRACK")) {
                continue;
            }

            cooldown = ability.getCooldown().get(player.getUniqueId());

            if (ability instanceof PortableBardAbility && PortableBardAbility.COOLDOWN.containsKey(player.getUniqueId()))
                cooldown = PortableBardAbility.COOLDOWN.get(player.getUniqueId());

            long remaining = cooldown - System.currentTimeMillis();

            if (remaining < 0L) {
                continue;
            }

            /*if (remaining > 180000) {
                continue;
            }*/

            if (ability instanceof PortableBardAbility) {
                if (containsPortableBard) continue;

                scores.add(ability.getScoreboardPrefix() + "&7: &c" + ScoreFunction.TIME_FANCY.apply((float) remaining / 1000));
                continue;
            }

            scores.add(ability.getScoreboardPrefix() + "&7: &c" + ScoreFunction.TIME_FANCY.apply((float) remaining / 1000));
        }

        GameHandler gameHandler = Foxtrot.getInstance().getMapHandler().getGameHandler();
        if (gameHandler != null) {
            if (gameHandler.isJoinable())
                scores.add("&5&lEvent&7: &c" + Foxtrot.getInstance().getMapHandler().getGameHandler().getOngoingGame().getGameType().getDisplayName() + " &7(/join)");
            else if (gameHandler.isHostCooldown())
                scores.add("&5&lEvent Cooldown&7: &c" + TimeUtils.formatIntoMMSS(gameHandler.getCooldownSeconds()));
        }

        if (!scores.isEmpty()) {
            if (player.hasMetadata("modmode")) {
                scores.addFirst("&7&m--------------------");
                scores.addFirst(" &6» &fVanished&7: &r" + (player.hasMetadata("invisible") ? "&aYes" : "&cNo"));
                scores.addFirst(" &6» &fChat&7: &f" + this.getChat(player));
                scores.addFirst("&6Staff Mode:");

            } else if (player.hasMetadata("vanish")) {
                scores.addFirst("&7&m--------------------");
                scores.addFirst(" &6» &fVanish&7: &r" + (player.hasMetadata("invisible") ? "&aYes" : "&cNo"));
            }
        }

        if (scores.isEmpty()) {
            if (player.hasMetadata("modmode")) {
                scores.addFirst(" &6» &fVanished&7: &r" + (player.hasMetadata("invisible") ? "&aYes" : "&cNo"));
                scores.addFirst(" &6» &fChat&7: &f" + this.getChat(player));
                scores.addFirst("&6Staff Mode:");

            } else if (player.hasMetadata("vanish")) {
                scores.addFirst(" &6» &fVanish&7: &r" + (player.hasMetadata("invisible") ? "&aYes" : "&cNo"));
            }
        }

        if (!scores.isEmpty()) {
            scores.addFirst("&a&7&m--------------------");
            scores.add(" ");
            scores.add("&7&ovexor.cc");
            scores.add("&e&7&m--------------------");
        }

        while (scores.size() > 15) {
            scores.remove(scores.size() - 1);
        }
        return;
    }


    public String getAppleScore(Player player) {
        if (GoldenAppleListener.getCrappleCooldown().containsKey(player.getUniqueId()) && GoldenAppleListener.getCrappleCooldown().get(player.getUniqueId()) >= System.currentTimeMillis()) {
            float diff = GoldenAppleListener.getCrappleCooldown().get(player.getUniqueId()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getHomeScore(Player player) {
        if (Foxtrot.getInstance().getServerHandler().getHomeTimer().containsKey(player.getName()) && Foxtrot.getInstance().getServerHandler().getHomeTimer().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = Foxtrot.getInstance().getServerHandler().getHomeTimer().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getFStuckScore(Player player) {
        if (TeamStuckCommand.getWarping().containsKey(player.getName())) {
            float diff = TeamStuckCommand.getWarping().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return null;
    }

    public String getLogoutScore(Player player) {
        Logout logout = Foxtrot.getInstance().getServerHandler().getTasks().get(player.getName());

        if (logout != null) {
            float diff = logout.getLogoutTime() - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return null;
    }

    public String getSpawnTagScore(Player player) {
        if (SpawnTagHandler.isTagged(player)) {
            float diff = SpawnTagHandler.getTag(player);

            if (diff >= 0) {
                return (ScoreFunction.TIME_SIMPLE.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getTimerScore(Map.Entry<String, Long> timer) {
        long diff = timer.getValue() - System.currentTimeMillis();

        if (diff > 0) {
            return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
        } else {
            return (null);
        }
    }

    public String getEnderpearlScore(Player player) {
        if (EnderpearlCooldownHandler.getEnderpearlCooldown().containsKey(player.getName()) && EnderpearlCooldownHandler.getEnderpearlCooldown().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = EnderpearlCooldownHandler.getEnderpearlCooldown().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getPvPTimerScore(Player player) {
        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
            int secondsRemaining = Foxtrot.getInstance().getPvPTimerMap().getSecondsRemaining(player.getUniqueId());

            if (secondsRemaining >= 0) {
                return (ScoreFunction.TIME_SIMPLE.apply((float) secondsRemaining));
            }
        }

        return (null);
    }

    public static String getTimerScore(Long timer) {
        long diff = timer - System.currentTimeMillis();

        if (diff > 0) {
            return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
        } else {
            return (null);
        }
    }


    public String getArcherMarkScore(Player player) {
        if (ArcherClass.isMarked(player)) {
            long diff = ArcherClass.getMarkedPlayers().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getBardEffectScore(Player player) {
        if (BardClass.getLastEffectUsage().containsKey(player.getName()) && BardClass.getLastEffectUsage().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = BardClass.getLastEffectUsage().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.TIME_SIMPLE.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getBardEnergyScore(Player player) {
        if (BardClass.getEnergy().containsKey(player.getName())) {
            float energy = BardClass.getEnergy().get(player.getName());

            if (energy > 0) {
                // No function here, as it's a "raw" value.
                return (String.valueOf(BardClass.getEnergy().get(player.getName())));
            }
        }

        return (null);
    }

    private String formatTPS(double tps) {
        return ((tps > 18.0) ? ChatColor.GREEN : ((tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED)).toString() + ((tps > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }

}