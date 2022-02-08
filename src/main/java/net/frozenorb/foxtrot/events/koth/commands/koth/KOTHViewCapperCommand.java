package net.frozenorb.foxtrot.events.koth.commands.koth;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.koth.KOTH;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class KOTHViewCapperCommand {

    @Command(names = {  "KOTH viewcapper", "KOTH viewcap", "event viewcapper", "event viewcap"}, permission = "foxtrot.koth.viewcapper")
    public static void kothviewcapper(CommandSender sender) {
        for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
            if (event.isActive() && event instanceof KOTH) {
                KOTH koth = (KOTH) event;

                if (koth.getCurrentCapper() != null) {
                    sender.sendMessage(ChatColor.GREEN.toString() + koth.getCurrentCapper() + " is currently capping " + event.getName() + ".");
                } else {
                    sender.sendMessage(ChatColor.GREEN.toString() + "Nobody is capping KOTH right now.");
                }
                break;
            }
        }
        sender.sendMessage(ChatColor.RED + "There is no event active right now.");
    }
}
