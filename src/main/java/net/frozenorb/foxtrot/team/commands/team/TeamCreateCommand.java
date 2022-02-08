package net.frozenorb.foxtrot.team.commands.team;

import cc.fyre.proton.command.param.filter.NormalFilter;
import cc.fyre.proton.command.processor.Type;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.commands.EOTWCommand;
import net.frozenorb.foxtrot.scoreboard.ScoreFunction;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.track.TeamActionTracker;
import net.frozenorb.foxtrot.team.track.TeamActionType;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;

import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.regex.Pattern;

public class TeamCreateCommand {

    public static final Pattern ALPHA_NUMERIC = Pattern.compile("[^a-zA-Z0-9]");
    private static final Set<String> disallowedTeamNames = ImmutableSet.of("list", "Glowstone");

    @Command(names = {"team create", "t create", "f create", "faction create", "fac create"}, permission = "")
    public static void teamCreate(Player sender, @Parameter(name = "team") @Type(NormalFilter.class ) String team) {
        if (Foxtrot.getInstance().getTeamHandler().getTeam(sender) != null) {
            sender.sendMessage(ChatColor.GRAY + "You're already in a team!");
            return;
        }

        if (team.length() > 16) {
            sender.sendMessage(ChatColor.RED + "Maximum team name size is 16 characters!");
            return;
        }

        if (team.length() < 3) {
            sender.sendMessage(ChatColor.RED + "Minimum team name size is 3 characters!");
            return;
        }

        if (team.contains("self")){
            return;
        }

        if (team.contains("Adolf Hitler")|| team.contains("Hitler") || team.contains("nigga") || team.contains("nigger")){
            sender.sendMessage(ChatColor.RED + "You are not allowed to create team names that include racism");
            return;
        }

        if (team.contains("EOTW") || team.contains("self") && !(sender.isOp())) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to create team names with the name you chose.");
            return;
        }

        if (Foxtrot.getInstance().getTeamHandler().getTeam(team) != null) {
            sender.sendMessage(ChatColor.GRAY + "That team already exists!");
            return;
        }

        if (ALPHA_NUMERIC.matcher(team).find()) {
            sender.sendMessage(ChatColor.RED + "Team names must be alphanumeric!");
            return;
        }

        if (EOTWCommand.realFFAStarted()) {
            sender.sendMessage(ChatColor.RED + "You can't create teams during FFA.");
            return;
        }

        if (Foxtrot.getInstance().getFactionDelayMap().has(sender.getUniqueId())) {
            if (System.currentTimeMillis() - Foxtrot.getInstance().getFactionDelayMap().getDelay(sender.getUniqueId()) < 30000) {
                sender.sendMessage(ChatColor.RED + "You still have " + ScoreFunction.TIME_SIMPLE.apply((float) (System.currentTimeMillis() - Foxtrot.getInstance().getFactionDelayMap().getDelay(sender.getUniqueId())) / 1000) + " on your faction create cooldown.");
                return;
            }
        }

        // sender.sendMessage(ChatColor.GOLD + "Team Created!");
        sender.sendMessage(ChatColor.GRAY + "To learn more about teams, do /team");

        Team createdTeam = new Team(team);

        TeamActionTracker.logActionAsync(createdTeam, TeamActionType.PLAYER_CREATE_TEAM, ImmutableMap.of(
                "playerId", sender.getUniqueId(),
                "playerName", sender.getName()
        ));

        createdTeam.setUniqueId(new ObjectId());
        createdTeam.setOwner(sender.getUniqueId());
        createdTeam.setName(team);
        createdTeam.setDTR(1);

        Foxtrot.getInstance().getTeamHandler().setupTeam(createdTeam);

        Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.WHITE + "Team " + ChatColor.YELLOW + createdTeam.getName() + ChatColor.WHITE + " has been " + ChatColor.GREEN + "created" + ChatColor.WHITE + " by " + CC.getColor(sender));
        Foxtrot.getInstance().getFactionDelayMap().addCooldown(sender.getUniqueId());
    }

}