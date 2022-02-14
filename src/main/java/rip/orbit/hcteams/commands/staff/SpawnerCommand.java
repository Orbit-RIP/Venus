package rip.orbit.hcteams.commands.staff;

import cc.fyre.proton.command.Command;
import me.lbuddyboy.crates.util.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.util.object.ItemBuilder;

public class SpawnerCommand {
    @Command(names = "spawner", permission = "orbit.admin")
    public static void spawnerCommand(Player player, @cc.fyre.proton.command.param.Parameter(name = "spawner") String spawnerName) {
        player.getInventory().addItem(new ItemBuilder(Material.MOB_SPAWNER).name(CC.translate("&r&a" + spawnerName + " Spawner")).build());
    }
}

