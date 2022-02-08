package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class CrystalRodListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (!event.hasBlock()) return;
        Player player = event.getPlayer();
        if (event.getClickedBlock().getType() == Material.FENCE_GATE){
            if (Foxtrot.getInstance().crystalrod.containsKey(player.getUniqueId()) && Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) != null) {
                long remaining = Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis();
                if (remaining > 0L) {
                    player.sendMessage(ChatColor.RED + "You cannot open fence gate for another " + DurationFormatUtils.formatDurationWords(Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                    event.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onInteractLever(PlayerInteractEvent event){
        if (!event.hasBlock()) return;
        Player player = event.getPlayer();
        if (event.getClickedBlock().getType() == Material.LEVER){
            if (Foxtrot.getInstance().crystalrod.containsKey(player.getUniqueId()) && Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) != null) {
                long remaining = Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis();
                if (remaining > 0L) {
                    player.sendMessage(ChatColor.RED + "You cannot click a lever for another " + DurationFormatUtils.formatDurationWords(Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                    event.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onInteractButton(PlayerInteractEvent event){
        if (!event.hasBlock()) return;
        Player player = event.getPlayer();
        if (event.getClickedBlock().getType() == Material.STONE_BUTTON || event.getClickedBlock().getType() == Material.WOOD_BUTTON){
            if (Foxtrot.getInstance().crystalrod.containsKey(player.getUniqueId()) && Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) != null) {
                long remaining = Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis();
                if (remaining > 0L) {
                    player.sendMessage(ChatColor.RED + "You cannot click a lever for another " + DurationFormatUtils.formatDurationWords(Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                    event.setCancelled(true);
                }
            }
        }
    }


    /**
     *
     * I do not know why this was added
     * Should be looked at but I don't think it needs to be in here.
     *
     */

//    @EventHandler
//    public void onInteractpresure(PlayerMoveEvent event){
//        Player player = event.getPlayer();
//        if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.STONE_PLATE || event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WOOD_PLATE){
//            if (Foxtrot.getInstance().crystalrod.containsKey(player.getUniqueId()) && Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) != null) {
//                long remaining = Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis();
//                if (remaining > 0L) {
//                    player.sendMessage(ChatColor.RED + "You cannot use Presure Plate for another " + DurationFormatUtils.formatDurationWords(Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
//                    event.setCancelled(true);
//                }
//            }
//        }
//    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if (Foxtrot.getInstance().crystalrod.containsKey(player.getUniqueId()) && Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) != null) {
            long remaining = Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis();
            if (remaining > 0L) {
                player.sendMessage(ChatColor.RED + "You cannot place blocks for another " + DurationFormatUtils.formatDurationWords(Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if (Foxtrot.getInstance().crystalrod.containsKey(player.getUniqueId()) && Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) != null) {
            long remaining = Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis();
            if (remaining > 0L) {
                player.sendMessage(ChatColor.RED + "You cannot break blocks for another " + DurationFormatUtils.formatDurationWords(Foxtrot.getInstance().crystalrod.get(player.getUniqueId()) - System.currentTimeMillis(), true, true) + ".");
                event.setCancelled(true);
            }
        }
    }
}
