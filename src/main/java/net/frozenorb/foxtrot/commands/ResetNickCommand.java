package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.ServerHandler;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ResetNickCommand {

    @Command(names = { "resetnick" }, permission = "foxtrot.nick")
    public static void resetNick(Player sender) {
        sender.sendMessage(ChatColor.YELLOW + "Successfully reset your nick.");
        Foxtrot.getInstance().getServerHandler().getNickName().remove(sender.getUniqueId());
        return;
    }
}
