package net.frozenorb.foxtrot.team.commands;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.NoFactionFocusFaction;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.nametag.FrozenNametagHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FocusCommand {

    @Command(names = {"focus"}, permission = "")
    public static void focus(Player sender, @Parameter(name = "player") Player target) {
        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(sender);
        Team targetTeam = Foxtrot.getInstance().getTeamHandler().getTeam(target);

        if (senderTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }


        // There's a few ways this can go:
        // a. Target's team is null, in which case they can be targeted.
        // b. Target's team is not null, in which case...
        //      1. The teams are equal, where they can't be targeted.
        //      2. They aren't equal, in which case they can be targeted.
        // This if statement really isn't as complex as the above
        // comment made it sound, but it took me a few minutes of
        // thinking through, so this is just to save time.
        if (senderTeam == targetTeam) {
            sender.sendMessage(ChatColor.RED + "You cannot target a player on your team.");
            return;
        }

        senderTeam.setFocused(target.getUniqueId());
        senderTeam.sendMessage(ChatColor.LIGHT_PURPLE + target.getName() + ChatColor.YELLOW + " has been focused by " + ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.YELLOW + ".");

        for (Player onlinePlayer : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
            if (senderTeam.isMember(onlinePlayer.getUniqueId())) {
                FrozenNametagHandler.reloadOthersFor(onlinePlayer);
            }
        }
    }

    @Command(names = {"unfocus"}, permission = "")
    public static void unFocus(Player sender) {
        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (senderTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "You have to be in a faction to unfocus an individual player!");
            return;
        }

        if (senderTeam.getFocused() == null) {
            sender.sendMessage(ChatColor.RED + "No one is currently focused.");
            return;
        }

        senderTeam.setFocused(null);
        senderTeam.sendMessage(ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + " has unfocused the focused player.");
        for (Player onlinePlayer : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
            if (senderTeam.isMember(onlinePlayer.getUniqueId())) {
                FrozenNametagHandler.reloadOthersFor(onlinePlayer);
            }
        }
    }

    @Command(names = {"f f", "faction f", "faction focus", "t f", "team f", "team focus", "f focus"}, permission = "")
    public static void factionFocus(Player sender, @Parameter(name = "player") Player target) {
        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(sender);
        Team targetTeam = Foxtrot.getInstance().getTeamHandler().getTeam(target);


        // There's a few ways this can go:
        // a. Target's team is null, in which case they can be targeted.
        // b. Target's team is not null, in which case...
        //      1. The teams are equal, where they can't be targeted.
        //      2. They aren't equal, in which case they can be targeted.
        // This if statement really isn't as complex as the above
        // comment made it sound, but it took me a few minutes of
        // thinking through, so this is just to save time.
        if (senderTeam != null && senderTeam == targetTeam) {
            sender.sendMessage(ChatColor.RED + "You cannot target a player on your team.");
            return;
        }


        if (senderTeam == null && targetTeam == null) {
            sender.sendMessage(ChatColor.RED + "Target is not in a faciton.");
            return;
        }

        if (senderTeam != null && targetTeam == null) {
            focus(sender, target);
            return;
        }

        if (senderTeam == null) {
            Foxtrot.getInstance().getNoFactionFocusFaction().setPlayerNoFactionTeamMap(sender.getUniqueId(), targetTeam);
            sender.sendMessage(ChatColor.YELLOW + targetTeam.getName() + ChatColor.WHITE + " has been focused" + ChatColor.WHITE + ".");
        } else {
            LunarClientAPI api = LunarClientAPI.getInstance();
            senderTeam.setFocusedTeam(targetTeam);
            senderTeam.sendMessage(ChatColor.YELLOW + targetTeam.getName() + ChatColor.WHITE + " has been focused by " + ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + ".");
            if (targetTeam.getHQ() == null) {
                return;
            }
            LCWaypoint focusedHQ = new LCWaypoint(String.format("%s's HQ", targetTeam.getName()), targetTeam.getHQ(), 13369344, true, true);
            senderTeam.getOnlineMembers().forEach(member -> {
                if (senderTeam.getFocusedWaypoint() != null) {
                    api.removeWaypoint(member, senderTeam.getFocusedWaypoint());
                }
                api.sendWaypoint(member, focusedHQ);
            });
            senderTeam.setFocusedWaypoint(focusedHQ);
        }


        for (Player onlinePlayer : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
            if (senderTeam != null && senderTeam.isMember(onlinePlayer.getUniqueId())) {
                FrozenNametagHandler.reloadOthersFor(onlinePlayer);
            }

            if (senderTeam == null) {
                FrozenNametagHandler.reloadPlayer(sender);
                FrozenNametagHandler.reloadOthersFor(sender);
            }
        }
    }

    @Command(names = {"f uf", "faction uf", "faction unfocus", "t uf", "team uf", "team ufnocus", "f unfocus"}, permission = "")
    public static void unfocusFaction(Player sender) {
        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        // There's a few ways this can go:
        // a. Target's team is null, in which case they can be targeted.
        // b. Target's team is not null, in which case...
        //      1. The teams are equal, where they can't be targeted.
        //      2. They aren't equal, in which case they can be targeted.
        // This if statement really isn't as complex as the above
        // comment made it sound, but it took me a few minutes of
        // thinking through, so this is just to save time.


        if (senderTeam == null) {
            if (Foxtrot.getInstance().getNoFactionFocusFaction().getPlayerNoFactionTeamMap().get(sender.getUniqueId()) == null) {
                sender.sendMessage(ChatColor.RED + "No faction is currently focused.");
                return;
            }
            Foxtrot.getInstance().getNoFactionFocusFaction().getPlayerNoFactionTeamMap().remove(sender.getUniqueId());
            sender.sendMessage(ChatColor.YELLOW + "Unfocused focused faction.");
        } else {
            if (senderTeam.getFocusedTeam() == null) {
                sender.sendMessage(ChatColor.RED + "No faction is currently focused.");
                return;
            }

            LunarClientAPI api = LunarClientAPI.getInstance();
            if (senderTeam.getFocusedTeam().getHQ() == null) {
                senderTeam.setFocusedTeam(null);
                return;
            }
                senderTeam.getOnlineMembers().forEach(member -> {
                    api.removeWaypoint(member, senderTeam.getFocusedWaypoint());
                });
                senderTeam.setFocusedTeam(null);
                senderTeam.sendMessage(ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + " has unfocused the focused faction.");
            }


            for (Player onlinePlayer : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
                if (senderTeam != null && senderTeam.isMember(onlinePlayer.getUniqueId())) {
                    FrozenNametagHandler.reloadOthersFor(onlinePlayer);
                }

                if (senderTeam == null) {
                    FrozenNametagHandler.reloadPlayer(sender);
                    FrozenNametagHandler.reloadOthersFor(sender);
                }
            }
        }
    }


