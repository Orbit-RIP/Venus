package net.frozenorb.foxtrot.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PearlDebugListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPearl(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Player player = event.getPlayer();

            Location to = event.getTo();
            Block block = to.getBlock();
            Material type = block.getType();
            Block above = block.getRelative(BlockFace.UP);
            Material aboveType = above.getType();
            Material westType = block.getRelative(BlockFace.WEST).getType();
            Material eastType = block.getRelative(BlockFace.EAST).getType();
            Material belowType = block.getRelative(BlockFace.DOWN).getType();
            Material southType = block.getRelative(BlockFace.SOUTH).getType();
            Material northType = block.getRelative(BlockFace.NORTH).getType();
            System.out.println("------------------");
            System.out.println("Denied Pearl Alert for " + player.getName());
            System.out.println("Solid Block Type " + type);
            System.out.println("Above - " + aboveType + ".");
            System.out.println("East - " + eastType + ".");
            System.out.println("West - " + westType + ".");
            System.out.println("Below - " + belowType + ".");
            System.out.println("South - " + southType + ".");
            System.out.println("North - " + northType + ".");
            System.out.println("------------------");
        }
    }
}
