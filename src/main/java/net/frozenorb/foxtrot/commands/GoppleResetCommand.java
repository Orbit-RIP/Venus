package net.frozenorb.foxtrot.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;

public class GoppleResetCommand {

    @Command(names={ "gapple reset"}, permission="foxtrot.gopplereset", hidden = true)
    public static void goppleReset(Player sender, @Parameter(name="player") UUID player) {
        Foxtrot.getInstance().getOppleMap().resetCooldown(player);
        sender.sendMessage(ChatColor.RED + "Cooldown reset!");
    }

}