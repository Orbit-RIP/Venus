package net.frozenorb.foxtrot.misc.game.commands;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ToggleCommand {

    @Command(names = {"game toggle"}, permission = "op")
    public static void execute(CommandSender sender) {
        Foxtrot.getInstance().getMapHandler().getGameHandler().setDisabled(!Foxtrot.getInstance().getMapHandler().getGameHandler().isDisabled());

        if (Foxtrot.getInstance().getMapHandler().getGameHandler().isDisabled()) {
            sender.sendMessage(ChatColor.YELLOW + "Events are now disabled!");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Events are now enabled!");
        }
    }

}
