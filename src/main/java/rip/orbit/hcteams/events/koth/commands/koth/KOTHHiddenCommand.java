package rip.orbit.hcteams.events.koth.commands.koth;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.events.Event;

public class KOTHHiddenCommand {

    @Command(names={ "KOTH Hidden", "events hidden", "event hidden" }, permission="foxtrot.koth.admin")
    public static void kothHidden(Player sender, @cc.fyre.proton.command.param.Parameter(name="koth") Event koth, @cc.fyre.proton.command.param.Parameter(name="hidden") boolean hidden) {
        koth.setHidden(hidden);
        sender.sendMessage(ChatColor.GRAY + "Set visibility for the " + koth.getName() + " event.");
    }

}