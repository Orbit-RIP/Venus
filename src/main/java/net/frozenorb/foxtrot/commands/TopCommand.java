package net.frozenorb.foxtrot.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class TopCommand {

    @Command(names={ "top" }, permission="foxtrot.top")
    public static void top(Player sender) {

        if(getHighestY(sender.getWorld(), sender.getLocation().getX(), sender.getLocation().getZ(), sender.getLocation().getPitch(), sender.getLocation().getYaw()).getY() <= sender.getLocation().getY()) {
            sender.sendMessage(ChatColor.RED + "Already at the highest block.");
            return;
        }

        sender.teleport(getHighestY(sender.getWorld(), sender.getLocation().getX(), sender.getLocation().getZ(), sender.getLocation().getPitch(), sender.getLocation().getYaw()));
        sender.sendMessage(ChatColor.GOLD + "Teleported to highest block.");
    }

    private static Location getHighestY(World world, double x, double z, float pitch, float yaw) {
        for (int i = 255; i >= 0; i--) {
            if(new Location(world, x, i, z).getBlock().getType()!=Material.AIR) {
                Location highestBlock = new Location(world, x, i+1, z);
                highestBlock.setPitch(pitch);
                highestBlock.setYaw(yaw);

                return highestBlock;
            }
        }
        return new Location(world, x, 256, z);
    }
}
