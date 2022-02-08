package net.frozenorb.foxtrot.misc.creditshop.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.misc.creditshop.menu.CreditShopMenu;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreditShopCommand {

    @Command(names = {"tokens", "token"}, permission = "", async = true)
    public static void creditShopCommand(Player sender) {
        if (!(DTRBitmask.SAFE_ZONE.appliesAt(sender.getLocation()))) {
            sender.sendMessage(CC.RED + "You must be in spawn to use this command!");
            return;
        }

        new CreditShopMenu().openMenu(sender);
    }

    @Command(names = {"tokens set", "token set"}, async = true, permission = "op")
    public static void creditGiveCommand(CommandSender sender, @Parameter(name = "player") Player target, @Parameter(name = "amount") int amount) {
        Foxtrot.getInstance().getCreditsMap().setCredits(target.getUniqueId(), amount);
        sender.sendMessage(CC.GREEN + "You have set " + target.getName());
    }

}
