package net.frozenorb.foxtrot.tab;

import cc.fyre.proton.Proton;
import cc.fyre.proton.tab.construct.TabLayout;
import cc.fyre.proton.tab.provider.LayoutProvider;
import com.google.common.collect.Maps;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.EventScheduledTime;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.foxtrot.tab.types.Tab;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.util.PlayerDirection;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class FoxtrotTabLayoutProvider implements LayoutProvider {


    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private LinkedHashMap<Team, Integer> cachedTeamList = Maps.newLinkedHashMap();
    long cacheLastUpdated;

    @Override
    public TabLayout provide(Player player) {
        TabListMode mode = Foxtrot.getInstance().getTabListModeMap().getTabListMode(player.getUniqueId());

        TabLayout layout = TabLayout.create(player);

        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);
        String server = Foxtrot.getInstance().getServerHandler().getTabServerName();

        layout.set(Tab.MIDDLE, 1, server);
        layout.set(Tab.MIDDLE, 2, "&7&ovexor.cc");

        if (team == null) {
            layout.set(Tab.MIDDLE, 5, "");
            layout.set(Tab.MIDDLE, 6, "");
            layout.set(Tab.MIDDLE, 7, "");
            layout.set(Tab.MIDDLE, 8, "");
            layout.set(Tab.MIDDLE, 9, "");
        } else {
            Location hq = team.getHQ();

            layout.set(Tab.MIDDLE, 5, "&6&lFaction Info");
            layout.set(Tab.MIDDLE, 6, "&7Name: &e" + team.getName());
            layout.set(Tab.MIDDLE, 7, "&7DTR: &e" + team.getDTRColor() + decimalFormat.format(team.getDTR()) + team.getDTRSuffix());
            layout.set(Tab.MIDDLE, 9, "&7Points: &e" + team.getPoints());
            layout.set(Tab.MIDDLE, 8, "&7HQ: &e" + (hq == null ? "No Location" : hq.getBlockX() + ", " + hq.getBlockZ()));

//            if (team.getFocusedTeam() != null) {
//                layout.set(Tab.MIDDLE, 12, "&6&lFocus Info");
//                layout.set(Tab.MIDDLE, 13, "&7Name: &e" + team.getFocusedTeam().getName());
//                layout.set(Tab.MIDDLE, 14, "&7DTR: &e" + team.getFocusedTeam().getDTRColor() + decimalFormat.format(team.getFocusedTeam().getDTR()) + team.getFocusedTeam().getDTRSuffix());
//                layout.set(Tab.MIDDLE, 15, "&7HQ: &e" + (team.getFocusedTeam().getHQ() == null ? "No Location" : team.getFocusedTeam().getHQ().getBlockX() + ", " + team.getFocusedTeam().getHQ().getBlockZ()));
//                layout.set(Tab.MIDDLE, 16, "&7Points: &e" + team.getFocusedTeam().getPoints());
//
//                }
            }

            layout.set(Tab.LEFT, 5, "&6&lStats");
            layout.set(Tab.LEFT, 6, "&7Kills: &e" + Foxtrot.getInstance().getKillsMap().getKills(player.getUniqueId()));
            layout.set(Tab.LEFT, 7, "&7Deaths: &e" + Foxtrot.getInstance().getDeathsMap().getDeaths(player.getUniqueId()));
            layout.set(Tab.LEFT, 8, "&7Balance: &2$&a" + NumberFormat.getNumberInstance(Locale.US).format(Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId())));
            layout.set(Tab.LEFT, 9, "&7Lives: &4❤&c" + Foxtrot.getInstance().getSoulboundLivesMap().getLives(player.getUniqueId()));

            layout.set(Tab.RIGHT, 5, "&6&lMap Info");
            layout.set(Tab.RIGHT, 6, "&7Map Kit: &eP1 S1");
            layout.set(Tab.RIGHT, 7, "&7Faction Size: &e" + Foxtrot.getInstance().getMapHandler().getTeamSize());
            layout.set(Tab.RIGHT, 8, "&7Allies: &e" + (Foxtrot.getInstance().getMapHandler().getAllyLimit() == 0 ? "No Allies" : Foxtrot.getInstance().getMapHandler().getAllyLimit()));

            Location loc = player.getLocation();
            Team ownerTeam = LandBoard.getInstance().getTeam(loc);
            String location;

            if (ownerTeam != null) {
                location = ownerTeam.getName(player.getPlayer());
            } else if (!Foxtrot.getInstance().getServerHandler().isWarzone(loc)) {
                location = ChatColor.DARK_GREEN + "Wilderness";
            } else if (LandBoard.getInstance().getTeam(loc) != null && LandBoard.getInstance().getTeam(loc).getName().equalsIgnoreCase("citadel")) {
                location = ChatColor.DARK_PURPLE + "Citadel";
            } else {
                location = ChatColor.RED + "Warzone";
            }

            String direction = PlayerDirection.getCardinalDirection(player);

            layout.set(Tab.RIGHT, 12, "&6&lLocation");
            layout.set(Tab.RIGHT, 13, location);
            if (direction != null) {
                layout.set(Tab.RIGHT, 14, ChatColor.WHITE + "(" + loc.getBlockX() + ", " + loc.getBlockZ() + ") [" + direction + "]");
            } else {
                layout.set(Tab.RIGHT, 14, ChatColor.WHITE + "(" + loc.getBlockX() + ", " + loc.getBlockZ() + ")");
            }

            layout.set(Tab.LEFT, 12, "&6&lNext Event");

            KOTH activeKOTH = null;

            for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
                if (!(event instanceof KOTH)) continue;
                KOTH koth = (KOTH) event;
                if (koth.isActive() && !koth.isHidden()) {
                    activeKOTH = koth;
                    break;
                }
            }

            if (activeKOTH == null) {
                Date now = new Date();

                String nextKothName = null;
                Date nextKothDate = null;

                for (Map.Entry<EventScheduledTime, String> entry : Foxtrot.getInstance().getEventHandler().getEventSchedule().entrySet()) {
                    if (entry.getKey().toDate().after(now)) {
                        if (nextKothDate == null || nextKothDate.getTime() > entry.getKey().toDate().getTime()) {
                            nextKothName = entry.getValue();
                            nextKothDate = entry.getKey().toDate();
                        }
                    }
                }

                if (nextKothName != null) {
                    layout.set(Tab.LEFT, 13, "&9" + nextKothName);

                    Event event = Foxtrot.getInstance().getEventHandler().getEvent(nextKothName);

                    if (event != null && event instanceof KOTH) {
                        KOTH koth = (KOTH) event;

                        int seconds = (int) ((nextKothDate.getTime() - System.currentTimeMillis()) / 1000);

                        String time = formatIntoDetailedString(seconds)
                                .replace("minutes", "min").replace("minute", "min")
                                .replace("seconds", "sec").replace("second", "sec");

                        layout.set(0, 14, time);
                    }
                }
            } else {
                layout.set(0, 13, activeKOTH.getName());
                layout.set(0, 14, "&7" + TimeUtils.formatIntoHHMMSS(activeKOTH.getRemainingCapTime()));
            }

            return layout;
        }

        public static double getKDR (UUID uuid){
            int kills = Foxtrot.getInstance().getKillsMap().getKills(uuid);
            int deaths = Foxtrot.getInstance().getDeathsMap().getDeaths(uuid);

            if (kills == 0) {
                return 0.0;
            }

            if (deaths == 0) {
                return 1.0;
            }

            return (double) kills / (double) deaths;
        }


        private void kitmap (Player player, TabLayout layout){
            boolean velt = Foxtrot.getInstance().getMapHandler().getScoreboardTitle().contains("Velt");
            String dominantColor = velt ? "&6" : "&6";

            if (velt) {
                layout.set(1, 1, "&6&lVexor");
            } else {
                layout.set(1, 1, "&6&lVexor");
            }

            layout.set(0, 4, "&6&lMap Info");
            layout.set(0, 5, "&7Map Kit: &eP 1, S 1");
            layout.set(0, 6, "&7Faction Size: &e" + Foxtrot.getInstance().getMapHandler().getTeamSize());
            layout.set(0, 7, "&7Border: &e3000");

            int y = 8;
            String titleColor = dominantColor + "&l";
            String infoColor = "&7";

            KOTH activeKOTH = null;
            for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
                if (!(event instanceof KOTH)) continue;
                KOTH koth = (KOTH) event;
                if (koth.isActive() && !koth.isHidden()) {
                    activeKOTH = koth;
                    break;
                }
            }

            if (activeKOTH == null) {
                Date now = new Date();

                String nextKothName = null;
                Date nextKothDate = null;

                for (Map.Entry<EventScheduledTime, String> entry : Foxtrot.getInstance().getEventHandler().getEventSchedule().entrySet()) {
                    if (entry.getKey().toDate().after(now)) {
                        if (nextKothDate == null || nextKothDate.getTime() > entry.getKey().toDate().getTime()) {
                            nextKothName = entry.getValue();
                            nextKothDate = entry.getKey().toDate();
                        }
                    }
                }

                if (nextKothName != null) {
                    layout.set(0, ++y, titleColor + "Next KOTH:");
                    layout.set(0, ++y, infoColor + nextKothName);

                    Event event = Foxtrot.getInstance().getEventHandler().getEvent(nextKothName);

                    if (event != null && event instanceof KOTH) {
                        KOTH koth = (KOTH) event;
                        layout.set(0, ++y, infoColor.toString() + koth.getCapLocation().getBlockX() + ", " + koth.getCapLocation().getBlockY() + ", " + koth.getCapLocation().getBlockZ()); // location

                        int seconds = (int) ((nextKothDate.getTime() - System.currentTimeMillis()) / 1000);
                        layout.set(0, ++y, titleColor + "Goes active in:");

                        String time = formatIntoDetailedString(seconds)
                                .replace("minutes", "min").replace("minute", "min")
                                .replace("seconds", "sec").replace("second", "sec");

                        layout.set(0, ++y, infoColor + time);
                    }
                }
            } else {
                layout.set(0, ++y, titleColor + activeKOTH.getName());
                layout.set(0, ++y, infoColor + TimeUtils.formatIntoHHMMSS(activeKOTH.getRemainingCapTime()));
                layout.set(0, ++y, infoColor.toString() + activeKOTH.getCapLocation().getBlockX() + ", " + activeKOTH.getCapLocation().getBlockY() + ", " + activeKOTH.getCapLocation().getBlockZ()); // location
            }

            if (velt) {
                layout.set(1, 2, "&dOnline&7: " + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
            } else {
                layout.set(1, 2, "&7Online&7: &e" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
            }

            layout.set(1, 4, titleColor + "Faction Info");
            Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);
            if (team != null) {
                layout.set(1, 5, "&7Name: &e" + team.getName());
                if (team.getHQ() != null) {
                    String homeLocation = infoColor.toString() + "Home: " + team.getHQ().getBlockX() + ", " + team.getHQ().getBlockY() + ", " + team.getHQ().getBlockZ();
                    layout.set(1, 6, homeLocation);
                } else {
                    layout.set(1, 6, infoColor + "Home: Not Set");
                }

                layout.set(1, 7, "&7Balance: $" + (int) team.getBalance());
            } else {
                layout.set(1, 5, "&7None");
            }

            layout.set(2, 4, titleColor + "Player Info");
            layout.set(2, 5, "&7Kills: &e" + Foxtrot.getInstance().getKillsMap().getKills(player.getUniqueId()));
            layout.set(2, 6, "&7Deaths: &e" + Foxtrot.getInstance().getDeathsMap().getDeaths(player.getUniqueId()));
            layout.set(2, 6, "&7Deaths: &e" + Foxtrot.getInstance().getDeathsMap().getDeaths(player.getUniqueId()));
            layout.set(2, 7, "&7Balance: &a$" + (int) Foxtrot.getInstance().getWrappedBalanceMap().getBalance(player.getUniqueId()));

            layout.set(2, 9, titleColor + "Location");

            String location;

            Location loc = player.getLocation();
            Team ownerTeam = LandBoard.getInstance().getTeam(loc);

            if (ownerTeam != null) {
                location = ownerTeam.getName(player.getPlayer());
            } else if (!Foxtrot.getInstance().getServerHandler().isWarzone(loc)) {
                location = ChatColor.DARK_GREEN + "Wilderness";
            } else if (LandBoard.getInstance().getTeam(loc) != null && LandBoard.getInstance().getTeam(loc).getName().equalsIgnoreCase("citadel")) {
                location = titleColor + "Citadel";
            } else {
                location = ChatColor.RED + "Warzone";
            }

            layout.set(2, 11, location);

        /* Getting the direction 4 times a second for each player on the server may be intensive.
        We may want to cache the entire location so it is accessed no more than 1 time per second.
        FIXME, WIP */
            String direction = PlayerDirection.getCardinalDirection(player);
            if (direction != null) {
                layout.set(2, 10, ChatColor.GRAY + "(" + loc.getBlockX() + ", " + loc.getBlockZ() + ") [" + direction + "]");
            } else {
                layout.set(2, 10, ChatColor.GRAY + "(" + loc.getBlockX() + ", " + loc.getBlockZ() + ")");
            }

        }

        public static String formatIntoDetailedString ( int secs){
            if (secs <= 60) {
                return "1 minute";
            } else {
                int remainder = secs % 86400;
                int days = secs / 86400;
                int hours = remainder / 3600;
                int minutes = remainder / 60 - hours * 60;
                String fDays = days > 0 ? " " + days + " day" + (days > 1 ? "s" : "") : "";
                String fHours = hours > 0 ? " " + hours + " hour" + (hours > 1 ? "s" : "") : "";
                String fMinutes = minutes > 0 ? " " + minutes + " minute" + (minutes > 1 ? "s" : "") : "";
                return (fDays + fHours + fMinutes).trim();
            }

        }

    }
