package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.nametag.FrozenNametagHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamAllyCommand {

    @Command(names={ "team ally", "t ally", "f ally", "faction ally", "fac ally" }, permission="")
    public static void teamAlly(Player sender, @Parameter(name="team") Team team) {
        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (senderTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (!(senderTeam.isOwner(sender.getUniqueId()) || senderTeam.isCaptain(sender.getUniqueId()) || senderTeam.isCoLeader(sender.getUniqueId()))) {
            sender.sendMessage(ChatColor.RED + "Only team captains can do this.");
            return;
        }

        if (senderTeam.equals(team)) {
            sender.sendMessage(ChatColor.RED + "You cannot ally your own team!");
            return;
        }

        if (senderTeam.getAllies().size() >= Foxtrot.getInstance().getMapHandler().getAllyLimit()) {
            sender.sendMessage(ChatColor.RED + "Your team already has the max number of allies, which is " + Foxtrot.getInstance().getMapHandler().getAllyLimit() + ".");
            return;
        }

        if (team.getAllies().size() >= Foxtrot.getInstance().getMapHandler().getAllyLimit()) {
            sender.sendMessage(ChatColor.RED + "The team you're trying to ally already has the max number of allies, which is " + Foxtrot.getInstance().getMapHandler().getAllyLimit() + ".");
            return;
        }

        if (senderTeam.isAlly(team)) {
            sender.sendMessage(ChatColor.RED + "You're already allied to " + team.getName(sender) + ChatColor.YELLOW + ".");
            return;
        }

        if (senderTeam.getRequestedAllies().contains(team.getUniqueId())) {
            senderTeam.getRequestedAllies().remove(team.getUniqueId());

            team.getAllies().add(senderTeam.getUniqueId());
            senderTeam.getAllies().add(team.getUniqueId());

            team.flagForSave();
            senderTeam.flagForSave();

            for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
                if (team.isMember(player.getUniqueId())) {
                    player.sendMessage(senderTeam.getName(player) + ChatColor.WHITE + " has accepted your request to ally. You now have " + Team.ALLY_COLOR + team.getAllies().size() + "/" + Foxtrot.getInstance().getMapHandler().getAllyLimit() + " allies" + ChatColor.YELLOW + ".");
                } else if (senderTeam.isMember(player.getUniqueId())) {
                    player.sendMessage(ChatColor.WHITE + "Your team has allied " + team.getName(sender) + ChatColor.WHITE + ". You now have " + Team.ALLY_COLOR + senderTeam.getAllies().size() + "/" + Foxtrot.getInstance().getMapHandler().getAllyLimit() + " allies" + ChatColor.YELLOW + ".");
                }

                if (team.isMember(player.getUniqueId()) || senderTeam.isMember(player.getUniqueId())) {
                    FrozenNametagHandler.reloadPlayer(sender);
                    FrozenNametagHandler.reloadOthersFor(sender);
                }
            }
        } else {
            if (team.getRequestedAllies().contains(senderTeam.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "You have already requested to ally " + team.getName(sender) + ChatColor.YELLOW + ".");
                return;
            }

            team.getRequestedAllies().add(senderTeam.getUniqueId());
            team.flagForSave();

            for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
                if (team.isMember(player.getUniqueId())) {
                    player.sendMessage(senderTeam.getName(player.getPlayer()) + ChatColor.WHITE + " has requested to be your ally. Type " + Team.ALLY_COLOR + "/team ally " + senderTeam.getName() + ChatColor.WHITE + " to accept.");
                } else if (senderTeam.isMember(player.getUniqueId())) {
                    player.sendMessage(ChatColor.WHITE + "Your team has requested to ally " + ChatColor.YELLOW + team.getName(player) + ChatColor.WHITE + ".");
                }
            }
        }
    }

}