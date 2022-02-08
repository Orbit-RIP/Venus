package net.frozenorb.foxtrot.events.ktk.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.ktk.KillTheKing;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KTKCommand {

    @Command(names = {"setktk" }, permission = "foxtrot.ktk.admin")
    public static void setktk(Player sender, @Parameter(name = "action")String action) {
        if (action.equalsIgnoreCase("stop")) {
            Foxtrot.getInstance().setKillTheKing(null);
        } else {
            Player target = Bukkit.getServer().getPlayer(action);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Invalid player.");
                return;
            }

            Foxtrot.getInstance().setKillTheKing(new KillTheKing(target.getUniqueId()));
        }
    }

    @Command(names = { "killtheking", "ktk", "kingthekill", "king"  }, permission = "foxtrot.ktk")
    public static void killtheking(Player sender) {
        sender.openInventory(Foxtrot.getInstance().getKillTheKingGUI().getInventory());
    }

}
