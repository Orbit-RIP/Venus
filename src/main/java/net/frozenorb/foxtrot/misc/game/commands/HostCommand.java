package net.frozenorb.foxtrot.misc.game.commands;

import net.frozenorb.foxtrot.misc.game.menu.HostMenu;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HostCommand {

    @Command(names = { "host", "game host" }, description = "Host a KitMap Event", permission = "", async = true)
    public static void execute(Player player) {
        if (SpawnTagHandler.isTagged(player)) {
            player.sendMessage(ChatColor.RED + "You can't host an event while spawn-tagged!");
            return;
        }

        new HostMenu().openMenu(player);
    }

}
