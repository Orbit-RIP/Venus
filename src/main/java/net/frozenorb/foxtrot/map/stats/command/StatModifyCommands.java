package net.frozenorb.foxtrot.map.stats.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.map.stats.StatsEntry;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StatModifyCommands {

	@Command(names = "stats setkills", permission = "op", hidden = true)
	public static void setKills(Player player, @Parameter(name = "kills") int kills) {
		StatsEntry stats = Foxtrot.getInstance().getMapHandler().getStatsHandler().getStats(player);
		stats.setKills(kills);

		Foxtrot.getInstance().getKillsMap().setKills(player.getUniqueId(), kills);

		player.sendMessage(ChatColor.GREEN + "You've set your own kills to: " + kills);
	}

	@Command(names = "stats setdeaths", permission = "op", hidden = true)
	public static void setDeaths(Player player, @Parameter(name = "deaths") int deaths) {
		StatsEntry stats = Foxtrot.getInstance().getMapHandler().getStatsHandler().getStats(player);
		stats.setDeaths(deaths);

		Foxtrot.getInstance().getDeathsMap().setDeaths(player.getUniqueId(), deaths);

		player.sendMessage(ChatColor.GREEN + "You've set your own deaths to: " + deaths);
	}

	@Command(names = "stats setteamkills", permission = "op", hidden = true)
	public static void setTeamKills(Player player, @Parameter(name = "kills") int kills) {
		Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);

		if (team != null) {
			team.setKills(kills);
			player.sendMessage(ChatColor.GREEN + "You've set your team's kills to: " + kills);
		}
	}

	@Command(names = "stats setteamdeaths", permission = "op", hidden = true)
	public static void setTeamDeaths(Player player, @Parameter(name = "deaths") int deaths) {
		Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);

		if (team != null) {
			team.setDeaths(deaths);
			player.sendMessage(ChatColor.GREEN + "You've set your team's deaths to: " + deaths);
		}
	}

}
