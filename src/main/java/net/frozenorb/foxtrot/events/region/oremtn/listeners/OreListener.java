package net.frozenorb.foxtrot.events.region.oremtn.listeners;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.region.oremtn.OreHandler;
import net.frozenorb.foxtrot.events.region.oremtn.OreMountain;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OreListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        OreHandler oreHandler = Foxtrot.getInstance().getOreHandler();
        OreMountain oreMountain = OreHandler.getOreMountain();
        Team teamAt = LandBoard.getInstance().getTeam(location);

        // If its unclaimed, or the server doesn't even have a mountain, or not even glowstone, why continue?
        if (Foxtrot.getInstance().getServerHandler().isUnclaimedOrRaidable(location) || !oreHandler.hasOreMountain()) {
            return;
        }

        // Check if the block broken is even in the mountain, and lets check the team to be safe
        if (teamAt == null || !teamAt.getName().equals(OreHandler.getOreTeamName())) {
            return;
        }

        if(!oreMountain.getOres().contains(location.toVector().toBlockVector())) {
            return;
        }

        //Stopping staff from breaking these blocks
        if(event.getPlayer().hasMetadata("modmode")) {
            return;
        }

        // Right, we can break this glowstone block, lets do it.
        event.setCancelled(false);

        // Now, we will decrease the value of the remaining glowstone
        oreMountain.setRemaining(oreMountain.getRemaining() - 1);

        // Let's announce when a glow mountain is a half and fully mined
        double total = oreMountain.getOres().size();
        double remaining = oreMountain.getRemaining();


        // Lets broadcast
        if (total == remaining) {
            Bukkit.broadcastMessage(ColorUtil.format("&9Ore Mountain &ehas been &9&l50% &emined."));
        } else if (remaining == 0) {
            Bukkit.broadcastMessage(ColorUtil.format("&9&lOre Mountain &ehas just been mined."));
        }
    }
}
