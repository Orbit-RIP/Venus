package net.frozenorb.foxtrot.events.region.oremtn.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.region.oremtn.OreHandler;
import net.frozenorb.foxtrot.events.region.oremtn.OreMountain;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.util.ColorUtil;
import cc.fyre.proton.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class OreCommand {

    @Command(names = "ore scan", permission = "op")
    public static void oreScan(Player sender) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(OreHandler.getOreTeamName());

        // Make sure we have a team
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You must first create the team (" + OreHandler.getOreTeamName() + ") and claim it!");
            return;
        }

        // Make sure said team has a claim
        if (team.getClaims().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "You must claim land for '" + OreHandler.getOreTeamName()+ "' before scanning it!");
            return;
        }

        // We have a claim, and a team, now do we have a glowstone?
        if (!Foxtrot.getInstance().getOreHandler().hasOreMountain()) {
            OreHandler.setOreMountain(new OreMountain());
        }

        //githubh
        // We have a glowstone now, we're gonna scan and save the area
        OreHandler.getOreMountain().scan();
        Foxtrot.getInstance().getOreHandler().save(); // save to file :D

        sender.sendMessage(GREEN + "[Ore Mountain] Scanned all ores and saved ore mountain to file!");
    }

    @Command(names = "ore reset", permission = "op")
    public static void oreReset(Player sender) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(OreHandler.getOreTeamName());

        // Make sure we have a team, claims, and a mountain!
        if (team == null || team.getClaims().isEmpty() || !Foxtrot.getInstance().getOreHandler().hasOreMountain()) {
            sender.sendMessage(RED + "Create the team '" + OreHandler.getOreTeamName() + "', then make a claim for it, finally scan it! (/glow scan)");
            return;
        }

        // Check, check, check, LIFT OFF! (reset the mountain)
        OreHandler.getOreMountain().reset();

        Bukkit.broadcastMessage(ColorUtil.format("&9&lOre Mountain &ehas just been reset."));
    }
}
