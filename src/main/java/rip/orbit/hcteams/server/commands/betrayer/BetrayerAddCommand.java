package rip.orbit.hcteams.server.commands.betrayer;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.util.Betrayer;

import java.util.UUID;

import static org.bukkit.ChatColor.*;

public class BetrayerAddCommand {

    @Command(names = {"betrayer add"}, permission = "op")
    public static void betrayerAdd(Player sender, @cc.fyre.proton.command.param.Parameter(name = "player") UUID player, @cc.fyre.proton.command.param.Parameter(name = "reason", wildcard=true) String reason) {
        if (HCF.getInstance().getServerHandler().getBetrayer(player) == null) {
            Betrayer betrayer = new Betrayer(player, sender.getUniqueId(), reason);
            HCF.getInstance().getServerHandler().getBetrayers().add(betrayer);
            HCF.getInstance().getServerHandler().save();

            sender.sendMessage(GREEN + "Added " + RED + UUIDUtils.name(player) + GREEN + "'s betrayer tag for " + YELLOW +  reason + GREEN + ".");
        } else {
            sender.sendMessage(RED + UUIDUtils.name(player) + " is already a betrayer.");
        }
    }

}