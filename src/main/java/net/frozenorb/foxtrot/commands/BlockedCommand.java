package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by PVPTUTORIAL | Created on 11/05/2020
 */

public class BlockedCommand {

    @Command(names = {"blocknick add", "blockednick add"}, permission = "foxtrot.blocknick")
    public static void blockNickCommand(CommandSender sender, @Parameter(name="nickname") String string) {
        if (Foxtrot.getInstance().getConfig().getStringList("blocked-nicks").contains(string.toLowerCase())) {
            sender.sendMessage(ChatColor.RED + "That nickname is already blocked.");
            return;
        }

        List<String> list = Foxtrot.getInstance().getConfig().getStringList("blocked-nicks");
        list.add(string.toLowerCase());
        Foxtrot.getInstance().getConfig().set("blocked-nicks", list);
        Foxtrot.getInstance().saveConfig();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&aYou have successfully added &f" + string.toLowerCase()
                        + " &ato the list of blocked nicknames."));
    }
    @Command(names = {"blocknick remove"}, permission = "foxtrot.blocknick")
    public static void blockNickRemoveCommand(CommandSender sender, @Parameter(name="nickname") String string) {
        if (!Foxtrot.getInstance().getConfig().getStringList("blocked-nicks").contains(string.toLowerCase())) {
            sender.sendMessage(ChatColor.RED + "That nickname is not blocked.");
            return;
        }

        List<String> list = Foxtrot.getInstance().getConfig().getStringList("blocked-nicks");
        list.remove(string.toLowerCase());
        Foxtrot.getInstance().getConfig().set("blocked-nicks", list);
        Foxtrot.getInstance().saveConfig();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&aYou have successfully remove &f" + string.toLowerCase()
                        + " &afrom the list of blocked nicknames."));
    }

}
