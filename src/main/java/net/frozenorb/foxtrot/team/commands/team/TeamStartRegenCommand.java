package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TeamStartRegenCommand {

    @Command(names={ "setdtrregen" }, permission="foxtrot.setdtrregen")
    public static void setDtrRegen(CommandSender sender, @Parameter(name = "action") Team team) {
        team.setDTRCooldown((team.isRaidable() ? Foxtrot.getInstance().getMapHandler().getRegenTimeRaidable():Foxtrot.getInstance().getMapHandler().getRegenTimeDeath()));

        sender.sendMessage(ChatColor.WHITE + "Put " + ChatColor.YELLOW + team.getName() + ChatColor.WHITE + " on dtr freeze.");
    }
}
