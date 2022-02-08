package net.frozenorb.foxtrot.listener;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.Openable;

public class PearlGlitchListener implements Listener {

    private final ImmutableSet<Material> blockedPearlTypes = Sets.immutableEnumSet(
            Material.THIN_GLASS,
            Material.GLASS,
            Material.STAINED_GLASS,
            Material.STAINED_GLASS_PANE,
            Material.IRON_FENCE,
            Material.CHEST,
            Material.TRAPPED_CHEST,
            Material.FENCE
    );

    private final ImmutableSet<Material> stairTypes = Sets.immutableEnumSet(
            Material.BRICK_STAIRS,
            Material.SMOOTH_STAIRS,
            Material.WOOD_STAIRS,
            Material.SPRUCE_WOOD_STAIRS,
            Material.NETHER_BRICK_STAIRS,
            Material.QUARTZ_STAIRS,
            Material.COBBLESTONE_STAIRS,
            Material.BRICK_STAIRS,
            Material.SANDSTONE_STAIRS,
            Material.BIRCH_WOOD_STAIRS
    );

    private final ImmutableSet<Material> returnTypes = Sets.immutableEnumSet(
            Material.BRICK_STAIRS,
            Material.SMOOTH_STAIRS,
            Material.WOOD_STAIRS,
            Material.SPRUCE_WOOD_STAIRS,
            Material.TORCH,
            Material.NETHER_BRICK_STAIRS,
            Material.QUARTZ_STAIRS,
            Material.BEDROCK,
            Material.CAKE,
            Material.STEP,
            Material.LEVER
    );


    private static String getDirection(float yaw) {
        yaw -= 180;
        yaw = (yaw % 360);
        if(yaw < 0) yaw += 360;

        if (0 <= yaw && yaw < 22.5) {
            return "NORTH";
        } else if (22.5 <= yaw && yaw < 67.5) {
            return "NORTH-EAST";
        } else if (67.5 <= yaw && yaw < 112.5) {
            return "EAST";
        } else if (112.5 <= yaw && yaw < 157.5) {
            return "SOUTH-EAST";
        } else if (157.5 <= yaw && yaw < 202.5) {
            return "SOUTH";
        } else if (202.5 <= yaw && yaw < 247.5) {
            return "SOUTH-WEST";
        } else if (247.5 <= yaw && yaw < 292.5) {
            return "WEST";
        } else if (292.5 <= yaw && yaw < 337.5) {
            return "NORTH-WEST";
        } else if (337.5 <= yaw && yaw < 360.0) {
            return "NORTH";
        } else {
            return "?";
        }
    }
}