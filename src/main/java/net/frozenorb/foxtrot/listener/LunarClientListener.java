//package net.frozenorb.foxtrot.listener;
//
//import com.lunarclient.bukkitapi.LunarClientAPI;
//import com.lunarclient.bukkitapi.nethandler.client.LCPacketCooldown;
//import com.lunarclient.bukkitapi.object.LCWaypoint;
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.events.Event;
//import net.frozenorb.foxtrot.events.events.EventActivatedEvent;
//import net.frozenorb.foxtrot.events.events.EventDeactivatedEvent;
//import net.frozenorb.foxtrot.events.koth.KOTH;
//import net.frozenorb.foxtrot.team.Team;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.entity.EntityDamageByEntityEvent;
//import org.bukkit.event.player.PlayerJoinEvent;
//
///**
// * @author scynse (scynse@fusiongames.dev)
// * 8/4/2021 / 8:44 AM
// * pom.xml / net.frozenorb.foxtrot.listener
// */
//public class LunarClientListener implements Listener {
//
//
//    LunarClientAPI api = LunarClientAPI.getInstance();
//
//    @EventHandler
//    public void on(PlayerJoinEvent event) {
//        Player player = event.getPlayer();
//        updateWaypoints(player);
//
//    }
//
//
//    @EventHandler
//    public void on(EventActivatedEvent event) {
//        for (Event events : Foxtrot.getInstance().getEventHandler().getEvents()) {
//
//            if (events.getName().contains("Conquest-")) {
//                return;
//            }
//
//            if (!event.getEvent().isActive()) {
//                return;
//            }
//            Foxtrot.getInstance().setEventWaypoint(new LCWaypoint(event.getEvent().getName() + (events instanceof KOTH ? " KOTH" : " Conquest"),
//                    ((KOTH) events).getCapLocation().toLocation(Bukkit.getWorld("world")), 16776960, true, true));
//
//            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
//                api.sendWaypoint(player, Foxtrot.getInstance().getEventWaypoint());
//            });
//        }
//    }
//
//    @EventHandler
//    public void on(EventDeactivatedEvent event) {
//        Event events = event.getEvent();
//
//        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
//            if (Foxtrot.getInstance().getEventWaypoint() != null) {
//                api.removeWaypoint(player, Foxtrot.getInstance().getEventWaypoint());
//            }
//        });
//    }
//
//    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
//    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
//        if (!(event.getDamager() instanceof Player)) return;
//
//        Player attacker = (Player) event.getDamager();
//        Entity entity;
//        if (attacker != null && (entity = event.getEntity()) instanceof Player) {
//            Player attacked = (Player) entity;
//            LunarClientAPI.getInstance().sendPacket(attacker, new LCPacketCooldown("Combat", 30000, 276));
//            LunarClientAPI.getInstance().sendPacket(attacked, new LCPacketCooldown("Combat", 30000, 276));
//
//        }
//    }
//    private void updateWaypoints(Player player) {
//
//        /*
//
//        api.sendWaypoint(player, new LCWaypoint(name,
//                x,
//                y,
//                z,
//                world,
//                color, #Colors are java color values refer here for values: https://pastebin.com/sVhBaacF
//                forced,
//                visible));
//
//
//         */
////
////        api.sendWaypoint(player, new LCWaypoint("Spawn",
////                0,
////                70,
////                0,
////                "world",
////                5635840,
////                true,
////                true));
//
//
//
//        for (Event events : Foxtrot.getInstance().getEventHandler().getEvents()) {
//            if (!events.isActive()) {
//                return;
//            }
//
//            if (events instanceof KOTH) {
//                    if (events.getName().contains("Conquest-")) {
//                        return;
//                    }
//                    Location l = ((KOTH) events).getCapLocation().toLocation(player.getWorld());
//                    api.sendWaypoint(player, new LCWaypoint(events.getName() + " KOTH",
//                            l.getBlockX(),
//                            l.getBlockY(), l.getBlockZ(),
//                            l.getWorld().getUID().toString(),
//                            16776960,
//                            true,
//                            true));
//                } else if (events.getName().contains("Conquest")) {
//
//                    Location l = ((KOTH) events).getCapLocation().toLocation(player.getWorld());
//
//
//                    api.sendWaypoint(player, new LCWaypoint(events.getName() + " Conquest",
//                            l.getBlockX(),
//                            l.getBlockY(), l.getBlockZ(),
//                            l.getWorld().getUID().toString(),
//                            16776960,
//                            true,
//                            true));
//
//                }
//            }
//        }
//
////        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);
////
////        if (team == null) {
////            return;
////        }
////
////        if (team.getFocusedTeam() != null) {
////            Location loc = team.getFocusedTeam().getHQ();
////            api.sendWaypoint(player, new LCWaypoint(team.getFocusedTeam().getName() + "'s HQ",
////                    loc.getBlockX(),
////                    loc.getBlockY(),
////                    loc.getBlockZ(),
////                    loc.getWorld().getUID().toString(),
////                    13369344,
////                    true,
////                    true));
////
////        }
////
////        if (team.getHQ() != null) {
////            Location loc = team.getHQ();
////            api.sendWaypoint(player, new LCWaypoint("HQ",
////                    loc.getBlockX(),
////                    loc.getBlockY(),
////                    loc.getBlockZ(),
////                    loc.getWorld().getUID().toString(),
////                    -16776961,
////                    true,
////                    true));
////        }
////
////        if (team.getRally() != null) {
////            Location loc = team.getRally();
////            api.sendWaypoint(player, new LCWaypoint("Rally Point",
////                    loc.getBlockX(),
////                    loc.getBlockY(), loc.getBlockZ(),
////                    loc.getWorld().getUID().toString(),
////                    15073510,
////                    true,
////                    true));
////        }
//
//
//    }
//
//
