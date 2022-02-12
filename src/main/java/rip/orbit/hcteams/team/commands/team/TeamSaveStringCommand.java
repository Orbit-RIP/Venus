package rip.orbit.hcteams.team.commands.team;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.orbit.hcteams.team.Team;

public class TeamSaveStringCommand {

    @Command(names={ "team savestring", "t savestring", "f savestring", "faction savestring", "fac savestring" }, permission="op")
    public static void teamSaveString(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name="team", defaultValue="self") Team team) {
        String saveString = team.saveString(false);

        sender.sendMessage(ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "Save String (" + team.getName() + ")");
        sender.sendMessage("");

        for (String line : saveString.split("\n")) {
            sender.sendMessage(ChatColor.BLUE + line.substring(0, line.indexOf(":")) + ": " + ChatColor.YELLOW + line.substring(line.indexOf(":") + 1).replace(",", ChatColor.BLUE + "," + ChatColor.YELLOW).replace(":", ChatColor.BLUE + ":" + ChatColor.YELLOW));
        }
    }

}