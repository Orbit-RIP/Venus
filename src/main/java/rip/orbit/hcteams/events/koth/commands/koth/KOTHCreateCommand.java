package rip.orbit.hcteams.events.koth.commands.koth;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.events.koth.KOTH;

public class KOTHCreateCommand {

    @Command(names={ "KOTH Create" }, permission="foxtrot.koth.admin")
    public static void kothCreate(Player sender, @cc.fyre.proton.command.param.Parameter(name="koth") String koth) {
        new KOTH(koth, sender.getLocation());
        sender.sendMessage(ChatColor.GRAY + "Created a KOTH named " + koth + "");
    }

}