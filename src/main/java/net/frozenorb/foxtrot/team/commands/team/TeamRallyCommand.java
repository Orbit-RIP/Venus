package net.frozenorb.foxtrot.team.commands.team;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TeamRallyCommand {

    @Command(names = {"team rally", "t r", "t rally", "f rally", "f r", "f tl", "t tl", "faction rally", "faction tl", "faction r", "team tl", "team r"}, permission = "")
    public static void teamRally(Player sender) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);
        LunarClientAPI api = LunarClientAPI.getInstance();

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        UUID worldUUID = sender.getWorld().getUID();
        Location rallyPoint = sender.getLocation();
        LCWaypoint oldRally = team.getRallyPoint();

        team.setRally(rallyPoint);
        team.setRallyPoint(new LCWaypoint("Rally Point",
                rallyPoint, 15073510, true, true));

        team.getOnlineMembers().forEach(member -> {
            if (oldRally != null) {
               api.removeWaypoint(member, oldRally);
            }
            api.sendWaypoint(member, team.getRallyPoint());
        });

        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> {
            team.getOnlineMembers().forEach(member -> {
                if (team.getRallyPoint() != null) {
                    api.removeWaypoint(member, team.getRallyPoint());
                }
            });
        }, (20 * 60 * 10));

        team.sendMessage(CC.translate("&3%player% has updated the team's rally point! This will last for 10 minutes").replace("%player%", sender.getName()));
    }

}
