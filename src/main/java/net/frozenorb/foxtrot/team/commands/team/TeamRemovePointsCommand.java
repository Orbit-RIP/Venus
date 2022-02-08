package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamRemovePointsCommand {

    @Command(names={ "team removepoints", "t removepoints", "f removepoints", "faction removepoints", "fac removepoints" }, permission="foxtrot.removepoints")
    public static void teamAddPoints(Player sender, @Parameter(name = "team") final Team team, @Parameter(name = "amount") int amount) {
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "That team doesn't exist!");
            return;
        }

        team.setAddedPoints(team.getAddedPoints() + -amount);
        sender.sendMessage(CC.translate("&fYou have successfully &cremoved &f" + amount + "&f points from &a" + team.getName() + "&f."));
    }
}