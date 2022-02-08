package net.frozenorb.foxtrot.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.listener.EndListener;
import cc.fyre.proton.command.Command;

public class ToggleEndCommand {

    @Command(names={ "ToggleEnd" }, permission="foxtrot.toggleend", hidden = true)
    public static void toggleEnd(Player sender) {
        EndListener.endActive = !EndListener.endActive;
        sender.sendMessage(ChatColor.WHITE + "End enabled? " + ChatColor.YELLOW + (EndListener.endActive ? "Yes" : "No"));
    }

}