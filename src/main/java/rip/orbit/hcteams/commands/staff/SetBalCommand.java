package rip.orbit.hcteams.commands.staff;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.economy.EconomyHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;

import java.util.UUID;

public class SetBalCommand {


    @Command(names={ "SetBal" }, permission="orbit.admin")
    public static void setBal(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name="player") UUID player, @cc.fyre.proton.command.param.Parameter(name="amount") float amount) {
        if (amount > 10000 && sender instanceof Player && !sender.isOp()) {
            sender.sendMessage("§cthe fuck you trying to do?");
            return;
        }

        if (Float.isNaN(amount)) {
            sender.sendMessage("§cWhy are you trying to do that?");
            return;
        }


        if (amount > 250000 && sender instanceof Player) {
            sender.sendMessage("§cYou cannot set a higher amount than 250000");
            return;
        }

        Player targetPlayer = HCF.getInstance().getServer().getPlayer(player);
        EconomyHandler.setBalance(player, amount);

        if (sender != targetPlayer) {
            sender.sendMessage("§6Balance for §e" + player + "§6 set to §e$" + amount);
        }

        if (sender instanceof Player && (targetPlayer != null)) {
            String targetDisplayName = ((Player) sender).getDisplayName();
            targetPlayer.sendMessage("§aYour balance has been set to §6$" + amount + "§a by §6" + targetDisplayName);
        } else if (targetPlayer != null) {
            targetPlayer.sendMessage("§aYour balance has been set to §6$" + amount + "§a by §4CONSOLE§a.");
        }

        HCF.getInstance().getWrappedBalanceMap().setBalance(player, amount);
    }

}