package net.frozenorb.foxtrot.team.commands;

import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.TeamHandler;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PowerFactionCommand {

    @Command(names={ "powerfac add", "team powerfac add", "pfac add"}, permission="foxtrot.powerfactions")
    public static void powerFactionAdd(Player sender, @Parameter(name="team") Team team) {
        team.setPowerFaction(true);
        sender.sendMessage(ChatColor.YELLOW + team.getName() + ChatColor.WHITE + " is now a power faction!");
    }

    @Command(names={"team powerfac remove", "pfac remove", "powerfac remove" }, permission="foxtrot.powerfactions")
    public static void powerFactionRemove(Player sender, @Parameter(name="team") Team team) {
        team.setPowerFaction(false);
        sender.sendMessage(ChatColor.YELLOW + team.getName() + ChatColor.WHITE + " is no longer a power faction!");
    }

    @Command(names={ "powerfac list", "team powerfac list", "pfac list"}, permission="foxtrot.powerfactions")
    public static void powerFactionList(Player sender) {
        sender.sendMessage(ChatColor.WHITE + "Found " + ChatColor.GOLD + TeamHandler.getPowerFactions().size() + ChatColor.YELLOW + " Power Factions.");
        int i = 1;
        for( Team t : TeamHandler.getPowerFactions() ) {
            sender.sendMessage(ChatColor.YELLOW + "" + i + ". " + ChatColor.YELLOW + t.getName());
            i++;
        }
    }
}