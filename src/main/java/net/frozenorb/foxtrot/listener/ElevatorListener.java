package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.Elevator;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.event.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElevatorListener implements Listener {

    private final List<String> elevatorDirections = Arrays.asList(Arrays.stream(Elevator.values()).map(Elevator::name).map(String::toLowerCase).collect(Collectors.joining()));
    private final List<Material> signMaterials = Arrays.asList(Material.SIGN_POST, Material.WALL_SIGN);

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignInteract(PlayerInteractEvent event) {

        final Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !this.signMaterials.contains(event.getClickedBlock().getType())) {
            return;
        }

        final BlockState blockState = event.getClickedBlock().getState();

        if (!(blockState instanceof Sign)) {
            return;
        }

        final Sign sign = (Sign) blockState;

        if (!sign.getLine(0).contains("[Elevator]")) {
            return;
        }

        final Block block = player.getTargetBlock(null,(int)sign.getLocation().distance(player.getLocation()));

        if (block != null && !(block.getState() instanceof Sign)) {
            return;
        }

        Elevator elevator;

        try {
            elevator = Elevator.valueOf(ChatColor.stripColor(sign.getLine(1).toUpperCase()));
        } catch (IllegalArgumentException ex) {
            player.sendMessage(ChatColor.RED + "Invalid elevator direction, try UP or DOWN.");
            ex.printStackTrace();
            return;
        }

        if (elevator == null) {
            player.sendMessage(ChatColor.RED + "Invalid elevator direction, try UP or DOWN.");
            return;
        }

        final Location toTeleport = elevator.getCalculatedLocation(sign.getLocation(),Elevator.Type.SIGN);

        if (toTeleport == null) {
            player.sendMessage(ChatColor.RED + "There was an issue trying to find a valid location!");
            return;
        }

        toTeleport.setYaw(player.getLocation().getYaw());
        toTeleport.setPitch(player.getLocation().getPitch());
        player.teleport(toTeleport.add(0.5,0,0.5));
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[Elevator]") && event.getLine(1).equalsIgnoreCase("Up")) {
            event.setLine(0, CC.translate("&9[Elevator]"));
            event.setLine(1, "Up");
        }
        if (event.getLine(0).equalsIgnoreCase("[Elevator]") && event.getLine(1).equalsIgnoreCase("Down")) {
            event.setLine(0, CC.translate("&9[Elevator]"));
            event.setLine(1, "Down");
        }
    }
}