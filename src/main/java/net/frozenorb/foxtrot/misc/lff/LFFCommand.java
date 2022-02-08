package net.frozenorb.foxtrot.misc.lff;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LFFCommand {

    @Command(names = { "lff" }, permission = "")
    public static void lff(Player sender) {
        if (Foxtrot.getInstance().getTeamHandler().getTeam(sender) != null) {
            sender.sendMessage(ChatColor.RED + "You cannot use that command whilst you're in a faction.");
            return;
        }
        new LFFMenu().openMenu(sender);
    }
}
