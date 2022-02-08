package net.frozenorb.foxtrot.commands;

import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;

import java.util.UUID;

public class SpawnCommand {

    @Command(names={ "spawn" }, permission="")
    public static void spawn(Player sender, @Parameter(name="player", defaultValue="self") Player player) {
        World world = Bukkit.getWorld("world");

        if (sender.hasPermission("foxtrot.spawn")) {
            player.teleport(world.getSpawnLocation().add(0.5, 0, 0.5));
        } else {
            sender.sendMessage(ChatColor.RED + "");
            sender.sendMessage(ChatColor.RED + "Spawn is located at 0, 0.");
            sender.sendMessage(ChatColor.RED + "");
        }
    }

}
