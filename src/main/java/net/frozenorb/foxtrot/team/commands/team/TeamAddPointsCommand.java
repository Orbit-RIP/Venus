package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamAddPointsCommand {

    @Command(names={ "team addpoints", "t addpoints", "f addpoints", "faction addpoints", "fac addpoints" }, permission="foxtrot.addpoints")
    public static void teamAddPoints(Player sender, @Parameter(name = "team") final Team team, @Parameter(name = "amount") int amount) {
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "That team doesn't exist!");
            return;
        }

        team.setAddedPoints(team.getAddedPoints() + amount);
        sender.sendMessage(CC.translate("&fYou have successfully added &e" + amount + "&f points to &a" + team.getName() + "&f."));
    }
}
