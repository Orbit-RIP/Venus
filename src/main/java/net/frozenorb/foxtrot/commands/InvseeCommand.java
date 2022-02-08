package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InvseeCommand {

    @Command(names = { "invsee" }, permission = "foxtrot.invsee")
    public static void invsee(Player sender, @Parameter(name="player") UUID player) {
        Player target = Foxtrot.getInstance().getServer().getPlayer(player);

        if(target == null) {
            sender.sendMessage(ChatColor.RED + "No player with that name found.");
            return;
        }

        sender.openInventory(target.getInventory());
        sender.sendMessage(CC.GOLD + "Successfully opened " + target.getName() + " inventory!");
    }

}
