package rip.orbit.hcteams.events.purge.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.TimeUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class PurgeCommands {

    @Getter
    private static Map<String, Long> customTimers = new HashMap<>();


    @Command(names = "purge start", permission = "foxtrot.purge")
    public static void sotwStart(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name = "time") String time) {
        int seconds = TimeUtils.parseTime(time);
        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }


        customTimers.put("&9&lThe Purge Event", System.currentTimeMillis() + (seconds * 1000));
        sender.sendMessage(ChatColor.GREEN + "Started the Purge Event for " + time);
    }

    @Command(names = {"purge cancel"}, permission = "foxtrot.purge")
    public static void purgeCancel(CommandSender sender) {
        Long removed = customTimers.remove("&9&lThe Purge Event");
        if (removed != null && System.currentTimeMillis() < removed) {
            sender.sendMessage(ChatColor.GREEN + "Deactivated the Purge Event.");
            return;
        }

        sender.sendMessage(ChatColor.RED + "Pure Event is not active.");
    }


    public static boolean isPurgeTimer() {
        return customTimers.containsKey("&9&lThe Purge Event");
    }
}
