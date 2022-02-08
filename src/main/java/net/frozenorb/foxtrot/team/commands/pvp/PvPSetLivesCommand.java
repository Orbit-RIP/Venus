package net.frozenorb.foxtrot.team.commands.pvp;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PvPSetLivesCommand {

    @Command(names = {"pvptimer setlives", "timer setlives", "pvp setlives", "pvptimer setlives", "timer setlives", "pvp setlives"}, permission = "foxtrot.setlives")
    public static void pvpSetLives(CommandSender sender, @Parameter(name = "player") UUID player, @Parameter(name = "life type") String lifeType, @Parameter(name = "amount") int amount) {
        if (lifeType.equalsIgnoreCase("soulbound")) {
            Foxtrot.getInstance().getSoulboundLivesMap().setLives(player, amount);
            sender.sendMessage(ChatColor.WHITE + "Set " + ChatColor.GREEN + UUIDUtils.name(player) + ChatColor.WHITE + "'s soulbound life count to " + amount + ".");
        } else if (lifeType.equalsIgnoreCase("friend")) {
            Foxtrot.getInstance().getFriendLivesMap().setLives(player, amount);
            sender.sendMessage(ChatColor.WHITE + "Set " + ChatColor.GREEN + UUIDUtils.name(player) + ChatColor.WHITE + "'s friend life count to " + amount + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "Not a valid life type: Options are soulbound or friend.");
        }
    }

}