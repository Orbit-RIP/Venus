package net.frozenorb.foxtrot.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;

public class ToggleDeathMessagesCommand {

    @Command(names = {"toggledeathmessages", "tdm"}, permission = "")
    public static void toggledeathmessages(Player sender) {
        boolean val = !Foxtrot.getInstance().getToggleDeathMessageMap().areDeathMessagesEnabled(sender.getUniqueId());


        sender.sendMessage(ChatColor.WHITE + "You are now " + (!val ? ChatColor.RED + "unable" : ChatColor.GREEN + "able") + ChatColor.WHITE + " to see Death Messages!");
        Foxtrot.getInstance().getToggleDeathMessageMap().setDeathMessagesEnabled(sender.getUniqueId(), val);
    }

}
