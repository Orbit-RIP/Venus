package rip.orbit.hcteams.map.stats.command;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.stats.StatsEntry;
import rip.orbit.hcteams.team.Team;

public class StatModifyCommands {

	@Command(names = "sm setkills", permission = "op")
	public static void setKills(Player player, @cc.fyre.proton.command.param.Parameter(name = "kills") int kills) {

		StatsEntry stats = HCF.getInstance().getMapHandler().getStatsHandler().getStats(player);
		stats.setKills(kills);

		player.sendMessage(ChatColor.GREEN + "You've set your own kills to: " + kills);
	}

	@Command(names = "sm setdeaths", permission = "op")
	public static void setDeaths(Player player, @cc.fyre.proton.command.param.Parameter(name = "deaths") int deaths) {

		StatsEntry stats = HCF.getInstance().getMapHandler().getStatsHandler().getStats(player);
		stats.setDeaths(deaths);

		player.sendMessage(ChatColor.GREEN + "You've set your own deaths to: " + deaths);
	}

	@Command(names = "sm setteamkills", permission = "op")
	public static void setTeamKills(Player player, @cc.fyre.proton.command.param.Parameter(name = "kills") int kills) {

		Team team = HCF.getInstance().getTeamHandler().getTeam(player);

		if (team != null) {
			team.setKills(kills);
			player.sendMessage(ChatColor.GREEN + "You've set your team's kills to: " + kills);
		}
	}

	@Command(names = "sm setteamdeaths", permission = "op")
	public static void setTeamDeaths(Player player, @cc.fyre.proton.command.param.Parameter(name = "deaths") int deaths) {
		Team team = HCF.getInstance().getTeamHandler().getTeam(player);

		if (team != null) {
			team.setDeaths(deaths);
			player.sendMessage(ChatColor.GREEN + "You've set your team's deaths to: " + deaths);
		}
	}

}
