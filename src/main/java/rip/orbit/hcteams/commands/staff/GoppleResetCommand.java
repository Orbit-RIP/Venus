package rip.orbit.hcteams.commands.staff;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;

import java.util.UUID;

public class GoppleResetCommand {

    @Command(names={ "GoppleReset" }, permission="orbit.admin")
    public static void goppleReset(Player sender, @cc.fyre.proton.command.param.Parameter(name="player") UUID player) {
        HCF.getInstance().getOppleMap().resetCooldown(player);
        sender.sendMessage(ChatColor.RED + "Cooldown reset!");
    }

}