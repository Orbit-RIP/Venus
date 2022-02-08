package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.commands.SOTWCommand;
import net.frozenorb.foxtrot.listener.FoxListener;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.Claim;
import cc.fyre.proton.command.Command;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TeamLockClaimCommand {

    @Command(names = {"team lockclaim", "f lockclaim", "faction lockclaim", "t lockclaim"}, permission = "", async = true)
    public static void teamLockClaim(Player sender) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);


        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You are not on a team!");
            return;
        }

        if (!(team.isOwner(sender.getUniqueId())
                || team.isCaptain(sender.getUniqueId())
                || team.isCoLeader(sender.getUniqueId()))) {
            sender.sendMessage(ChatColor.GOLD + "Only team captains can do this.");
            return;
        }

        if (!SOTWCommand.getCustomTimers().containsKey("&a&lSOTW")) {
            sender.sendMessage(ChatColor.RED + "This can only be used during SOTW.");
            return;
        }

        boolean claimLocked = !team.isClaimLocked();
        team.setClaimLocked(claimLocked);

        if (claimLocked) {
            for (Claim claim : team.getClaims()) {
                claim.getPlayers().stream()
                        .filter(player -> {
                            if (team.getMembers().contains(player.getUniqueId())) return false;
                            if (player.hasMetadata("vanished")) return false;
                            return !player.hasMetadata("modmode");
                        }).forEach(player -> {
                            FoxListener.teleport(player, true);
                            player.sendMessage(ChatColor.RED + "The claim you were just in got locked, so you were teleported outside of it.");
                        });
            }
        }

        sender.sendMessage(ChatColor.WHITE + "You have " + (claimLocked ? "" : "un")
                + "locked your faction claim.");

    }
}
