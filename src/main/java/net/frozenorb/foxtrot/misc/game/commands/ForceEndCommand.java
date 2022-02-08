package net.frozenorb.foxtrot.misc.game.commands;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ForceEndCommand {

    @Command(names = { "game forceend" }, description = "Force end an event", permission = "op")
    public static void execute(Player player) {
        if (!Foxtrot.getInstance().getMapHandler().getGameHandler().isOngoingGame()) {
            player.sendMessage(ChatColor.RED.toString() + "There is no ongoing event.");
            return;
        }

        Foxtrot.getInstance().getMapHandler().getGameHandler().getOngoingGame().endGame();
        player.sendMessage(ChatColor.GREEN + "Successfully ended the ongoing event!");
    }

}
