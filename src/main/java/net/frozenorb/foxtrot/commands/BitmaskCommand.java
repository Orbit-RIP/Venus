package net.frozenorb.foxtrot.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;

public class BitmaskCommand {

    //TODO: Cleanup

    @Command(names={ "flags list", "flag list" }, permission="op")
    public static void bitmaskList(Player sender) {
        for (DTRBitmask bitmaskType : DTRBitmask.values()) {
            sender.sendMessage(ChatColor.GOLD + bitmaskType.getName() + " (" + bitmaskType.getBitmask() + "): " + ChatColor.YELLOW + bitmaskType.getDescription());
        }
    }

    @Command(names={ "flags info", "flag info" }, permission="op")
    public static void bitmaskInfo(Player sender, @Parameter(name="team") Team team) {
        if (team.getOwner() != null) {
            sender.sendMessage(ChatColor.RED + "Bitmask flags cannot be applied to teams without a null leader.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Bitmask flags of " + ChatColor.GOLD + team.getName() + ChatColor.YELLOW + ":");

        for (DTRBitmask bitmaskType : DTRBitmask.values()) {
            if (!team.hasDTRBitmask(bitmaskType)) {
                continue;
            }

            sender.sendMessage(ChatColor.GOLD + bitmaskType.getName() + " (" + bitmaskType.getBitmask() + "): " + ChatColor.YELLOW + bitmaskType.getDescription());
        }

        sender.sendMessage(ChatColor.YELLOW + "Raw DTR: " + ChatColor.YELLOW + team.getDTR());
    }

    @Command(names={ "flags add", "flag add" }, permission="op")
    public static void bitmaskAdd(Player sender, @Parameter(name="target") Team team, @Parameter(name="bitmask") DTRBitmask bitmask) {
        if (team.getOwner() != null) {
            sender.sendMessage(ChatColor.RED + "Bitmask flags cannot be applied to teams without a null leader.");
            return;
        }

        if (team.hasDTRBitmask(bitmask)) {
            sender.sendMessage(ChatColor.RED + "This claim already has the bitmask value " + bitmask.getName() + ".");
            return;
        }

        int dtrInt = (int) team.getDTR();

        dtrInt += bitmask.getBitmask();

        team.setDTR(dtrInt);
        bitmaskInfo(sender, team);
    }

    @Command(names={ "flags remove", "flag remove" }, permission="op")
    public static void bitmaskRemove(Player sender, @Parameter(name="team") Team team, @Parameter(name="bitmask") DTRBitmask bitmask) {
        if (team.getOwner() != null) {
            sender.sendMessage(ChatColor.RED + "Bitmask flags cannot be applied to teams without a null leader.");
            return;
        }

        if (!team.hasDTRBitmask(bitmask)) {
            sender.sendMessage(ChatColor.RED + "This claim doesn't have the bitmask value " + bitmask.getName() + ".");
            return;
        }

        int dtrInt = (int) team.getDTR();

        dtrInt -= bitmask.getBitmask();

        team.setDTR(dtrInt);
        bitmaskInfo(sender, team);
    }

}