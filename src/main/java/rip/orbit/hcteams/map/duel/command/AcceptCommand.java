package rip.orbit.hcteams.map.duel.command;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.duel.DuelHandler;
import rip.orbit.hcteams.map.duel.DuelInvite;

public class AcceptCommand {

    @Command(names = { "accept" }, permission = "")
    public static void accept(Player sender, @cc.fyre.proton.command.param.Parameter(name = "player") Player target) {
        if (!HCF.getInstance().getMapHandler().isKitMap()) {
            sender.sendMessage(ChatColor.RED + "This command is only available on KitMap!");
            return;
        }

        DuelHandler duelHandler = HCF.getInstance().getMapHandler().getDuelHandler();

        if (!duelHandler.canAccept(sender, target)) {
            return;
        }

        DuelInvite invite = duelHandler.getInvite(target.getUniqueId(), sender.getUniqueId());
        duelHandler.acceptDuelRequest(invite);
    }

}
