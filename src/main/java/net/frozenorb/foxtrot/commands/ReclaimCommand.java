
package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

public class ReclaimCommand {

    @Command(
            names = {"reclaim"},
            permission = ""
    )
    public static void execute(Player player) {

        if (Foxtrot.getInstance().getReclaimMap().hasReclaimed(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have already reclaimed this map.");
            return;
        }

        Profile user = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId());

        if (Foxtrot.getInstance().getConfig().getStringList("reclaim." + user.getActiveRank().getName()) == null || !Foxtrot.getInstance().getConfig().contains("reclaim." +user.getActiveRank().getName())) {
            player.sendMessage(ChatColor.RED + "It appears there is no reclaim found for your rank.");
            return;
        }

        for (String command : Foxtrot.getInstance().getConfig().getStringList("reclaim." + user.getActiveRank().getName())) {
            Foxtrot.getInstance().getServer().dispatchCommand(Foxtrot.getInstance().getServer().getConsoleSender(),command.replace("{player}",player.getName()));
        }

        Foxtrot.getInstance().getReclaimMap().setReclaimed(player.getUniqueId(),true);
    }

}