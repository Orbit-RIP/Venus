package rip.orbit.hcteams.team.commands.pvp;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;

import java.util.concurrent.TimeUnit;

public class PvPCreateCommand {

    @Command(names={ "pvptimer create", "timer create", "pvp create", "pvp add", "pvp give" }, permission="worldedit.*")
    public static void pvpCreate(Player sender, @cc.fyre.proton.command.param.Parameter(name="player", defaultValue="self") Player player) {
        HCF.getInstance().getPvPTimerMap().createTimer(player.getUniqueId(), (int) TimeUnit.MINUTES.toSeconds(30));
        player.sendMessage(ChatColor.YELLOW + "You have 30 minutes of PVP Timer!");

        if (sender != player) {
            sender.sendMessage(ChatColor.YELLOW + "Gave 30 minutes of PVP Timer to " + player.getName() + ".");
        }
    }

}