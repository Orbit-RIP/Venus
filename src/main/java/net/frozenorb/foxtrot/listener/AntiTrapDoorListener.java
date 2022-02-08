package net.frozenorb.foxtrot.listener;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AntiTrapDoorListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (player.getLocation().getY() < 60 && (block.getType() == Material.WOODEN_DOOR || block.getType() == Material.WOOD_DOOR)) {
            player.sendMessage(ChatColor.RED + "Doors cannot be placed under Y60.");
            event.setCancelled(true);
        }
    }

}
