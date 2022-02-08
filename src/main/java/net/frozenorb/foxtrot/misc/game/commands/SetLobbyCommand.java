package net.frozenorb.foxtrot.misc.game.commands;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetLobbyCommand {

    @Command(names = { "game setlobby" }, description = "Sets the lobby spawn location for KitMap events", permission = "op", async = true)
    public static void execute(Player player) {
        Foxtrot.getInstance().getMapHandler().getGameHandler().getConfig().setLobbySpawnLocation(player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Updated KitMap event lobby location!");
    }

}
