package net.frozenorb.foxtrot.team.commands.lives;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LivesGiveCommand {

    @Command(names = {"lives give", "lives send", "lives add"}, permission = "foxtrot.addlives")
    public static void livesGive(CommandSender sender, @Parameter(name = "player") UUID player, @Parameter(name = "life type") String lifeType, @Parameter(name = "amount") int amount) {
        if (lifeType.equalsIgnoreCase("soulbound")) {
//            int senderLives = Foxtrot.getInstance().getSoulboundLivesMap().getLives(sender.getUniqueId());
            int targetLives = Foxtrot.getInstance().getSoulboundLivesMap().getLives(player);
//            if (senderLives == 0) {
//
//            }
            Foxtrot.getInstance().getSoulboundLivesMap().setLives(player, targetLives + amount);
//            Foxtrot.getInstance().getSoulboundLivesMap().setLives(sender.getUniqueId(), senderLives + amount);
            sender.sendMessage(ChatColor.WHITE + "Gave " + ChatColor.GREEN + UUIDUtils.name(player) + ChatColor.WHITE + " " + amount + " soulbound lives.");

            Player bukkitPlayer = Bukkit.getPlayer(player);
            if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
                String suffix = sender instanceof Player ? " from " + sender.getName() : "";
                bukkitPlayer.sendMessage(ChatColor.GREEN + "You have received " + amount + " lives" + suffix);
            }

        } else if (lifeType.equalsIgnoreCase("friend")) {
            Foxtrot.getInstance().getFriendLivesMap().setLives(player, Foxtrot.getInstance().getFriendLivesMap().getLives(player) + amount);
            sender.sendMessage(ChatColor.WHITE + "Gave " + ChatColor.GREEN + UUIDUtils.name(player) + ChatColor.YELLOW + " " + amount + " friend lives.");

            Player bukkitPlayer = Bukkit.getPlayer(player);
            if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
                String suffix = sender instanceof Player ? " from " + sender.getName() : "";
                bukkitPlayer.sendMessage(ChatColor.GREEN + "You have received " + amount + " lives" + suffix);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Not a valid life type: Options are soulbound or friend");
        }
    }

}
