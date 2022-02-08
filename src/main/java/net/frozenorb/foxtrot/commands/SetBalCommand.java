package net.frozenorb.foxtrot.commands;

import java.util.UUID;

import cc.fyre.proton.Proton;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import rip.orbit.nebula.Nebula;

public class SetBalCommand {

    @Command(names={ "eco set" }, permission="foxtrot.setbal")
    public static void setBal(CommandSender sender, @Parameter(name="player") UUID player, @Parameter(name="amount") float amount) {
        if (amount > 10000 && sender instanceof Player && !sender.isOp()) {
            sender.sendMessage("§cYou cannot set a balance this high. This action has been logged.");
            return;
        }

        if (Float.isNaN(amount)) {
            sender.sendMessage("§cWhy are you trying to do that?");
            return;
        }


        if (amount > 250000 && sender instanceof Player && !sender.isOp()) {
            sender.sendMessage("§cYou have exceeded the maximum capacity of balance gained in 1 try.");
            return;
        }

        Player targetPlayer = Foxtrot.getInstance().getServer().getPlayer(player);
        Proton.getInstance().getEconomyHandler().setBalance(player, amount);

        if (sender != targetPlayer) {
            sender.sendMessage("§3Balance for §b" + player + "§3 set to §2$§a" + amount);
        }

        if (sender instanceof Player && (targetPlayer != null)) {
            String targetDisplayName = ((Player) sender).getDisplayName();
            targetPlayer.sendMessage("§3Your balance has been set to §2$§a" + amount + "§3 by " + Nebula.getInstance().getProfileHandler().fromUuid(player) + targetDisplayName);
        } else if (targetPlayer != null) {
            targetPlayer.sendMessage("§aYour balance has been set to §2$§a" + amount + "§a by §4&lCONSOLE§a.");
        }

        Foxtrot.getInstance().getWrappedBalanceMap().setBalance(player, amount);
    }

}