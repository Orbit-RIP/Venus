package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class ResetReclaimCommand {

    @Command(
            names = {"resetreclaim","reclaimreset"},
            permission = "foxtrot.command.resetreclaim"
    )
    public static void execute(CommandSender sender, @Parameter(name = "player") UUID uuid) {

        if (!Foxtrot.getInstance().getReclaimMap().hasReclaimed(uuid)) {
            sender.sendMessage(ChatColor.RED + "That player has not reclaimed yet this map!");
            return;
        }

        Foxtrot.getInstance().getReclaimMap().setReclaimed(uuid,false);

        sender.sendMessage(ChatColor.GOLD + "Reset " + ChatColor.YELLOW + uuid.toString() + ChatColor.GOLD + "'s reclaim.");
    }

}
