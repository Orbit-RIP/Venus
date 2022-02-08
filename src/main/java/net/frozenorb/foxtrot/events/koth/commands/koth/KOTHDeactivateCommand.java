package net.frozenorb.foxtrot.events.koth.commands.koth;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.EventScheduledTime;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.frozenorb.foxtrot.events.Event;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;

import java.util.Date;

public class KOTHDeactivateCommand {

    @Command(names={ "KOTH Deactivate", "KOTH Inactive", "event deactivate" }, permission="foxtrot.koth.admin")
    public static void kothDectivate(CommandSender sender, @Parameter(name="koth") Event koth) {
        koth.deactivate();

        EventScheduledTime scheduledTime = EventScheduledTime.parse(new Date());
        Foxtrot.getInstance().getEventHandler().getEventSchedule().remove(scheduledTime);

        sender.sendMessage(ChatColor.GRAY + "Deactivated " + koth.getName() + " event.");
    }

}
