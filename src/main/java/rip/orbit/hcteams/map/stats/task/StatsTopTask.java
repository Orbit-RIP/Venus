package rip.orbit.hcteams.map.stats.task;

import cc.fyre.proton.Proton;
import cc.fyre.proton.hologram.builder.HologramBuilder;
import cc.fyre.proton.hologram.construct.Hologram;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.stats.StatsEntry;
import rip.orbit.hcteams.map.stats.command.StatsTopCommand;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.util.CC;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static rip.orbit.hcteams.team.commands.team.TeamTopCommand.getSortedTeams;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 14/08/2021 / 12:19 AM
 * HCTeams / rip.orbit.hcteams.map.stats.task
 */
public class StatsTopTask {

	@Getter @Setter
	private static Hologram killHologram;
	@Getter @Setter
	private static Hologram teamHologram;

	public static void updateHolograms() {

		if (killHologram != null) {
			killHologram.delete();
		}
		if (teamHologram != null) {
			teamHologram.delete();
		}

		String killPath = "kills-top-hologram";
		String teamPath = "team-top-hologram";

		double killX = HCF.getInstance().getConfig().getDouble(killPath + ".x");
		double killY = HCF.getInstance().getConfig().getDouble(killPath + ".y");
		double killZ = HCF.getInstance().getConfig().getDouble(killPath + ".z");
		World killWorld = Bukkit.getWorld(HCF.getInstance().getConfig().getString(killPath + ".world"));

		double teamX = HCF.getInstance().getConfig().getDouble(teamPath + ".x");
		double teamY = HCF.getInstance().getConfig().getDouble(teamPath + ".y");
		double teamZ = HCF.getInstance().getConfig().getDouble(teamPath + ".z");
		World teamWorld = Bukkit.getWorld(HCF.getInstance().getConfig().getString(teamPath + ".world"));

		Location killLoc = new Location(killWorld, killX, killY, killZ);
		Location teamLoc = new Location(teamWorld, teamX, teamY, teamZ);

		HologramBuilder killHolo = Proton.getInstance().getHologramHandler().createHologram();
		HologramBuilder teamHolo = Proton.getInstance().getHologramHandler().createHologram();

		if (true) {

			killHolo.addLines(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 32));
			killHolo.addLines(CC.translate("&6&lTop Kills"));
			killHolo.addLines(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 32));

			List<Profile> added = new ArrayList<>();

			double index = 0;
			for (Map.Entry<StatsEntry, String> entry : HCF.getInstance().getMapHandler().getStatsHandler().getLeaderboards(StatsTopCommand.StatsObjective.KILLS, 10).entrySet()) {
				if (entry.getKey().getKills() == 0)
					continue;
				if (index >= 10.0) {
					break;
				}
				index = index + 1;

				Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(entry.getKey().getOwner(), true);

				killHolo.addLines(CC.translate("&7" + ((int)index) + ") &f" + profile.getFancyName() + "&7: &6" + entry.getKey().getKills()));
				added.add(profile);
			}
			for (int i = 0; i < (10 - added.size()); i++) {
				++index;
				killHolo.addLines(CC.translate("&7" + ((int)index) + ") &fUnknown..."));
			}

			killHolo.addLines(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 32));

		}

		if (true) {
			LinkedHashMap<Team, Integer> sortedTeamPlayerCount = getSortedTeams();

			double index = 0;

			teamHolo.addLines(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 32));
			teamHolo.addLines(ChatColor.GOLD + ChatColor.BOLD.toString() + "Top Teams");
			teamHolo.addLines(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 32));

			List<Team> added = new ArrayList<>();

			for (Map.Entry<Team, Integer> teamEntry : sortedTeamPlayerCount.entrySet()) {

				if (teamEntry.getKey().getOwner() == null) {
					continue;
				}

				index = index + 1;

				if (index >= 10.0) {
					break;
				}

				Team team = teamEntry.getKey();

				teamHolo.addLines(CC.translate("&7" + ((int)index) + ") &f" + team.getName() + "&7: &6" + teamEntry.getValue()));
				added.add(team);
			}
			for (int i = 0; i < (10 - added.size()); i++) {
				++index;
				teamHolo.addLines(CC.translate("&7" + ((int)index) + ") &fUnknown..."));
			}

			teamHolo.addLines(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 32));
		}

		killHolo.at(killLoc);
		teamHolo.at(teamLoc);
		setKillHologram(killHolo.build());
		setTeamHologram(teamHolo.build());

	}

}
