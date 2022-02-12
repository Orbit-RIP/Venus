package rip.orbit.hcteams.events.citadel.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.util.TimeUtils;
import com.google.common.base.Joiner;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.events.Event;
import rip.orbit.hcteams.events.citadel.CitadelHandler;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.util.JavaUtils;

import java.util.HashSet;
import java.util.Set;

public class CitadelCommand {

    // Make this pretty.
    @Command(names={ "citadel" }, permission="")
    public static void citadel(Player sender) {
        Set<ObjectId> cappers = HCF.getInstance().getCitadelHandler().getCappers();
        Set<String> capperNames = new HashSet<>();

        for (ObjectId capper : cappers) {
            Team capperTeam = HCF.getInstance().getTeamHandler().getTeam(capper);

            if (capperTeam != null) {
                capperNames.add(capperTeam.getName());
            }
        }

        if (!capperNames.isEmpty()) {
            sender.sendMessage(CitadelHandler.PREFIX + " " + ChatColor.YELLOW + "Citadel was captured by " + ChatColor.GREEN + Joiner.on(", ").join(capperNames) + ChatColor.YELLOW + "");
        } else {
            Event citadel = HCF.getInstance().getEventHandler().getEvent("Citadel");

            if (citadel != null && citadel.isActive()) {
                sender.sendMessage(CitadelHandler.PREFIX + " " + ChatColor.YELLOW + "Citadel can be captured now.");
            } else {
                sender.sendMessage(CitadelHandler.PREFIX + " " + ChatColor.YELLOW + "Citadel was not captured last week.");
            }
        }

        long duration = JavaUtils.parse("1d");
        long timeLeft = (HCF.getInstance().getCitadelHandler().getCappedAt() + duration) - System.currentTimeMillis();
        sender.sendMessage(ChatColor.GOLD + "Citadel: " + ChatColor.WHITE + "Lootable for " + (TimeUtils.formatIntoDetailedString((int) (timeLeft / 1000))) + (capperNames.isEmpty() ? "" : ", and lootable now by " + Joiner.on(", ").join(capperNames) + "."));
    }

}