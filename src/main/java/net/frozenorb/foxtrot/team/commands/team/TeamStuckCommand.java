package net.frozenorb.foxtrot.team.commands.team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import cc.fyre.proton.command.Command;

public class TeamStuckCommand implements Listener {

    private static final double MAX_DISTANCE = 5;

    private static final Set<Integer> warn = new HashSet<>();

    static {
        warn.add(60);
        warn.add(30);
        warn.add(10);
        warn.add(5);
        warn.add(4);
        warn.add(3);
        warn.add(2);
        warn.add(1);

        Foxtrot.getInstance().getServer().getPluginManager().registerEvents(new TeamStuckCommand(), Foxtrot.getInstance());
    }

    @Getter private static Map<String, Long> warping = new ConcurrentHashMap<>();
    private static List<String> damaged = Lists.newArrayList();

    @Command(names={ "team stuck", "t stuck", "f stuck", "faction stuck", "fac stuck", "stuck", "team unstuck", "t unstuck", "f unstuck", "faction unstuck", "fac unstuck", "unstuck"}, permission="")
    public static void teamStuck(final Player sender) {
        if (warping.containsKey(sender.getName())) {
            sender.sendMessage(ChatColor.RED +"You are already being warped!");
            return;
        }

        if (sender.getWorld().getEnvironment() != World.Environment.NORMAL) {
            sender.sendMessage(ChatColor.RED +"You can only use this command from the overworld.");
            return;
        }

        int seconds = sender.isOp() && sender.getGameMode() == GameMode.CREATIVE ? 5 : 45;
        warping.put(sender.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));

        new BukkitRunnable() {

            private int seconds = sender.isOp() && sender.getGameMode() == GameMode.CREATIVE ? 5 : 45;

            private Location loc = sender.getLocation();

            private int xStart = (int) loc.getX();
            private int yStart = (int) loc.getY();
            private int zStart = (int) loc.getZ();

            private Location nearest;

            @Override
            public void run() {
                //Bukkit.broadcastMessage("line 81");

                if (damaged.contains(sender.getName())) {
                    sender.sendMessage(ChatColor.RED + "You took damage, teleportation cancelled!");
                    damaged.remove(sender.getName());
                    warping.remove(sender.getName());
                    cancel();
                    return;
                }

                // Begin asynchronously searching for an available location prior to the actual teleport
                if (seconds == 5) {
                     //Bukkit.broadcastMessage("line 99");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            //Bukkit.broadcastMessage("Started searching for loction line 100");
                            nearest = nearestSafeLocation(sender.getLocation());
                        }

                    }.runTask(Foxtrot.getInstance());
                }

                Location loc = sender.getLocation();

                if (seconds <= 0) {
                    if (nearest == null) {
                        //Bukkit.broadcastMessage("nearest == null");
                        kick(sender);
                    } else {
                        Foxtrot.getInstance().getLogger().info("Moved " + sender.getName() + " " + loc.distance(nearest) + " blocks from " + toStr(loc) + " to " + toStr(nearest));

                        sender.teleport(nearest);
                        sender.sendMessage(ChatColor.GOLD + "Teleported you to the nearest safe area!");
                    }

                    //Bukkit.broadcastMessage("Line 122");
                    warping.remove(sender.getName());
                    cancel();
                    return;
                }

                // More than 5 blocks away
                if ((loc.getX() >= xStart + MAX_DISTANCE || loc.getX() <= xStart - MAX_DISTANCE) || (loc.getY() >= yStart + MAX_DISTANCE || loc.getY() <= yStart - MAX_DISTANCE) || (loc.getZ() >= zStart + MAX_DISTANCE || loc.getZ() <= zStart - MAX_DISTANCE)) {
                    sender.sendMessage(ChatColor.RED + "You moved more than " + MAX_DISTANCE + " blocks, teleport cancelled!");
                    warping.remove(sender.getName());
                    cancel();
                    return;
                }

