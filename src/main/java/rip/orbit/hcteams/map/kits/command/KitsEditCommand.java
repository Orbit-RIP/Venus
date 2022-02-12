package rip.orbit.hcteams.map.kits.command;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.kits.DefaultKit;

public class KitsEditCommand {
    
    @Command(names = { "kitadmin edit" }, permission = "op")
    public static void execute(Player sender, @cc.fyre.proton.command.param.Parameter(name = "kit", wildcard = true) DefaultKit kit) {
        kit.update(sender.getInventory());
        HCF.getInstance().getMapHandler().getKitManager().saveDefaultKits();

        sender.sendMessage(ChatColor.YELLOW + "Kit " + ChatColor.BLUE + kit.getName() + ChatColor.YELLOW + " has been edited and saved.");
    }

}
