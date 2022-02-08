package net.frozenorb.foxtrot.misc.game.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.misc.game.Game;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LeaveCommand {

    @Command(names = { "leave", "game leave" }, description = "Leave the event", permission = "")
    public static void execute(Player player) {
        if (!Foxtrot.getInstance().getMapHandler().getGameHandler().isOngoingGame()) {
            player.sendMessage(ChatColor.RED + "There is no ongoing event.");
            return;
        }

        Game ongoingGame = Foxtrot.getInstance().getMapHandler().getGameHandler().getOngoingGame();
        if (!ongoingGame.isPlaying(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are not in the ongoing event!");
            return;
        }

        ongoingGame.removePlayer(player);
    }

}
