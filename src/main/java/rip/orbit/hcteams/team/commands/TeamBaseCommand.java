package rip.orbit.hcteams.team.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.menu.BaseMenu;
import rip.orbit.hcteams.util.CC;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 10/07/2021 / 12:59 AM
 * HCTeams / rip.orbit.hcteams.team.commands
 */
public class TeamBaseCommand {
	@Command(names={ "team base", "t base", "f base", "faction base", "fac base" }, permission="")
	public static void base(Player sender) {
		Team team = HCF.getInstance().getTeamHandler().getTeam(sender.getUniqueId());

		if (team.getClaims().isEmpty()) {
			sender.sendMessage(CC.chat("&cYou cannot do this without a claim."));
			return;
		}

		new BaseMenu(team).openMenu(sender);
	}

	@Command(names = "removebasecooldown", permission = "foxtrot.removebasecooldown")
	public static void removecd(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name = "target")UUID target) {
		HCF.getInstance().getBaseCooldownMap().remove(target);
	}

}
