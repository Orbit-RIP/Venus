package rip.orbit.hcteams.team.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.team.Team;

public class StartDTRRegenCommand {

    @Command(names={ "startdtrregen" }, permission="foxtrot.startdtrregen")
    public static void startDTRRegen(Player sender, @cc.fyre.proton.command.param.Parameter(name="team") Team team) {
        team.setDTRCooldown(System.currentTimeMillis());
        sender.sendMessage(ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + " is now regenerating DTR.");
    }

}