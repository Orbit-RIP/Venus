package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.ItemBuilder;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by PVPTUTORIAL | Created on 03/05/2020
 */

public class SpawnerCommand {

    @Command(names = "spawner", permission = "foxtrot.command.spawner")
    public static void spawnerCommand(Player player, @Parameter(name = "spawner") String spawnerName) {
        player.getInventory().addItem(new ItemBuilder(Material.MOB_SPAWNER).displayName(ChatColor.translateAlternateColorCodes('&', "&r&a" + spawnerName + " Spawner")).build());
    }

}
