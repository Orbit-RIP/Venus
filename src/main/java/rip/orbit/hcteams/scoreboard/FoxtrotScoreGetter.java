package rip.orbit.hcteams.scoreboard;

import cc.fyre.proton.Proton;
import cc.fyre.proton.scoreboard.construct.ScoreFunction;
import cc.fyre.proton.scoreboard.construct.ScoreGetter;
import cc.fyre.proton.util.TimeUtils;
import cc.fyre.proton.util.UUIDUtils;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.ability.Ability;
import rip.orbit.hcteams.commands.staff.EOTWCommand;
import rip.orbit.hcteams.commands.staff.SOTWCommand;
import rip.orbit.hcteams.customtimer.CustomTimer;
import rip.orbit.hcteams.events.Event;
import rip.orbit.hcteams.events.EventType;
import rip.orbit.hcteams.events.conquest.game.ConquestGame;
import rip.orbit.hcteams.events.dtc.DTC;
import rip.orbit.hcteams.events.killtheking.KingEvent;
import rip.orbit.hcteams.events.koth.KOTH;
import rip.orbit.hcteams.events.purge.commands.PurgeCommands;
import rip.orbit.hcteams.listener.kits.GoldenAppleListener;
import rip.orbit.hcteams.map.duel.Duel;
import rip.orbit.hcteams.map.game.Game;
import rip.orbit.hcteams.map.game.GameHandler;
import rip.orbit.hcteams.map.stats.StatsEntry;
import rip.orbit.hcteams.pvpclasses.PvPClass;
import rip.orbit.hcteams.pvpclasses.PvPClassHandler;
import rip.orbit.hcteams.pvpclasses.pvpclasses.ArcherClass;
import rip.orbit.hcteams.pvpclasses.pvpclasses.BardClass;
import rip.orbit.hcteams.server.EnderpearlCooldownHandler;
import rip.orbit.hcteams.server.ServerHandler;
import rip.orbit.hcteams.server.SpawnTagHandler;
import rip.orbit.hcteams.settings.Setting;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.commands.team.TeamStuckCommand;
import rip.orbit.hcteams.team.dtr.DTRBitmask;
import rip.orbit.hcteams.util.CC;
import rip.orbit.hcteams.util.Logout;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.timer.Timer;
import rip.orbit.nebula.timer.TimerHandler;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FoxtrotScoreGetter implements ScoreGetter {

    private String formatBasicTps(double tps) {
        return "" + Math.min(Math.round(tps * 10.0) / 10.0, 20.0);
    }

    public void getScores(LinkedList<String> scores, Player player) {
        Team team = HCF.getInstance().getTeamHandler().getTeam(player);

        if (HCF.getInstance().getMapHandler().getGameHandler() != null && HCF.getInstance().getMapHandler().getGameHandler().isOngoingGame()) {
            Game ongoingGame = HCF.getInstance().getMapHandler().getGameHandler().getOngoingGame();
            if (ongoingGame.isPlayingOrSpectating(player.getUniqueId())) {
                ongoingGame.getScoreboardLines(player, scores);
                scores.addFirst("&a&7&m--------------------");
                scores.add("&b&7&m--------------------");
                return;
            }
        }

        if (HCF.getInstance().getInDuelPredicate().test(player)) {
            Duel duel = HCF.getInstance().getMapHandler().getDuelHandler().getDuel(player);

            scores.add("&6&l┃ &fOpponent&7: &6" + UUIDUtils.name(duel.getOpponent(player.getUniqueId())));
            scores.addFirst("&a&7&m--------------------");
            scores.add("&b&7&m--------------------");
            return;
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

        String mainColor = CC.translate(HCF.getInstance().getConfig().getString("scoreboard.mainColor"));
        String otherColor = CC.translate(HCF.getInstance().getConfig().getString("scoreboard.otherColor"));
        String deathbanScore = getDeathbanScore(player);

        if (Setting.SCOREBOARD_STAFF_BOARD.isEnabled(player) && player.hasMetadata("modmode")) {
            double tps = Bukkit.spigot().getTPS()[1];
            scores.add(mainColor + "&6Mod Mode &7(" + (player.hasMetadata("invisible") ? "&aOn" : "&cOff") + "&7)");
            scores.add("" + otherColor + "&6&l┃ &fOnline&7: &6" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
            if (player.isOp()) {
                scores.add("" + otherColor + "&6&l┃ &fTPS&7: &6" + formatBasicTps(tps));
            }
        }

        if (HCF.getInstance().getMapHandler().isKitMap()) {
            StatsEntry stats = HCF.getInstance().getMapHandler().getStatsHandler().getStats(player.getUniqueId());
            if (!DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                scores.add(otherColor + "&6&l┃ &6Kills&7: &f" + stats.getKills());
                scores.add(otherColor + "&6&l┃ &6Deaths&7: &f" + stats.getDeaths());
                if (stats.getKillstreak() > 0) {
                    scores.add(otherColor + "&6&l┃ &6Killstreak&7: &f" + stats.getKillstreak());
                }
                scores.add(otherColor + "&6&l┃ &6Balance&7: &2&l" + "$&a" + Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId()));
            }
        }

        if (spawnTagScore != null) {
            scores.add("&c&lSpawn Tag&7: &c" + spawnTagScore);
        }

        if (homeScore != null) {
            scores.add("&9&lHome§7: &c" + homeScore);
        }

        if (appleScore != null) {
            scores.add("&6Apple&7: &c" + appleScore);
        }
        long cooldown = HCF.getInstance().getOppleMap().getCooldown(player.getUniqueId());
        if (cooldown > System.currentTimeMillis()) {
            long millisLeft = cooldown - System.currentTimeMillis();
            String msg = TimeUtils.formatLongIntoHHMMSS(TimeUnit.MILLISECONDS.toSeconds(millisLeft));
            scores.add("&6&lGopple&7: **&c" + msg);
        }
        if (enderpearlScore != null) {
            scores.add("&5&lEnderpearl&7: &c" + enderpearlScore);
        }


        if (KingEvent.isStarted(false)) {
            Player target = KingEvent.getFocusedPlayer();
            scores.add(mainColor + "&lKill The King");
            scores.add("&7┃" + otherColor + " King: " + target.getDisplayName());
            if (target != player) {
                scores.add("&7┃" + otherColor + " Location: &f" + target.getLocation().getBlockX() + ", " + target.getLocation().getBlockX() + ", " + target.getLocation().getBlockZ());
                if (target.getWorld() == player.getWorld()) {
                    int distance = (int) target.getLocation().distance(player.getLocation());
                    if (distance <= 3000) {
                        scores.add("&7┃" + otherColor + " Distance: &f" + distance + 'm');
                    }
                }
            }
            scores.add(otherColor + "&6&l┃ &6Reward: &f" + KingEvent.getReward());
            scores.add(otherColor + "&6&l┃ &6Time Left: &f" + DurationFormatUtils.formatDuration(KingEvent.getTime(), "mm:ss"));
        }

        if (pvpTimerScore != null) {
            if (HCF.getInstance().getStartingPvPTimerMap().get(player.getUniqueId())) {
                scores.add("&a&lStarting Timer&7: &c" + pvpTimerScore);
            } else {
                scores.add("&a&LPvP Timer&7: &c" + pvpTimerScore);
            }
        }

        Iterator<Map.Entry<String, Long>> iterator = SOTWCommand.getCustomTimers().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> timer = iterator.next();
            if (timer.getValue() < System.currentTimeMillis()) {
                iterator.remove();
                continue;
            }

            switch (timer.getKey()) {
                case "&a&lSOTW ends in":
                    if (SOTWCommand.hasSOTWEnabled(player.getUniqueId())) {
                        scores.add(ChatColor.translateAlternateColorCodes('&', "&a&lSOTW &a&mends in &a&l&m " + getTimerScore(timer)));
                    } else {
                        scores.add(ChatColor.translateAlternateColorCodes('&', "&a&lSOTW &aends in &a&l" + getTimerScore(timer)));
                    }
                    break;
                case "&e&lMOTW":
                    if (SOTWCommand.hasMOTWEnabled(player.getUniqueId())) {
                        scores.add(ChatColor.translateAlternateColorCodes('&', "&e&lMOTW &e&mends in &e&l&m " + getTimerScore(timer)));
                    } else {
                        scores.add(ChatColor.translateAlternateColorCodes('&', "&e&lSOTW &eends in &e&l" + getTimerScore(timer)));
                    }
                    break;
                    case "&a&lDONATOR":
                        scores.add(SOTWCommand.SOTW_PREFIX.getCurrentString() + "&7: &c" + getTimerScore(timer));
                        break;
                    case "&a&lMOTWSALE":
                        scores.add(SOTWCommand.MOTW_PREFIX.getCurrentString() + "&7: &c" + getTimerScore(timer));
                        break;
                    case "&a&lOTHERSALE":
                        scores.add(SOTWCommand.OTHER_PREFIX.getCurrentString() + "&7: &c" + getTimerScore(timer));
                        break;
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

        if (PurgeCommands.isPurgeTimer()) {
            scores.add(ChatColor.translateAlternateColorCodes('&', "&c&lPurge Event&7: &c" + PurgeCommands.getCustomTimers()));
        }

        for (Event event : HCF.getInstance().getEventHandler().getEvents()) {
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
                    displayName = ChatColor.WHITE.toString() + event.getName();
                    break;
            }

            if (event.getType() == EventType.DTC) {
                scores.add("&6&l┃ " + displayName + "&7: &c" + ((DTC) event).getCurrentPoints());
            } else {
                scores.add("&6&l┃ " + displayName + "&7: &c" + ScoreFunction.TIME_SIMPLE.apply((float) ((KOTH) event).getRemainingCapTime()));
            }
        }

        if (EOTWCommand.isFfaEnabled()) {
            long ffaEnabledAt = EOTWCommand.getFfaActiveAt();
            if (System.currentTimeMillis() < ffaEnabledAt) {
                long difference = ffaEnabledAt - System.currentTimeMillis();
                scores.add("&c&lFFA&7: &c" + ScoreFunction.TIME_SIMPLE.apply(difference / 1000F));
            }
        }
        PvPClass classs = PvPClassHandler.getPvPClass(player);
        if (classs != null) {
            scores.add("&a&lActive Class&7: &c" + classs.getName());
        }

        if (archerMarkScore != null) {
            scores.add("&c&LArcher Mark&7: &c" + archerMarkScore);
        }

        if (bardEffectScore != null && classs != null) {
            scores.add("&6&lBard Effect&7: &c" + bardEffectScore);
        }

        if (bardEnergyScore != null && classs != null) {
            scores.add("&a&LBard Energy&7: &c" + bardEnergyScore);
        }
        if (ArcherClass.getLastSpeedUsage().containsKey(player.getName()) && ArcherClass.getLastSpeedUsage().get(player.getName()) > System.currentTimeMillis() && classs != null) {
            long millisLeft = ArcherClass.getLastSpeedUsage().get(player.getName()) - System.currentTimeMillis();
            String msg = TimeUtils.formatIntoMMSS((int) millisLeft / 1000);
            scores.add("&6&l┃ &fSpeed&7: &c" + msg);
        }
        if (ArcherClass.getLastJumpUsage().containsKey(player.getName()) && ArcherClass.getLastJumpUsage().get(player.getName()) > System.currentTimeMillis() && classs != null) {
            long millisLeft = ArcherClass.getLastJumpUsage().get(player.getName()) - System.currentTimeMillis();
            String msg = TimeUtils.formatIntoMMSS((int) millisLeft / 1000);
            scores.add("&6&l┃ &fJump Boost&7: &c" + msg);
        }

        if (fstuckScore != null) {
            scores.add("&4&lStuck&7: &c" + fstuckScore);
        }

        if (logoutScore != null) {
            scores.add("&c&lLogout&7: &c" + logoutScore);
        }

        if (HCF.getInstance().getAbilityHandler().getAbilityCD().onCooldown(player)) {
            scores.add("&a&lAbility Item&7: &c" + HCF.getInstance().getAbilityHandler().getAbilityCD().getRemaining(player));
        }

        GameHandler gameHandler = HCF.getInstance().getMapHandler().getGameHandler();
        if (gameHandler != null) {
            if (gameHandler.isJoinable())
                scores.add("&5&lEvent&7: &c" + HCF.getInstance().getMapHandler().getGameHandler().getOngoingGame().getGameType().getDisplayName() + " &7(/join)");
            else if (gameHandler.isHostCooldown())
                scores.add("&5&lEvent Cooldown&7: &c" + gameHandler.getCooldownSeconds() + "s");
        }

        if (HCF.getInstance().getToggleAbilityCDsSBMap().isEnabled(player.getUniqueId())) {
            for (Ability ability : HCF.getInstance().getAbilityHandler().getAbilities()) {
                if (ability.cooldown().onCooldown(player)) {
                    scores.add("&6&l┃ &6" + ChatColor.stripColor(ability.displayName()) + "&7: &c" + ability.cooldown().getRemaining(player));
                }
            }
        }


        ConquestGame conquest = HCF.getInstance().getConquestHandler().getGame();

        if (conquest != null) {
            if (scores.size() != 0) {
                scores.add("&c&7&m--------------------");
            }
            KOTH red = (KOTH) HCF.getInstance().getEventHandler().getEvent("Conquest-RED");
            KOTH yellow = (KOTH) HCF.getInstance().getEventHandler().getEvent("Conquest-YELLOW");
            KOTH blue = (KOTH) HCF.getInstance().getEventHandler().getEvent("Conquest-BLUE");
            KOTH green = (KOTH) HCF.getInstance().getEventHandler().getEvent("Conquest-GREEN");

            scores.add("&6&lConquest:");
            scores.add((ChatColor.GREEN + " \u2588 " + ChatColor.GRAY + "(" + green.getRemainingCapTime() + ChatColor.GRAY + "s) " + (green.getRemainingCapTime() < 10 ? "  " : "") + ChatColor.YELLOW + "\u2588 " + ChatColor.GRAY + "(" + yellow.getRemainingCapTime() + ChatColor.GRAY + "s)"));
            scores.add((ChatColor.BLUE + " \u2588 " + ChatColor.GRAY + "(" + blue.getRemainingCapTime() + ChatColor.GRAY + "s) " + (blue.getRemainingCapTime() < 10 ? "  " : "") + ChatColor.RED + "\u2588 " + ChatColor.GRAY + "(" + red.getRemainingCapTime() + ChatColor.GRAY + "s)"));
            AtomicInteger displayed = new AtomicInteger();
            conquest.getTeamPoints().forEach((key, value) -> {
                Team resolved = HCF.getInstance().getTeamHandler().getTeam(key);
                if (displayed.get() != 3) {
                    if (resolved != null) {
                        scores.add((" " + resolved.getName(player) + "&7 (" + value) + " Point" + (value == 1 ? "" : "s") + ")");
                        displayed.getAndIncrement();
                    }
                }
            });
        }

        if (Proton.getInstance().getAutoRebootHandler().isRebooting()) {
            scores.add("&4&lRebooting&7: &c" + TimeUtils.formatIntoMMSS(Proton.getInstance().getAutoRebootHandler().getRebootSecondsRemaining()));
        }
        if (HCF.getInstance().getClaimOnSbMap().isToggled(player.getUniqueId())) {
            if (team != null) {
                if (team.getFactionFocused() != null || conquest != null) {
                    scores.add(" ");
                }
            }
        }
        if (team != null) {
            String dtrColored;
            double dtr;
            if (team.getFactionFocused() != null) {
                Team focusedFaction = team.getFactionFocused();
                scores.add(mainColor + "&6Team&7: " + focusedFaction.getName(player) + " &7(&f" + focusedFaction.getOnlineMemberAmount() + "&7/&f" + focusedFaction.getMembers().size() + "&7)");

                dtr = Double.parseDouble((new DecimalFormat("#.##")).format(focusedFaction.getDTR()));
                if (dtr >= 1.01D) {
                    dtrColored = ChatColor.GREEN + String.valueOf(dtr);
                } else if (dtr <= 0.0D) {
                    dtrColored = ChatColor.RED + String.valueOf(dtr);
                } else {
                    dtrColored = ChatColor.YELLOW + String.valueOf(dtr);
                }

                if (focusedFaction.getHq() != null) {
                    scores.add(mainColor + "&6&l┃ &6HQ&7: &f" + focusedFaction.getHq().getBlockX() + ", " + focusedFaction.getHq().getBlockZ());
                } else {
                    scores.add(mainColor + "&6&l┃ &6HQ&7: &cNot set.");
                }
                scores.add(mainColor + "&6&l┃ &6DTR&7: &r" + dtrColored + focusedFaction.getDTRSuffix());
            }

            TimerHandler timerHandler = Nebula.getInstance().getTimerHandler();
            int i = 0;
            for (Timer timer : timerHandler.getTimers()) {
                if (timer.getTimeLeft() > 0) {
                    if (i == 1) {
                        scores.add(" ");
                    }
                    scores.add("&6&l┃ " + timer.getDisplay() + ": &c" + TimeUtils.formatLongIntoHHMMSS(timer.getTimeLeft() / 1000));
                    i++;
                }
            }
        }

        if (!scores.isEmpty()) {
            scores.addFirst("&a&7&m--------------------");
            if (HCF.getInstance().getConfig().getBoolean("scoreboard.footerboolean")) {
                scores.add("&c&l");
                scores.add(HCF.getInstance().getAnimationHandler().getFooter());
            }
            scores.add("&e&7&m--------------------");
        }

        while (scores.size() > 15) {
            scores.remove(scores.size() - 1);
        }

    }

    public String getDeathbanScore(Player player) {
        if (HCF.getInstance().getDeathbanMap().isDeathbanned(player.getUniqueId())) {
            float diff = HCF.getInstance().getDeathbanMap().getDeathban(player.getUniqueId()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return null;
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
        if (ServerHandler.getHomeTimer().containsKey(player.getName()) && ServerHandler.getHomeTimer().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = ServerHandler.getHomeTimer().get(player.getName()) - System.currentTimeMillis();

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
        Logout logout = ServerHandler.getTasks().get(player.getName());

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
        if (HCF.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
            int secondsRemaining = HCF.getInstance().getPvPTimerMap().getSecondsRemaining(player.getUniqueId());

            if (secondsRemaining >= 0) {
                return (ScoreFunction.TIME_SIMPLE.apply((float) secondsRemaining));
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
                return (String.valueOf(BardClass.getEnergy().get(player.getName())));
            }
        }

        return (null);
    }

}