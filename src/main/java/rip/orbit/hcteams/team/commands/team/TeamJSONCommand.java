package rip.orbit.hcteams.team.commands.team;

import cc.fyre.proton.command.Command;
import org.bukkit.command.CommandSender;
import rip.orbit.hcteams.team.Team;

public class TeamJSONCommand {

    @Command(names={ "team json", "t json", "f json", "faction json", "fac json" }, permission="op")
    public static void teamJSON(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name="team", defaultValue="self") Team team) {
        sender.sendMessage(team.toJSON().toString());
    }

}