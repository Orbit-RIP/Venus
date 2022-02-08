package net.frozenorb.foxtrot.listener;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketCooldown;
import com.lunarclient.bukkitapi.nethandler.shared.LCPacketWaypointAdd;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.koth.KOTH;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.UUID;

public class LunarClientListenerold implements Listener {


    LunarClientAPI api = LunarClientAPI.getInstance();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();
        Entity entity;
        if (attacker != null && (entity = event.getEntity()) instanceof Player) {
            Player attacked = (Player) entity;
            LunarClientAPI.getInstance().sendPacket(attacker, new LCPacketCooldown("Combat", 3000, 30));
            LunarClientAPI.getInstance().sendPacket(attacked, new LCPacketCooldown("Combat", 3000, 30));

        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase("world_the_end")) return;

        new BukkitRunnable() {
            public void run() {
                UUID worlduid = Bukkit.getWorld("world").getUID();
                UUID netheruid = Bukkit.getWorld("world_nether").getUID();
                api.sendWaypoint(player, new LCWaypoint("Spawn",
                        0,
                        82, 0,
                        player.getWorld().getUID().toString(),
                        5635840,
                        true,
                        true));//                new LCPacketWaypointAdd("Glowstone Mountain", netheruid, Color.ORANGE, 150, 43, 150, true, true).sendTo(player);
//                new LCPacketWaypointAdd("End Exit", worlduid, Color.PINK, 0, 70, 300, true, true).sendTo(player);


                for (Event events : Foxtrot.getInstance().getEventHandler().getEvents()) {
                    if (events.isActive()) {
                    if (events instanceof KOTH) {
                        Location l = ((KOTH) events).getCapLocation().toLocation(player.getWorld());
                        api.sendWaypoint(player, new LCWaypoint(events.getName() + " KOTH",
                                l.getBlockX(),
                                l.getBlockY(), l.getBlockZ(),
                                l.getWorld().getUID().toString(),
                                16776960,
                                true,
                                true));
                    } else if (events.getName().equalsIgnoreCase("Conquest")) {

                        Location l = ((KOTH) events).getCapLocation().toLocation(player.getWorld());


                        api.sendWaypoint(player, new LCWaypoint(events.getName() + " Conquest",
                                l.getBlockX(),
                                l.getBlockY(), l.getBlockZ(),
                                l.getWorld().getUID().toString(),
                                16776960,
                                true,
                                true));

                    }
                }
                }
                if (Foxtrot.getInstance().getTeamHandler().getTeam(player) != null && Foxtrot.getInstance().getTeamHandler().getTeam(player).getHQ() != null) {
                    Location loc = Foxtrot.getInstance().getTeamHandler().getTeam(player).getHQ();
                    api.sendWaypoint(player, new LCWaypoint("Home",
                            loc.getBlockX(),
                            loc.getBlockY(),
                            loc.getBlockZ(),
                            loc.getWorld().getUID().toString(),
                            -16776961,
                            true,
                            true));                }
            }
        }.runTaskLaterAsynchronously(Foxtrot.getInstance(), 40);
    }

    @EventHandler
    public void worldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID worlduid = player.getWorld().getUID();
        UUID fromuid = event.getFrom().getUID();
        new BukkitRunnable() {
            public void run() {
                if (event.getFrom() != player.getWorld() && player.getWorld().getName().equalsIgnoreCase("world_the_end")) {
                    api.removeWaypoint(player, new LCWaypoint("Spawn",
                            0,
                            82, 0,
                            player.getWorld().getUID().toString(),
                            5635840,
                            true,
                            true));


                }
                if (event.getFrom() != player.getWorld() && player.getWorld().getName().equalsIgnoreCase("world_nether")) {
                    //new LCPacketWaypointRemove("End Exit", fromuid).sendTo(player);
                  //  new LCPacketWaypointRemove("Spawn", fromuid).sendTo(player);
                 //   new LCPacketWaypointAdd("Glowstone Mountain", worlduid, Color.ORANGE, 150, 43, 150, true, true).sendTo(player);
                  //  new LCPacketWaypointAdd("Spawn", worlduid, Color.GREEN, 0, 42, 0, true, true).sendTo(player);
                }
                if (event.getFrom() != player.getWorld() && player.getWorld().getName().equalsIgnoreCase("world")) {
                    api.sendWaypoint(player, new LCWaypoint("Spawn",
                            0,
                            82, 0,
                            player.getWorld().getUID().toString(),
                            5635840,
                            true,
                            true));//                    new LCPacketWaypointAdd("End Exit", worlduid, Color.PINK, 0, 70, 300, true, true).sendTo(player);


                    for (Event events : Foxtrot.getInstance().getEventHandler().getEvents()) {
                        if (events.isActive()) {
                            if (events instanceof KOTH) {
                                Location l = ((KOTH) events).getCapLocation().toLocation(player.getWorld());
                                api.sendWaypoint(player, new LCWaypoint(events.getName() + " KOTH",
                                        l.getBlockX(),
                                        l.getBlockY(), l.getBlockZ(),
                                        l.getWorld().getUID().toString(),
                                        16776960,
                                        true,
                                        true));
                            } else if (events.getName().equalsIgnoreCase("Conquest")) {

                                Location l = ((KOTH) events).getCapLocation().toLocation(player.getWorld());


                                api.sendWaypoint(player, new LCWaypoint(events.getName() + " Conquest",
                                        l.getBlockX(),
                                        l.getBlockY(), l.getBlockZ(),
                                        l.getWorld().getUID().toString(),
                                        16776960,
                                        true,
                                        true));

                            }
                        }
                    }
                    if (Foxtrot.getInstance().getTeamHandler().getTeam(player) != null && Foxtrot.getInstance().getTeamHandler().getTeam(player).getHQ() != null) {
                        Location teamHQ = Foxtrot.getInstance().getTeamHandler().getTeam(player).getHQ();
//                        new LCPacketWaypointAdd("Home", worlduid, Color.DARK_GRAY, teamHQ.getBlockX(), teamHQ.getBlockY(), teamHQ.getBlockZ(), true, true).sendTo(player);
                    }
                }
            }
        }.runTaskLaterAsynchronously(Foxtrot.getInstance(), 40);
    }

}
