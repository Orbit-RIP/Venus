package net.frozenorb.foxtrot.misc.game.impl.thimble;

import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.misc.game.Game;
import net.frozenorb.foxtrot.misc.game.GameType;
import net.frozenorb.foxtrot.misc.game.arena.GameArena;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ThimbleGame extends Game {

    public ThimbleGame(UUID host, GameType gameType, List<GameArena> arenaOptions) {
        super(host, gameType, arenaOptions);
    }

    @Override
    public void startGame() {
        super.startGame();
    }

    @Override
    public Player findWinningPlayer() {
        return null;
    }

    @Override
    public void getScoreboardLines(Player player, LinkedList<String> lines) {

    }

    @Override
    public List<FancyMessage> createHostNotification() {
        return null;
    }
}
