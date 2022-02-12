package rip.orbit.hcteams.map.game.command;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.game.Game;
import rip.orbit.hcteams.map.game.GameState;

public class StartCommand {

    @Command(names = { "game start", "start" }, description = "Start the event you are hosting", permission = "")
    public static void execute(Player player) {
        if (!HCF.getInstance().getMapHandler().getGameHandler().isOngoingGame()) {
            player.sendMessage(ChatColor.RED.toString() + "There is no ongoing event.");
            return;
        }

        Game ongoingGame = HCF.getInstance().getMapHandler().getGameHandler().getOngoingGame();
        if (!ongoingGame.isPlaying(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are not in the ongoing event!");
            return;
        }

        if (ongoingGame.getHost() != player.getUniqueId()) {
            player.sendMessage(ChatColor.RED + "You are not the host of the ongoing event!");
            return;
        }

        if (ongoingGame.getPlayers().size() < ongoingGame.getGameType().getMinForceStartPlayers()) {
            player.sendMessage(ChatColor.RED + "There must be at least " + ongoingGame.getGameType().getMinForceStartPlayers() + " players to start the game!");
            return;
        }

        if (ongoingGame.getState() == GameState.WAITING) {
            ongoingGame.hostForceStart();
        } else {
            player.sendMessage(ChatColor.RED + "Can't start an event that has already been started.");
        }
    }

}
