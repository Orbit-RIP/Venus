package net.frozenorb.foxtrot.map.stats.hologram;

import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.map.stats.StatsEntry;
import net.frozenorb.foxtrot.map.stats.StatsHandler;
import net.frozenorb.foxtrot.map.stats.command.StatsTopCommand;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.commands.team.TeamTopCommand;
import net.frozenorb.foxtrot.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.Map;


public class HologramRunnable extends BukkitRunnable {

    private static final String TOP_KILLS = "Kills";
    private static final String TOP_FACTIONS = "Factions";

    public HologramRunnable() {
        runTaskTimer(Foxtrot.getInstance(), 0L, (20 * 60) * 10);
    }

    @Override
    public void run() {
        System.out.println(" ");
        System.out.println("REFRESHING ALL HOLOGRAMS");

        StatsHandler statsHandler = Foxtrot.getInstance().getMapHandler().getStatsHandler();

        setLocations(statsHandler);
    }

    private static void setupHologram(String name, Location location, StatsHandler statsHandler) {

        NamedHologram hologram = new NamedHologram(location, name.toLowerCase());

        if (!NamedHologramManager.isExistingHologram(name.toLowerCase())) {

            System.out.println(name + " does not exist. Creating");
            createHologram(hologram, name, statsHandler);

            hologram.refreshAll();

        } else if (NamedHologramManager.isExistingHologram(name.toLowerCase())) {

            NamedHologramManager.getHologram(name.toLowerCase()).delete();
            hologram.delete();

            NamedHologram newHologram = new NamedHologram(location, name.toLowerCase());
            createHologram(newHologram, name, statsHandler);

            System.out.println(name + " hologram refreshed");
        }
    }

    private static void namedHolomanager(NamedHologram hologram) {
        NamedHologramManager.addHologram(hologram);
        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
    }

    private static void createHologram(NamedHologram hologram, String name, StatsHandler statsHandler) {

        hologram.clearLines();


        hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "Top " + name);
        hologram.appendTextLine("");


        if (name.equalsIgnoreCase(TOP_KILLS)) {
            int count = 1;
            for (int i = 0; i < 5; i++) {
                StatsEntry entry = statsHandler.get(StatsTopCommand.StatsObjective.KILLS, i + 1);

                if (entry == null || entry.getOwner() == null) {
                    hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + count + " " + ChatColor.GRAY + "" + ChatColor.ITALIC + "Loading...");
                    count++;
                } else {
                    try {
                        hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + count + " " + ChatColor.WHITE + Foxtrot.getInstance().getServer().getPlayer(entry.getOwner()).getName() + ChatColor.YELLOW + " [" + entry.getKills() + "]");
                        count++;
                    } catch (NullPointerException nullPointerException) {
                        nullPointerException.printStackTrace();
                    }
                }


            }
        } else if (name.equalsIgnoreCase(TOP_FACTIONS)) {

            hologram.appendTextLine(CC.translate("&7Below are a list of the top factions conquering"));
            hologram.appendTextLine(CC.translate("&7the server, all " + name + " receive a payout"));
            hologram.appendTextLine(CC.translate("&7at the end of the each map. Kill enemies and capture events!"));
            hologram.appendTextLine(CC.translate(""));

            LinkedHashMap<Team, Integer> sortedTeamPlayerCount = TeamTopCommand.getSortedTeams();

            int index = 0;

            for (Map.Entry<Team, Integer> teamEntry : sortedTeamPlayerCount.entrySet()) {

                if (teamEntry.getKey().getOwner() == null) {
                    continue;
                }

                index++;

                if (5 < index) {
                    break;
                }

                Team team = teamEntry.getKey();

                if (index == 1) {
                    hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + index + " " + ChatColor.WHITE + team.getName() + ChatColor.YELLOW + " [" + teamEntry.getValue().toString() + "]" + ChatColor.GREEN + " $50 Store Credit");
                } else if (index == 2) {
                    hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + index + " " + ChatColor.WHITE + team.getName() + ChatColor.YELLOW + " [" + teamEntry.getValue().toString() + "]" + ChatColor.GREEN + " $35 Store Credit");
                } else if (index == 3) {
                    hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + index + " " + ChatColor.WHITE + team.getName() + ChatColor.YELLOW + " [" + teamEntry.getValue().toString() + "]" + ChatColor.GREEN + " $25 Store Credit");
                } else if (index == 4) {
                    hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + index + " " + ChatColor.WHITE + team.getName() + ChatColor.YELLOW + " [" + teamEntry.getValue().toString() + "]");
                } else if (index == 5){
                    hologram.appendTextLine(ChatColor.GOLD + "" + ChatColor.BOLD + "#" + index + " " + ChatColor.WHITE + team.getName() + ChatColor.YELLOW + " [" + teamEntry.getValue().toString() + "]");
                }
            }
        }

        namedHolomanager(hologram);
    }

    private static void setLocations(StatsHandler statsHandler) {

        try {
//            statsHandler.setKillTopHologram(NamedHologramManager.getHologram(TOP_KILLS.toLowerCase()).getLocation());
            statsHandler.setTopFactionHologram(NamedHologramManager.getHologram(TOP_FACTIONS.toLowerCase()).getLocation());

            setupHologram(TOP_FACTIONS, statsHandler.getTopFactionHologram(), statsHandler);
//            setupHologram(TOP_KILLS, statsHandler.getKillTopHologram(), statsHandler);
        } catch (Exception e) {
            System.out.println("No holograms made. Please make them to stop this message from showing");
            e.printStackTrace();
        }
    }
}