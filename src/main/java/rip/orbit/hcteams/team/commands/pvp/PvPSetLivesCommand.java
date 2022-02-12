package rip.orbit.hcteams.team.commands.pvp;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;

import java.util.UUID;

public class PvPSetLivesCommand {

    @Command(names={ "pvptimer setlives", "pvp setlives", "pvptimer setlives", "pvp setlives" }, permission="foxtrot.setlives")
    public static void pvpSetLives(Player sender, @cc.fyre.proton.command.param.Parameter(name="player") UUID player, @cc.fyre.proton.command.param.Parameter(name="amount") int amount) {
        HCF.getInstance().getLivesMap().setLives(player, amount);
        sender.sendMessage(ChatColor.YELLOW + "Set " + ChatColor.GREEN + UUIDUtils.name(player) + ChatColor.YELLOW + "'s life count to " + amount + ".");

    }

}