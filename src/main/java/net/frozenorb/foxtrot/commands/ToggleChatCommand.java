package net.frozenorb.foxtrot.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;

public class ToggleChatCommand {

    @Command(names={"ToggleGlobalChat", "TGC" }, permission="")
    public static void toggleChat(Player sender) {
        boolean val = !Foxtrot.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(sender.getUniqueId());

        sender.sendMessage(ChatColor.WHITE + "You are now " + (!val ? ChatColor.RED + "unable" : ChatColor.GREEN + "able") + ChatColor.WHITE + " to see global chat!");
        Foxtrot.getInstance().getToggleGlobalChatMap().setGlobalChatToggled(sender.getUniqueId(), val);
    }

}