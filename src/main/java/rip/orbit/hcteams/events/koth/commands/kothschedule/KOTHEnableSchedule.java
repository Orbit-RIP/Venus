package rip.orbit.hcteams.events.koth.commands.kothschedule;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.orbit.hcteams.HCF;

public class KOTHEnableSchedule {

    @Command(names = "KOTHSchedule Enable", permission = "foxtrot.koth.admin")
    public static void kothScheduleEnable(CommandSender sender) {
        HCF.getInstance().getEventHandler().setScheduleEnabled(true);

        sender.sendMessage(ChatColor.YELLOW + "The KOTH schedule has been " + ChatColor.GREEN + "enabled" + ChatColor.YELLOW + "");
    }

}
