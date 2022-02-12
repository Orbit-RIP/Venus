package rip.orbit.hcteams.team.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.team.Team;

import java.util.UUID;

public class ForceKickCommand {

    @Command(names={ "forcekick" }, permission="foxtrot.forcekick")
    public static void forceKick(Player sender, @cc.fyre.proton.command.param.Parameter(name="player") UUID player) {
        Team team = HCF.getInstance().getTeamHandler().getTeam(player);

        if (team == null) {
            sender.sendMessage(ChatColor.RED + UUIDUtils.name(player) + " is not on a team!");
            return;
        }

        if (team.getMembers().size() == 1) {
            sender.sendMessage(ChatColor.RED + UUIDUtils.name(player) + "'s team has one member. Please use /forcedisband to perform this action.");
            return;
        }

        team.removeMember(player);
        HCF.getInstance().getTeamHandler().setTeam(player, null);

        Player bukkitPlayer = Bukkit.getPlayer(player);
        if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
            bukkitPlayer.sendMessage(ChatColor.RED + "You were kicked from your team by a staff member.");
        }

        sender.sendMessage(ChatColor.YELLOW + "Force kicked " + ChatColor.LIGHT_PURPLE + UUIDUtils.name(player) + ChatColor.YELLOW + " from their team, " + ChatColor.LIGHT_PURPLE + team.getName() + ChatColor.YELLOW + ".");
    }

}