                /* Not necessary if we put the stuck timer in sidebar
                if (warn.contains(seconds)) {
                    sender.sendMessage(ChatColor.YELLOW + "You will be teleported in " + ChatColor.RED.toString() + ChatColor.BOLD + TimeUtils.formatIntoMMSS(seconds) + ChatColor.YELLOW + "!");
                }
                */

                seconds--;
                //Bukkit.broadcastMessage(String.valueOf(seconds));
            }

        }.runTaskTimer(Foxtrot.getInstance(), 0L, 20L);
    }

    private static String toStr(Location loc) {
        return "{x=" + loc.getBlockX() + ", y=" + loc.getBlockY() + ", z=" + loc.getBlockZ() + "}";
    }

    public static Location nearestSafeLocation(Location origin) {
        LandBoard landBoard = LandBoard.getInstance();

        if (landBoard.getClaim(origin) == null) {
            //Bukkit.broadcastMessage("155");
            return (getActualHighestBlock(origin.getBlock()).getLocation().add(0 , 1, 0));
        }

        // Start iterating outward on both positive and negative X & Z.
        for (int xPos = 2, xNeg = -2; xPos < 250; xPos += 2, xNeg -= 2) {
            for (int zPos = 2, zNeg = -2; zPos < 250; zPos += 2, zNeg -= 2) {
                Location atPos = origin.clone().add(xPos, 0, zPos);

                // Try to find a unclaimed location with no claims adjacent
                if (landBoard.getClaim(atPos) == null && !isAdjacentClaimed(atPos)) {
                    //Bukkit.broadcastMessage("Line 166 found unclaimed location with no claims");
                    return (getActualHighestBlock(atPos.getBlock()).getLocation().add(0, 1, 0));
                }

                Location atNeg = origin.clone().add(xNeg, 0, zNeg);

                // Try again to find a unclaimed location with no claims adjacent
                if (landBoard.getClaim(atNeg) == null && !isAdjacentClaimed(atNeg)) {
                    //Bukkit.broadcastMessage("Line 174 found unclaimed location with no claims");
                    return (getActualHighestBlock(atNeg.getBlock()).getLocation().add(0, 1, 0));
                }
            }
        }

        return (null);
    }

    @EventHandler(priority= EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (warping.containsKey(player.getName())) {
                damaged.add(player.getName());
            }
        }
    }

    private static Block getActualHighestBlock(Block block) {
        block = block.getWorld().getHighestBlockAt(block.getLocation());

        while (block.getType() == Material.AIR && block.getY() > 0) {
            block = block.getRelative(BlockFace.DOWN);
        }

        return (block);
    }

    private static void kick(Player player){
        player.setMetadata("loggedout", new FixedMetadataValue(Foxtrot.getInstance(), true));
        player.kickPlayer(ChatColor.RED + "We couldn't find a safe location, so we safely logged you out for now. Contact a staff member before logging back on! " + ChatColor.BLUE + "TeamSpeak: ts." + Foxtrot.getInstance().getServerHandler().getNetworkWebsite());
    }

    /**
     * @Parameter base center block
     * @return list of all adjacent locations
     */
    private static List<Location> getAdjacent(Location base) {
        List<Location> adjacent = new ArrayList<>();

        // Add all relevant locations surrounding the base block
        for(BlockFace face : BlockFace.values()) {
            if(face != BlockFace.DOWN && face != BlockFace.UP) {
                adjacent.add(base.getBlock().getRelative(face).getLocation());
            }
        }

        return adjacent;
    }

    /**
     *
     * @Parameter location location to check for
     * @return if any of it's blockfaces are claimed
     */
    private static boolean isAdjacentClaimed(Location location) {
        for (Location adjacent : getAdjacent(location)) {
            if (LandBoard.getInstance().getClaim(adjacent) != null) {
                return true; // we found a claim on an adjacent block!
            }
        }

        return false;
    }
}