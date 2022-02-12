package rip.orbit.hcteams.team.commands.pvp;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;

import java.util.UUID;

public class PvPAddLivesCommand {

    @Command(names = {"pvp addlives", "addlives"}, permission = "foxtrot.addlives")
    public static void pvpSetLives(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name = "player") UUID player, @cc.fyre.proton.command.param.Parameter(name = "amount") int amount) {

        Player target = Bukkit.getPlayer(player);

        HCF.getInstance().getLivesMap().addLives(player, amount);

        sender.sendMessage(ChatColor.GREEN + "Gave " + ChatColor.YELLOW + UUIDUtils.name(player) + ChatColor.GREEN + " " + amount + " lives.");

        String suffix = sender instanceof Player ? " from " + sender.getName() : "";
        if (target != null)
            target.sendMessage(ChatColor.GREEN + "You have received " + amount + " lives" + suffix);
    }

}
