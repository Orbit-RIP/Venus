package net.frozenorb.foxtrot.team.commands;

import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamPointBreakDownCommand {

	@Command(names = { "f pointsbreakdown", "team pbr", "t pointbr", "t pbr" }, permission = "op")
	public static void teamPointBreakDown(Player player, @Parameter(name="team", defaultValue="self") final Team team) {
		player.sendMessage(ChatColor.GOLD + "Point Breakdown of " + team.getName());

		for (String info : team.getPointBreakDown()) {
			player.sendMessage(info);
		}
	}

}
