package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamHQCommand {

    @Command(names={ "team hq", "t hq", "f hq", "faction hq", "fac hq", "team home", "t home", "f home", "faction home", "fac home", "home", "hq" }, permission="")
    public static void teamHQ(Player sender) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GOLD + "You are not on a team!");
            return;
        }

        if (team.getHQ() == null) {
            sender.sendMessage(ChatColor.RED + "HQ not set.");
            return;
        }

        if (Foxtrot.getInstance().getServerHandler().isEOTW()) {
            sender.sendMessage(ChatColor.RED + "You cannot teleport to your team headquarters during the End of the World!");
            return;
        }

        if (sender.hasMetadata("frozen")) {
            sender.sendMessage(ChatColor.RED + "You cannot teleport to your team headquarters while you're frozen!");
            return;
        }

        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Use /pvp enable to toggle your PvP Timer off!");
            return;
        }

        Foxtrot.getInstance().getServerHandler().beginHQWarp(sender, team, 10, false);
    }
}