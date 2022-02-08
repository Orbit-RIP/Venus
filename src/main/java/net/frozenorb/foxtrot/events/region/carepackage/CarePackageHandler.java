package net.frozenorb.foxtrot.events.region.carepackage;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cc.fyre.proton.Proton;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import cc.fyre.proton.Proton;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.redis.RedisCommand;
import net.md_5.bungee.api.ChatColor;
import redis.clients.jedis.Jedis;

public class CarePackageHandler implements Listener {

    private Location lastCarePackage;
    private World world;
    private static List<ItemStack> loot;
    
    public CarePackageHandler() {
        if (true) {
            return;
        }
        
        Proton.getInstance().getCommandHandler().registerClass(this.getClass());
        this.world = Bukkit.getWorlds().get(0);
        Bukkit.getScheduler().runTaskTimer(Foxtrot.getInstance(), this::spawnCarePackage, 60 * 20, Bukkit.getServerName().equalsIgnoreCase("Kitmap") ? 15 * 60 * 20 : 60 * 60 * 20);

        try {
            loot = Lists.newArrayList(Proton.GSON.fromJson(Proton.getInstance().getIRedisCommand().runBackboneRedisCommand(new RedisCommand<String>() {

                @Override
                public String execute(Jedis redis) {
                    String lookupString = Bukkit.getServerName() + ":" + "carePackageLoot";
                    Bukkit.getLogger().info("Lookup string: " + lookupString);
                    return redis.get(lookupString);
                }

            }), ItemStack[].class));
        } catch (Exception e) {
            loot = Lists.newArrayList();
            Bukkit.getLogger().info("No care package loot is set up.");
        }
    }

    @Command(names = "carepackage saveloot", permission = "op", async = true)
    public static void save(Player sender) {
        Proton.getInstance().getIRedisCommand().runBackboneRedisCommand((redis) -> {
            String lookupString = Bukkit.getServerName() + ":" + "carePackageLoot";
            Bukkit.getLogger().info("Lookup string: " + lookupString);
            redis.set(lookupString, Proton.PLAIN_GSON.toJson(Arrays.stream(sender.getInventory().getContents()).filter(item -> item != null && item.getType() != Material.AIR).collect(Collectors.toList())));
            return null;
        });
        
        loot = Arrays.stream(sender.getInventory().getContents()).filter(item -> item != null && item.getType() != Material.AIR).collect(Collectors.toList());
        sender.sendMessage(ChatColor.GREEN + "Loot updated.");
    }

    private void spawnCarePackage() {
        int x = 0, z = 0;

        while (Math.abs(x) <= 100) x = Proton.RANDOM.nextInt(1000) - 500;
        while (Math.abs(z) <= 100) z = Proton.RANDOM.nextInt(1000) - 500;

        while (LandBoard.getInstance().getTeam(new Location(world, x, 100, z)) != null) {
            x = 0; z = 0;

            while (Math.abs(x) <= 100) x = Proton.RANDOM.nextInt(1000) - 500;
            while (Math.abs(z) <= 100) z = Proton.RANDOM.nextInt(1000) - 500;
        }

        int y = world.getHighestBlockYAt(x, z);
        Block block = world.getBlockAt(x, y, z);

        if (block == null) {
            // couldn't find location, lets try again
            spawnCarePackage();
            return;
        }
        
        Block realBlock = block.getRelative(BlockFace.UP);
        realBlock.setType(Material.ENDER_CHEST);
        realBlock.setMetadata("CarePackage", new FixedMetadataValue(Foxtrot.getInstance(), new Object()));
        lastCarePackage = realBlock.getLocation();

        Bukkit.getLogger().info("Spawning crate at " + realBlock.getLocation());
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6[Crate] &eA &dCrate &ehas spawned at &d" + x + " " + z + "&e."));
        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), this::removeCarePackage, 10 * 20 * 60);
    }

    private void removeCarePackage() {
        if (lastCarePackage != null && lastCarePackage.getBlock() != null && lastCarePackage.getBlock().getType() == Material.ENDER_CHEST) {
            lastCarePackage.getBlock().setType(Material.AIR);
            lastCarePackage.getBlock().removeMetadata("CarePackage", Foxtrot.getInstance());
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6[Crate] &eThe &dCrate &eat &d" + lastCarePackage.getBlockX() + " " + lastCarePackage.getBlockZ() + " &ehas despawned."));
        }
    }

    /*@EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getLocation().getBlockY() >= 45) return;
        if (LandBoard.getInstance().getTeam(event.getBlock().getLocation()) == null) {
            if (Proton.RANDOM.nextInt(149) == 0) {
                event.setCancelled(true);
                event.getBlock().setType(Material.CHEST);

                Chest chest = (Chest) event.getBlock().getState();

                event.getPlayer().playSound(chest.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
                event.getPlayer().sendMessage(ChatColor.YELLOW + "Woah! You found a chest.");

                chest.getBlockInventory().setItem(Proton.RANDOM.nextInt(chest.getBlockInventory().getSize()), loot.get(Proton.RANDOM.nextInt(loot.size())));
                chest.getBlockInventory().setItem(Proton.RANDOM.nextInt(chest.getBlockInventory().getSize()), loot.get(Proton.RANDOM.nextInt(loot.size())));
                chest.getBlockInventory().setItem(Proton.RANDOM.nextInt(chest.getBlockInventory().getSize()), loot.get(Proton.RANDOM.nextInt(loot.size())));
            }
        }
    }*/

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || !clickedBlock.hasMetadata("CarePackage")) {
            return;
        }

        Set<ItemStack> toGive = Sets.newHashSet();
        while (toGive.size() < 4) {
            toGive.add(loot.get(Proton.RANDOM.nextInt(loot.size())));
        }

        for (ItemStack item : toGive) {
            world.dropItemNaturally(clickedBlock.getLocation(), item);
        }

        clickedBlock.removeMetadata("CarePackage", Foxtrot.getInstance());
        clickedBlock.setType(Material.AIR);
        event.setCancelled(true);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6[Crate] &eA &dCrate &eat &d" + clickedBlock.getLocation().getBlockX() + " " + clickedBlock.getLocation().getBlockZ() + " &ehas been opened."));
    }

    @EventHandler
    public void onPluginShutdown(PluginDisableEvent event) {
        if (event.getPlugin() == Foxtrot.getInstance()) {
            removeCarePackage();
        }
    }
}
