package rip.orbit.hcteams.team.commands.team;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.team.Team;

public class TeamInfoCommand {

    @Command(names={ "team info", "t info", "f info", "faction info", "fac info", "team who", "t who", "f who", "faction who", "fac who", "team show", "t show", "f show", "faction show", "fac show", "team i", "t i", "f i", "faction i", "fac i" }, permission="")
    public static void teamInfo(Player sender, @cc.fyre.proton.command.param.Parameter(name="team", defaultValue="self", tabCompleteFlags={ "noteams", "players" }) Team team) {
        new BukkitRunnable() {

            @Override
            public void run() {
                Team exactPlayerTeam = HCF.getInstance().getTeamHandler().getTeam(UUIDUtils.uuid(team.getName()));

                if (exactPlayerTeam != null && exactPlayerTeam != team) {
                    exactPlayerTeam.sendTeamInfo(sender);
                }

                team.sendTeamInfo(sender);

            }

        }.runTaskAsynchronously(HCF.getInstance());
    }

}