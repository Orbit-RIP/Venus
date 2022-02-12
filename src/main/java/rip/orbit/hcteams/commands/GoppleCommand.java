package rip.orbit.hcteams.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;

public class GoppleCommand {

    @Command(names={ "Gopple", "Opple", "GoppleTime", "OppleTime", "GoppleTimer", "OppleTimer" }, permission="")
    public static void gopple(Player sender) {
        if (HCF.getInstance().getOppleMap().isOnCooldown(sender.getUniqueId())) {
            long millisLeft = HCF.getInstance().getOppleMap().getCooldown(sender.getUniqueId()) - System.currentTimeMillis();
            sender.sendMessage(ChatColor.GOLD + "Gopple cooldown: " + ChatColor.WHITE + TimeUtils.formatIntoDetailedString((int) millisLeft / 1000));
        } else {
            sender.sendMessage(ChatColor.RED + "No current gopple cooldown!");
        }
    }

}