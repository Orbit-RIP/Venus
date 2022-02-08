package net.frozenorb.foxtrot.team.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RecalculatePointsCommand {
    
    @Command(names = {"team pointscorrect", "f pointscorrect", "team pointscorrect"}, permission = "op")
    public static void recalculate(CommandSender sender) {
        int changed = 0;
        
        for (Team team : Foxtrot.getInstance().getTeamHandler().getTeams()) {
            int oldPoints = team.getPoints();
            team.recalculatePoints();
            if (team.getPoints() != oldPoints) {
                team.flagForSave();
                sender.sendMessage(ChatColor.WHITE + "Changed " + team.getName() + "'s points from " + oldPoints + " to " + team.getPoints());
                changed++;
            }

        }
        
        sender.sendMessage(ChatColor.WHITE + "Changed a total of " + changed + " teams points.");
    }
    
}