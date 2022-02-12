package rip.orbit.hcteams.events.dtc.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.events.dtc.DTC;

public class DTCCreateCommand {

    @Command(names={ "DTC Create" }, permission="foxtrot.dtc.admin")
    public static void kothCreate(Player sender, @cc.fyre.proton.command.param.Parameter(name="dtc") String koth) {
        new DTC(koth, sender.getLocation());
        sender.sendMessage(ChatColor.GRAY + "Created a DTC named " + koth + ".");
    }

}
