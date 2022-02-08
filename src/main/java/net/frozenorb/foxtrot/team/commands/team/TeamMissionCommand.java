package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.menu.MissionMenu;
import cc.fyre.proton.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author xanderume@gmail (JavaProject)
 */
public class TeamMissionCommand {

    @Command(names = {"team mission","team missions","t mission","team missions","faction mission","f mission","faction missions","f missions"}, permission = "")
    public static void execute(Player player) {

        final Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(player);

        if (senderTeam == null) {
            player.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        new MissionMenu(senderTeam).openMenu(player);
    }

}
