package rip.orbit.hcteams.cheat;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import rip.orbit.hcteams.HCF;

public class CheatCommand {

    @Command(names = "cheat", permission = "hcf.cheats")
    public static void cheat(Player sender) {
        if (!sender.hasMetadata("cheat")) {
            sender.setMetadata("cheat", new FixedMetadataValue(HCF.getInstance(), "cheat"));
            sender.sendMessage(ChatColor.GREEN + "You have enabled cheats.");
        } else {
            sender.removeMetadata("cheat", HCF.getInstance());
            sender.sendMessage(ChatColor.RED + "You have disabled cheats.");
        }
    }
}

