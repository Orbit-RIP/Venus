package rip.orbit.hcteams.events.koth.commands.kothschedule;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.orbit.hcteams.HCF;

import java.io.File;

public class KOTHRegenerateSchedule {

    @Command(names = {"KOTHSchedule Regenerate", "KOTHSchedule Regen"}, permission = "foxtrot.koth.admin", async = true)
    public static void kothScheduleEnableHCF(CommandSender sender) {
        File kothSchedule = new File(HCF.getInstance().getDataFolder(), "eventSchedule.json");

        if (kothSchedule.delete()) {
            HCF.getInstance().getEventHandler().loadSchedules();

            sender.sendMessage(ChatColor.YELLOW + "The event schedule has been regenerated.");
        } else {
            sender.sendMessage(ChatColor.RED + "Couldn't delete event schedule file.");
        }
    }

    @Command(names = {"KOTHSchedule debug"}, permission = "op")
    public static void kothScheduleDebug(CommandSender sender) {
        HCF.getInstance().getEventHandler().fillSchedule();
        sender.sendMessage(ChatColor.GREEN + "The event schedule has been filled.");
    }
}
