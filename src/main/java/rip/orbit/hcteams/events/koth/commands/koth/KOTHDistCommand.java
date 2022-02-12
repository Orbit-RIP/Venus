package rip.orbit.hcteams.events.koth.commands.koth;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.events.Event;
import rip.orbit.hcteams.events.EventType;
import rip.orbit.hcteams.events.koth.KOTH;

public class KOTHDistCommand {

    @Command(names={ "KOTH Dist" }, permission="foxtrot.koth.admin")
    public static void kothDist(Player sender, @cc.fyre.proton.command.param.Parameter(name="koth") Event koth, @cc.fyre.proton.command.param.Parameter(name="distance") int distance) {
        if (koth.getType() != EventType.KOTH) {
            sender.sendMessage(ChatColor.RED + "Can only set distance for KOTHs");
            return;
        }

        ((KOTH) koth).setCapDistance(distance);
        sender.sendMessage(ChatColor.GRAY + "Set max distance for the " + koth.getName() + " KOTH.");
    }

}