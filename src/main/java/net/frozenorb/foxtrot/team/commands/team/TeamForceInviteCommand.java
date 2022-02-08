//package net.frozenorb.foxtrot.team.commands.team;
/*
import com.google.common.collect.ImmutableMap;

import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.track.TeamActionTracker;
import net.frozenorb.foxtrot.team.track.TeamActionType;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
public class TeamForceInviteCommand {


    @Command(names = {"team forceinvite", "t forceinvite", "f forceinvite", "faction forceinvite", "fac forceinvite"}, permission = "")
    public static void teamForceInvite(Player sender, @Parameter(name="player") UUID player) {
        if (!Foxtrot.getInstance().getServerHandler().isForceInvitesEnabled()) {
            sender.sendMessage(ChatColor.RED + "Force-invites are not enabled on this server.");
            return;
        }

        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (Foxtrot.getInstance().getMapHandler().isKitMap()) {
            sender.sendMessage(ChatColor.RED + "You don't need to use this during kit maps.");
            return;
        }

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.getMembers().size() >= Foxtrot.getInstance().getMapHandler().getTeamSize()) {
            sender.sendMessage(ChatColor.RED + "The max team size is " + Foxtrot.getInstance().getMapHandler().getTeamSize() + "!");
            return;
        }

        if (!(team.isOwner(sender.getUniqueId()) || team.isCoLeader(sender.getUniqueId()) || team.isCaptain(sender.getUniqueId()))) {
            sender.sendMessage(ChatColor.GOLD + "Only team captains can do this.");
            return;
        }

        if (team.isMember(player)) {
            sender.sendMessage(ChatColor.GOLD + UUIDUtils.name(player) + " is already on your team.");
            return;
        }

        if (team.getInvitations().contains(player)) {
            sender.sendMessage(ChatColor.RED + "That player has already been invited.");
            return;
        }

        if (!team.getHistoricalMembers().contains(player)) {
            sender.sendMessage(ChatColor.RED + "That player has never been a member of your faction. Please use /f invite.");
            return;
        }

        if (team.isRaidable()) {
            sender.sendMessage(ChatColor.RED + "You may not invite players while your team is raidable!");
            return;
        }

        if (team.getForceInvites() == 0) {
            sender.sendMessage(ChatColor.RED + "You do not have any force-invites left!");
            return;
        }

        team.setForceInvites(team.getForceInvites() - 1);
        TeamActionTracker.logActionAsync(team, TeamActionType.PLAYER_INVITE_SENT, ImmutableMap.of(
                "playerId", player,
                "invitedById", sender.getUniqueId(),
                "invitedByName", sender.getName(),
                "betrayOverride", "false",
                "usedForceInvite", "true"
        ));

        // we use a runnable so /this message gets displayed at the end
        new BukkitRunnable() {
            @Override
            public void run() {
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You have used a force-invite.");

                if (team.getForceInvites() != 0) {
                    sender.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.RED + team.getForceInvites() + ChatColor.YELLOW + " of those left.");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.RED + "none" + ChatColor.YELLOW + " of those left.");
                }
            }
        }.runTask(Foxtrot.getInstance());

        team.getInvitations().add(player);
        team.flagForSave();

        Player bukkitPlayer = Foxtrot.getInstance().getServer().getPlayer(player);

        if (bukkitPlayer != null) {
            bukkitPlayer.sendMessage(ChatColor.GOLD + sender.getName() + " invited you to join '" + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "'.");

            FancyMessage clickToJoin =new FancyMessage("Type '").color(ChatColor.GOLD).then("/team join " + team.getName()).color(ChatColor.YELLOW);
            clickToJoin.then("' or ").color(ChatColor.GOLD);
            clickToJoin.then("click here").color(ChatColor.YELLOW).command("/team join " + team.getName()).tooltip("§aJoin " + team.getName());
            clickToJoin.then(" to join.").color(ChatColor.GOLD);

            clickToJoin.send(bukkitPlayer);
        }

        team.sendMessage(ChatColor.YELLOW + UUIDUtils.name(player) + " has been invited to the team!");
    }

}

 */

