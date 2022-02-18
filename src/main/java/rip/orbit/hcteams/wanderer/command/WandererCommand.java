package rip.orbit.hcteams.wanderer.command;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;

public class WandererCommand {

    @Command(names = "wanderer spawn", permission = "wanderer.spawn")
    public static void wandererSpawn(Player player) {
        HCF.getInstance().getWandererHandler().spawnWanderer();
    }
}
