package net.frozenorb.foxtrot.listener;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.frozenorb.foxtrot.commands.SOTWCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.Proton;

public class EndListener implements Listener {

    public static boolean endActive = true;
    @Getter @Setter private static Location endReturn; // end -> overworld teleport location

    private Map<String, Long> msgCooldown = new HashMap<>();

    // Display a message and give the killer the egg (when the dragon is killed)
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            Team team = Foxtrot.getInstance().getTeamHandler().getTeam(event.getEntity().getKiller());
            String teamName = ChatColor.GOLD + "[" + ChatColor.YELLOW + "-" + ChatColor.GOLD + "]";

            if (team != null) {
                teamName = ChatColor.GOLD + "[" + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "]";
            }

            for (int i = 0; i < 6; i++) {
                Foxtrot.getInstance().getServer().broadcastMessage("");
            }

            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████" + ChatColor.GOLD + " [Enderdragon]");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████" + ChatColor.YELLOW + " killed by");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "█" + ChatColor.DARK_PURPLE + "█" + ChatColor.LIGHT_PURPLE + "█" + ChatColor.BLACK + "██" + ChatColor.LIGHT_PURPLE + "█" + ChatColor.DARK_PURPLE + "█" + ChatColor.LIGHT_PURPLE + "█" + " " + teamName);
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████" + " " + event.getEntity().getKiller().getDisplayName());
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");

            ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG);
            ItemMeta itemMeta = dragonEgg.getItemMeta();
            DateFormat sdf = new SimpleDateFormat("M/d HH:mm:ss");

            itemMeta.setLore(Arrays.asList
                    (ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Enderdragon " + ChatColor.WHITE + "slain by " + ChatColor.YELLOW + event.getEntity().getKiller().getName(),
                            ChatColor.WHITE + sdf.format(new Date()).replace(" AM", "").replace(" PM", "")));

            dragonEgg.setItemMeta(itemMeta);

            // Should we drop the item or directly add it to their inventory?

            event.getEntity().getKiller().getInventory().addItem(dragonEgg);

            if (!event.getEntity().getKiller().getInventory().contains(Material.DRAGON_EGG)) {
                event.getDrops().add(dragonEgg);
            }
        }
    }

    // Prevent items dropped through from creating the obsidian platform.
    @EventHandler
    public void onEntityCreatePortal(EntityCreatePortalEvent event) {
        if (event.getEntity() instanceof Item && event.getPortalType() == PortalType.ENDER) {
            event.getBlocks().clear();
        }
    }

    // Display the enderdragon's health on the bar at the top of the screen (with a percentage)
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof EnderDragon && event.getEntity().getWorld().getEnvironment() == World.Environment.THE_END) {
            ((EnderDragon) event.getEntity()).setCustomName("Ender Dragon " + ChatColor.YELLOW.toString() + ChatColor.BOLD + Math.round((((EnderDragon) event.getEntity()).getHealth() / ((EnderDragon) event.getEntity()).getMaxHealth()) * 100) + "% Health");
        }
    }

    // Disallow block breaking/placing
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END) {
            if (event.getPlayer().isOp() && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                return;
            }

            event.setCancelled(true);
        }
    }

    // Disallow block breaking/placing
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END) {
            if (event.getPlayer().isOp() && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                return;
            }

            event.setCancelled(true);
        }
    }


    // Disallow bucket usage
    @EventHandler
    public void onPlayerBukkitEmpty(PlayerBucketEmptyEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END) {
            if (event.getPlayer().isOp() && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                return;
            }

            event.setCancelled(true);
        }
    }

    // Disallow bucket usage
    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END) {
            if (event.getPlayer().isOp() && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                return;
            }

            event.setCancelled(true);
        }
    }

    // Cancel the exit portal being spawned when the dragon is killed.
    @EventHandler
    public void onCreatePortal(EntityCreatePortalEvent event) {
        if (event.getEntity().getType() == EntityType.ENDER_DRAGON) {
            event.setCancelled(true);
        }
    }

    // Whenever a player enters/leaves the end
    @EventHandler(priority= EventPriority.LOWEST) // Lowest gets called first, so we can not have other plugins deal w/ null event.getTo()s
    public void onPortal(PlayerPortalEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            return;
        }

        Player player = event.getPlayer();

        // Special case leaving the miner world, as the event.getTo will be/contain nulls.
        if ((event.getTo() == null || event.getTo().getWorld() == null) && event.getFrom().getWorld().getName().equalsIgnoreCase("world_miner")) {
            event.setTo(Foxtrot.getInstance().getServer().getWorlds().get(0).getSpawnLocation());
        }

        if (event.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) { // Leaving the End
            // Don't let players leave the end while the dragon is still alive.
            if (event.getFrom().getWorld().getEntitiesByClass(EnderDragon.class).size() != 0) {
                event.setCancelled(true);

                if (!(msgCooldown.containsKey(player.getName())) || msgCooldown.get(player.getName()) < System.currentTimeMillis()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot leave the end before the dragon is killed.");
                    msgCooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
                }
            }
            event.setCancelled(true);

            loadEndReturn();
            player.teleport(endReturn);

        } else if (event.getTo().getWorld().getEnvironment() == World.Environment.THE_END) { // Entering the end
            //Don't allow factions of to large size to enter the mini end.
//            Team team = LandBoard.getInstance().getTeam(event.getFrom());
//            if (team != null && team.getKitName().equalsIgnoreCase(MiniEndConfiguration.getTeamName())) {
//                Team playerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(event.getPlayer());
//                if (playerTeam == null || playerTeam.getSize() <= MiniEndConfiguration.getMaximumTeamSize()) {
//                    event.setTo(MiniEndConfiguration.getSpawnLocation());
//                } else {
//                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot enter this end portal, it is for factions under the size of " + ChatColor.YELLOW + MiniEndConfiguration.getMaximumTeamSize() + ChatColor.RED + " players.");
//                    event.setCancelled(true);
//                    return;
//                }
//
//            }

            // Don't let players enter the end while they have their PvP timer (or haven't activated it)
            if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
                event.setCancelled(true);

                if (!(msgCooldown.containsKey(player.getName())) || msgCooldown.get(player.getName()) < System.currentTimeMillis()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot enter the end while you have PvP protection.");
                    msgCooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
                }
            }

            // Don't let players enter the end while they're spawn tagged
            if (SpawnTagHandler.isTagged(event.getPlayer())) {
                event.setCancelled(true);

                if (!(msgCooldown.containsKey(player.getName())) || msgCooldown.get(player.getName()) < System.currentTimeMillis()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot enter the end while you are spawn tagged.");
                    msgCooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
                }
            }

            // Don't let players enter the end while it's not activated (and they're not in gamemode)
            if (!endActive && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);

                if (!(msgCooldown.containsKey(player.getName())) || msgCooldown.get(player.getName()) < System.currentTimeMillis()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "The End is currently disabled.");
                    msgCooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
                }
            }

            event.useTravelAgent(false);
            event.setTo(event.getTo().getWorld().getSpawnLocation().add(0.5, 0, 0.5));
            // Remove all potion effects with less than 9s remaining
            for (PotionEffect potionEffect : event.getPlayer().getActivePotionEffects()) {
                if (potionEffect.getDuration() < 20 * 9) {
                    event.getPlayer().removePotionEffect(potionEffect.getType());
                }
            }
        }
    }

    // Always prevent enderdragons breaking blocks (?)
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.blockList().clear();
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        Player player = event.getPlayer();

        if (from.getWorld().getEnvironment() != Environment.THE_END || to.getWorld().getEnvironment() != Environment.THE_END) return;

        if (event.getPlayer().getLocation().getBlock().getType() == Material.WATER || event.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
            double pitch = 21.294214;
            double yaw = 28.865494;
            loadEndReturn();
            player.teleport(new Location(Bukkit.getWorld("world") , endReturn.getX(),endReturn.getY(), endReturn.getZ()));
        }

        if (!SOTWCommand.isSOTWTimer() || SOTWCommand.hasSOTWEnabled(event.getPlayer().getUniqueId())) return;

        if (to.getBlockY() < -128) {
            event.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
        }
    }

    // Always deny enderdragons using portals.
    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    public static void saveEndReturn() {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Foxtrot.getInstance(), () -> Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {
            redis.set("endReturn", Proton.PLAIN_GSON.toJson(endReturn));
            return null;
        }));
    }

    public static void loadEndReturn() {
        if (endReturn != null) {
            return;
        }

        Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {
            if (redis.exists("endReturn")) {
                endReturn = Proton.PLAIN_GSON.fromJson(redis.get("endReturn"), Location.class);
            } else {
                endReturn = new Location(Bukkit.getWorlds().get(0), 0.6, 64, 346.5);
            }
            return null;
        });
    }

}