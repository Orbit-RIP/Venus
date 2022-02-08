package net.frozenorb.foxtrot.events.conquest.game;

import java.util.*;

import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.conquest.ConquestHandler;
import net.frozenorb.foxtrot.events.conquest.enums.ConquestCapzone;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.EventType;
import net.frozenorb.foxtrot.events.events.EventCapturedEvent;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.foxtrot.events.koth.events.EventControlTickEvent;
import net.frozenorb.foxtrot.events.koth.events.KOTHControlLostEvent;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.util.InventoryUtils;
import cc.fyre.proton.util.UUIDUtils;

public class ConquestGame implements Listener {

    @Getter
    private LinkedHashMap<ObjectId, Integer> teamPoints = new LinkedHashMap<>();

    public ConquestGame() {
        Foxtrot.getInstance().getServer().getPluginManager().registerEvents(this, Foxtrot.getInstance());

        for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
            if (event.getType() != EventType.KOTH) continue;
            KOTH koth = (KOTH) event;
            if (koth.getName().startsWith(ConquestHandler.KOTH_NAME_PREFIX)) {
                if (!koth.isHidden()) {
                    koth.setHidden(true);
                }

                if (koth.getCapTime() != ConquestHandler.TIME_TO_CAP) {
                    koth.setCapTime(ConquestHandler.TIME_TO_CAP);
                }

                koth.activate();
            }
        }

        String[] messages = new String[]{
                ChatColor.GRAY + "███████",
                ChatColor.GRAY + "██" + ChatColor.GOLD + "████" + ChatColor.GRAY + "█",
                ChatColor.GRAY + "█" + ChatColor.GOLD + "█" + ChatColor.GRAY + "█████ " + ChatColor.GOLD + "[Conquest]",
                ChatColor.GRAY + "█" + ChatColor.GOLD + "█" + ChatColor.GRAY + "█████ " + ChatColor.GOLD + "Conquest",
                ChatColor.GRAY + "█" + ChatColor.GOLD + "█" + ChatColor.GRAY + "█████ " + ChatColor.GOLD + "can be contested now.",
                ChatColor.GRAY + "█" + ChatColor.GOLD + "█" + ChatColor.GRAY + "█████",
                ChatColor.GRAY + "██" + ChatColor.GOLD + "████" + ChatColor.GRAY + "█",
                ChatColor.GRAY + "███████"
        };

