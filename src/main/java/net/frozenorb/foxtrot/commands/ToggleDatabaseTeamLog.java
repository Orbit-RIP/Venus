package net.frozenorb.foxtrot.commands;

import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.team.track.TeamActionTracker;
import cc.fyre.proton.command.Command;

public class ToggleDatabaseTeamLog {

    @Command(names = {"databaseteamlog enable" }, permission = "op", hidden = true)
    public static void toggleDatabaseTeamLog(Player sender) {
        TeamActionTracker.setDatabaseLogEnabled(!TeamActionTracker.isDatabaseLogEnabled());
        sender.sendMessage("Enabled: " + TeamActionTracker.isDatabaseLogEnabled());
    }

}