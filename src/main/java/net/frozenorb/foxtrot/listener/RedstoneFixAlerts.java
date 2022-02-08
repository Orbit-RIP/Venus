package net.frozenorb.foxtrot.listener;

import lombok.Getter;
import net.frozenorb.foxtrot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedstoneFixAlerts implements Listener {

//    @EventHandler(priority = EventPriority.HIGH)
//    public void onLever(PlayerInteractEvent event) {
//        Block block = event.getClickedBlock();
//        Action action = event.getAction();
//        Player player = event.getPlayer();
//
//        Integer[] array = ThreadingManager.getTickCounter().getTicksPerSecond();
//        Integer tps = array[array.length - 1];
//        if (tps < 18) {
//            if (action == Action.RIGHT_CLICK_BLOCK) {
//                if (block.getType() == Material.LEVER || block.getType().name().endsWith("_BUTTON")) {
//                    if (tps < 17) {
//                        player.sendMessage(ChatColor.RED + "Server currently cannot use levers.");
//                        event.setCancelled(true);
//                    }
//                }
//            }
//        }
//
//    }

    @Getter
    private Map<UUID, Long> leverCooldown = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null
                || event.getClickedBlock().getType() != Material.LEVER) return;
        Player player = event.getPlayer();

        if (leverCooldown.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis()) {
            event.setCancelled(true);
            return;
        }

        leverCooldown.put(player.getUniqueId(), System.currentTimeMillis() + 500L);
    }

}