        Arrays.stream(messages).forEach(Bukkit::broadcastMessage);
        Foxtrot.getInstance().getConquestHandler().setGame(this);
    }

    public void endGame(final Team winner) {
        if (winner == null) {
            Foxtrot.getInstance().getServer().broadcastMessage(ConquestHandler.PREFIX + " " + ChatColor.GOLD + "Conquest has ended.");
        } else {
            winner.setConquestsCapped(winner.getConquestsCapped() + 1);
            String[] messages = new String[]{
                    ChatColor.GRAY + "███████",
                    ChatColor.GRAY + "██" + ChatColor.GOLD + "████" + ChatColor.GRAY + "█",
                    ChatColor.GRAY + "█" + ChatColor.GOLD + "█" + ChatColor.GRAY + "█████ " + ChatColor.GOLD + "[Conquest]",
                    ChatColor.GRAY + "█" + ChatColor.GOLD + "█" + ChatColor.GRAY + "█████ " + ChatColor.YELLOW + "controlled by",
                    ChatColor.GRAY + "█" + ChatColor.GOLD + "█" + ChatColor.GRAY + "█████ " + ChatColor.DARK_RED + winner.getName(),
                    ChatColor.GRAY + "█" + ChatColor.GOLD + "█" + ChatColor.GRAY + "█████",
                    ChatColor.GRAY + "██" + ChatColor.GOLD + "████" + ChatColor.GRAY + "█",
                    ChatColor.GRAY + "███████"
            };
            Arrays.stream(messages).forEach(Bukkit::broadcastMessage);
            //Foxtrot.getInstance().getServer().broadcastMessage(ConquestHandler.PREFIX + " " + ChatColor.GOLD.toString() + ChatColor.BOLD + winner.getName() + ChatColor.GOLD + " has won Conquest!");
        }

        for (Event koth : Foxtrot.getInstance().getEventHandler().getEvents()) {
            if (koth.getName().startsWith(ConquestHandler.KOTH_NAME_PREFIX)) {
                koth.deactivate();
            }
        }

        HandlerList.unregisterAll(this);
        Foxtrot.getInstance().getConquestHandler().setGame(null);
    }

    @EventHandler
    public void onKOTHCaptured(final EventCapturedEvent event) {
        if (!event.getEvent().getName().startsWith(ConquestHandler.KOTH_NAME_PREFIX)) {
            return;
        }

        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(event.getPlayer());
        ConquestCapzone capzone = ConquestCapzone.valueOf(event.getEvent().getName().replace(ConquestHandler.KOTH_NAME_PREFIX, "").toUpperCase());

        if (team == null) {
            return;
        }

        int points = 1;

        if (teamPoints.containsKey(team.getUniqueId())) {
            if (capzone.getName().contains("Sky") || capzone.getName().contains("Middle")) {
                teamPoints.put(team.getUniqueId(), teamPoints.get(team.getUniqueId()) + 2);
                points = 2;
            } else {
                teamPoints.put(team.getUniqueId(), teamPoints.get(team.getUniqueId()) + 1);
            }
        } else {
            if (capzone.getName().contains("Sky") || capzone.getName().contains("Middle")) {
                teamPoints.put(team.getUniqueId(), 2);
                points = 2;
            } else {
                teamPoints.put(team.getUniqueId(), 1);
            }
        }

        teamPoints = sortByValues(teamPoints);
        Foxtrot.getInstance().getServer().broadcastMessage(ConquestHandler.PREFIX + " " + ChatColor.WHITE + team.getName() + ChatColor.GRAY + " has captured " + capzone.getColor() + capzone.getName() + ChatColor.GRAY + " and gained " + ChatColor.WHITE.toString() + points + " point" + (points == 1 ? "" : "s") + ChatColor.GRAY + "!" + ChatColor.RED + " (" + teamPoints.get(team.getUniqueId()) +
                "/" + ConquestHandler.getPointsToWin() + ")");

        if (teamPoints.get(team.getUniqueId()) >= ConquestHandler.getPointsToWin()) {
            endGame(team);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr givekey " + event.getPlayer().getName() + " Conquest 5");
        } else {
            new BukkitRunnable() {

                public void run() {
                    if (Foxtrot.getInstance().getConquestHandler().getGame() != null) {
                        event.getEvent().activate();
                    }
                }

            }.runTaskLater(Foxtrot.getInstance(), 10L);
        }
    }

    @EventHandler
    public void onKOTHControlLost(KOTHControlLostEvent event) {
        if (!event.getKOTH().getName().startsWith(ConquestHandler.KOTH_NAME_PREFIX)) {
            return;
        }

        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(UUIDUtils.uuid(event.getKOTH().getCurrentCapper()));
        ConquestCapzone capzone = ConquestCapzone.valueOf(event.getKOTH().getName().replace(ConquestHandler.KOTH_NAME_PREFIX, "").toUpperCase());

        if (team == null) {
            return;
        }

        team.sendMessage(ConquestHandler.PREFIX + ChatColor.WHITE + " " + event.getKOTH().getCurrentCapper() + ChatColor.GRAY + " was knocked off of " + capzone.getColor() + capzone.getName() + ChatColor.GRAY + "!");
    }

    @EventHandler
    public void onKOTHControlTick(EventControlTickEvent event) {

        if (!event.getKOTH().getName().startsWith(ConquestHandler.KOTH_NAME_PREFIX) || event.getKOTH().getRemainingCapTime() % 5 != 0) {
            return;
        }

        ConquestCapzone capzone = ConquestCapzone.valueOf(event.getKOTH().getName().replace(ConquestHandler.KOTH_NAME_PREFIX, "").toUpperCase());
        Player capper = Foxtrot.getInstance().getServer().getPlayerExact(event.getKOTH().getCurrentCapper());

        if (capper != null) {
            Team team = Foxtrot.getInstance().getTeamHandler().getTeam(capper.getUniqueId());

            if (team == null)
                return;

            capper.sendMessage(ConquestHandler.PREFIX + " " + ChatColor.GRAY + "Attempting to capture " + capzone.getColor() + capzone.getName() + ChatColor.GRAY + "! (" + event.getKOTH().getRemainingCapTime() + "s)");
        }
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(event.getEntity());

        if (team == null || !teamPoints.containsKey(team.getUniqueId())) {
            return;
        }

        teamPoints.put(team.getUniqueId(), Math.max(0, teamPoints.get(team.getUniqueId()) - ConquestHandler.POINTS_DEATH_PENALTY));
        teamPoints = sortByValues(teamPoints);
        team.sendMessage(ConquestHandler.PREFIX + ChatColor.WHITE + event.getEntity().getName() + ChatColor.GRAY + " has died and your faction has lost " + ChatColor.RED + ConquestHandler.POINTS_DEATH_PENALTY + " points " + ChatColor.GRAY + "(" + teamPoints.get(team.getUniqueId()) + "/" + ConquestHandler.getPointsToWin() + ")");

    }

    private static LinkedHashMap<ObjectId, Integer> sortByValues(Map<ObjectId, Integer> map) {
        LinkedList<Map.Entry<ObjectId, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        LinkedHashMap<ObjectId, Integer> sortedHashMap = new LinkedHashMap<>();
        Iterator<Map.Entry<ObjectId, Integer>> iterator = list.iterator();

        while (iterator.hasNext()) {
            java.util.Map.Entry<ObjectId, Integer> entry = iterator.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHashMap;
    }

}