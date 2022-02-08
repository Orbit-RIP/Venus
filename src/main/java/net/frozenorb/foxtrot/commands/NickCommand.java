package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.ServerHandler;
import net.frozenorb.foxtrot.util.ColorUtil;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Pattern;

public class NickCommand {

    @Command(names = { "nick" }, permission = "foxtrot.nickt")
    public static void nick(Player sender, @Parameter(name = "nick") String nick) {
        if (nick.equalsIgnoreCase("reset")) {
            sender.sendMessage(ChatColor.YELLOW + "Successfully reset your nick.");
            Foxtrot.getInstance().getServerHandler().getNickName().remove(sender.getUniqueId());
            return;
        }

        if (nick.length() > 16 || nick.length() < 3 && !sender.getName().equalsIgnoreCase("AlfieYT")) {
            sender.sendMessage(CC.translate("&eYour nick-name must be between &a3-16 letters&e long!!"));
            return;
        }

        if (Bukkit.getPlayer(nick) != null) {
            sender.sendMessage(ChatColor.RED + "That name is already is in use.");
            return;
        }

        if (checkSpecialCharacters(nick)) {
            sender.sendMessage(ChatColor.RED + "You cannot use that nickname.");
            return;
        }

        List<String> blockedNames = Foxtrot.getInstance().getConfig().getStringList("blocked-nicks");

        for (String str : blockedNames) {
            if (str.equalsIgnoreCase(nick)) {
                sender.sendMessage(ChatColor.RED + "This is a blocked nickname.");
                return;
            }
        }

        for (String nicks : Foxtrot.getInstance().getServerHandler().getNickName().values()) {
            if (nicks.equalsIgnoreCase(nick)) {
                sender.sendMessage(ChatColor.RED + "You cannot set your nickname to this as somebody online already has that name.");
                return;
            }
        }

        sender.sendMessage(ColorUtil.format("&aYour nick has been successfully set to &f" + nick + "&a."));
        Foxtrot.getInstance().getServerHandler().getNickName().put(sender.getUniqueId(), nick);
    }

    public static boolean checkSpecialCharacters(String in) {
        Pattern pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);

        if (pattern.matcher(in).find()) {
            return true;
        }

        return false;
    }
}
