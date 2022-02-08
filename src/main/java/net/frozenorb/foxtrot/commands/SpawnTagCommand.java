package net.frozenorb.foxtrot.commands;

import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.server.SpawnTagHandler;
import cc.fyre.proton.command.Command;

public class SpawnTagCommand {

    @Command(names={ "spawntag add. tagme" }, permission="op")
    public static void spawnTagMe(Player sender) {
        SpawnTagHandler.addOffensiveSeconds(sender, SpawnTagHandler.getMaxTagTime());
    }

    @Command(names={ "spawntag remove" }, permission="op")
    public static void spawnTagRemove(Player sender) {
        SpawnTagHandler.removeTag(sender);
    }

}