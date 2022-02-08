package net.frozenorb.foxtrot.team.commands.lives;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LivesReviveCommand {

    @Command(names = {"pvp revive", "lives revive" }, permission = "")
    public static void pvpRevive(Player sender, @Parameter(name = "player") UUID player) {
        int friendLives = Foxtrot.getInstance().getFriendLivesMap().getLives(sender.getUniqueId());

        if (Foxtrot.getInstance().getServerHandler().isPreEOTW()) {
            sender.sendMessage(ChatColor.RED + "You may not use lives while &4&lEOTW &cis active!");
            return;
        }

        if (friendLives <= 0) {
            sender.sendMessage(ChatColor.RED + "You have no lives which can be used to revive other players!");
            return;
        }

        if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(player)) {
            sender.sendMessage(ChatColor.RED + "That player is not deathbanned!");
            return;
        }

        if (Foxtrot.getInstance().getServerHandler().getBetrayer(player) != null) {
            sender.sendMessage(ChatColor.RED + "Betrayers may not be revived!");
            return;
        }

        // Use a friend life.
        Foxtrot.getInstance().getFriendLivesMap().setLives(sender.getUniqueId(), friendLives - 1);
        sender.sendMessage(ChatColor.WHITE + "You have revived " + ChatColor.GREEN + UUIDUtils.name(player) + ChatColor.WHITE + "!");

        Foxtrot.getInstance().getDeathbanMap().revive(player);
    }

}