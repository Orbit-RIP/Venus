package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.util.CC;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class FactionWarListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/alts AlfieYT") && event.getMessage().equalsIgnoreCase("/alts treybles")) {
            event.getPlayer().sendMessage(CC.RED + "No permission to execute this command!");
            event.setCancelled(true);
        }
        if (!event.getPlayer().isOp() && event.getMessage().contains("kit")) {
            Team team = LandBoard.getInstance().getTeam(event.getPlayer().getLocation());
            if (SpawnTagHandler.isTagged(event.getPlayer())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "This command cannot be used in combat!");

                if (team != null && team.getName().equalsIgnoreCase("War")) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "This command cannot be used in this claim.");
                }
                if (team != null && team.getName().equalsIgnoreCase("Citadel")) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "This command cannot be used in this claim.");
                }
            }
        }
        if(event.getMessage().equalsIgnoreCase("/fr")) {
            Team team = LandBoard.getInstance().getTeam(event.getPlayer().getLocation());

            if (team != null && team.getName().equalsIgnoreCase("War")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "This command cannot be used in this claim.");
            }
        }
        if(event.getMessage().equalsIgnoreCase("/fres")) {
            Team team = LandBoard.getInstance().getTeam(event.getPlayer().getLocation());

            if (team != null && team.getName().equalsIgnoreCase("War")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "This command cannot be used in this claim.");
            }
        }
        if(event.getMessage().equalsIgnoreCase("/invis")) {
            Team team = LandBoard.getInstance().getTeam(event.getPlayer().getLocation());

            if (team != null && team.getName().equalsIgnoreCase("War")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "This command cannot be used in this claim.");
            }
        }
    }
}
