package net.frozenorb.foxtrot.team.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ForceLeaderCommand {

    @Command(names={ "ForceLeader" }, permission="foxtrot.forceleader")
    public static void forceLeader(Player sender, @Parameter(name="player", defaultValue="self") UUID player) {
        Team playerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(player);

        if (playerTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "That player is not on a team.");
            return;
        }

        Player bukkitPlayer = Bukkit.getPlayer(player);

        if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
            bukkitPlayer.sendMessage(ChatColor.WHITE + "A staff member has made you leader of §b" + playerTeam.getName() + "§f.");
        }

        playerTeam.setOwner(player);
        sender.sendMessage(ChatColor.YELLOW + UUIDUtils.name(player) + ChatColor.WHITE + " is now the owner of " + ChatColor.YELLOW + playerTeam.getName() + ChatColor.YELLOW + ".");
    }

}