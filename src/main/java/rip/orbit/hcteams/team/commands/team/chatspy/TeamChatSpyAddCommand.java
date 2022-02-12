package rip.orbit.hcteams.team.commands.team.chatspy;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.team.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamChatSpyAddCommand {

    @Command(names={ "team chatspy add", "t chatspy add", "f chatspy add", "faction chatspy add", "fac chatspy add" }, permission="foxtrot.chatspy")
    public static void teamChatSpyAdd(Player sender, @cc.fyre.proton.command.param.Parameter(name="team") Team team) {
        if (HCF.getInstance().getChatSpyMap().getChatSpy(sender.getUniqueId()).contains(team.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You are already spying on " + team.getName() + ".");
            return;
        }

        List<ObjectId> teams = new ArrayList<>(HCF.getInstance().getChatSpyMap().getChatSpy(sender.getUniqueId()));

        teams.add(team.getUniqueId());

        HCF.getInstance().getChatSpyMap().setChatSpy(sender.getUniqueId(), teams);
        sender.sendMessage(ChatColor.GREEN + "You are now spying on the chat of " + ChatColor.YELLOW + team.getName() + ChatColor.GREEN + ".");
    }

}