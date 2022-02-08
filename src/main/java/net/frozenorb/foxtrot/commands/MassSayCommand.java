package net.frozenorb.foxtrot.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MassSayCommand {

    @Command(names = { "masssay" }, permission = "op")
    public static void massSay(CommandSender sender, @Parameter(name = "message", defaultValue = " ", wildcard = true) String message) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.chat(message);
            if (online.hasPermission("foxtrot.masssay")) {
                online.sendMessage(ChatColor.WHITE + sender.getName() + ChatColor.YELLOW + " has ran the mass say command!");
            }
        }
        return;
    }

}
