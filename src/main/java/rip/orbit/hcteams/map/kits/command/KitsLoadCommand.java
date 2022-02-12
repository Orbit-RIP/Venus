package rip.orbit.hcteams.map.kits.command;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.map.kits.DefaultKit;

public class KitsLoadCommand {
    
    @Command(names = { "kitadmin load" }, permission = "op")
    public static void execute(Player sender, @cc.fyre.proton.command.param.Parameter(name = "kit", wildcard = true) DefaultKit kit) {
        kit.apply(sender);
        
        sender.sendMessage(ChatColor.YELLOW + "Applied the " + ChatColor.BLUE + kit.getName() + ChatColor.YELLOW + ".");
    }

}
