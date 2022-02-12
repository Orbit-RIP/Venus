package rip.orbit.hcteams.map.kits.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.kits.DefaultKit;

public class KitDescriptionCommand {

    @Command(names = { "kitadmin setdesc" }, description = "Sets the description of a kit", permission = "op")
    public static void execute(Player player, @cc.fyre.proton.command.param.Parameter(name = "kit") DefaultKit kit, @cc.fyre.proton.command.param.Parameter(name = "description", wildcard = true) String description) {
        kit.setDescription(description);
        HCF.getInstance().getMapHandler().getKitManager().saveDefaultKits();

        player.sendMessage(ChatColor.GREEN + "Set description of " + kit.getName() + "!");
    }

}
