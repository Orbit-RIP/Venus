package rip.orbit.hcteams.map.stats.command;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.util.CC;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 14/08/2021 / 12:29 AM
 * HCTeams / rip.orbit.hcteams.map.stats.command
 */
public class HologramSetLocationCommand {

	@Command(names = {"killhologram setloc", "killhologram setlocation"}, permission = "foxtrot.setholo")
	public static void setLoc(Player sender) {
		String path = "kills-top-hologram";
		if (!HCF.getInstance().getConfig().contains(path)) {
			HCF.getInstance().getConfig().createSection(path);
		}
		if (!HCF.getInstance().getConfig().contains(path + ".x")) {
			HCF.getInstance().getConfig().createSection(path + ".x");
		}
		if (!HCF.getInstance().getConfig().contains(path + ".y")) {
			HCF.getInstance().getConfig().createSection(path + ".y");
		}
		if (!HCF.getInstance().getConfig().contains(path + ".z")) {
			HCF.getInstance().getConfig().createSection(path + ".z");
		}
		if (!HCF.getInstance().getConfig().contains(path + ".world")) {
			HCF.getInstance().getConfig().createSection(path + ".world");
		}
		HCF.getInstance().getConfig().set(path + ".x", sender.getLocation().getX());
		HCF.getInstance().getConfig().set(path + ".y", sender.getLocation().getY());
		HCF.getInstance().getConfig().set(path + ".z", sender.getLocation().getZ());
		HCF.getInstance().getConfig().set(path + ".world", sender.getLocation().getWorld().getName());
		HCF.getInstance().saveConfig();
		sender.sendMessage(CC.translate("&aKill Top hologram successfully set."));

	}

	@Command(names = {"teamhologram setloc", "teamhologram setlocation"}, permission = "foxtrot.setholo")
	public static void setTeamLoc(Player sender) {
		String path = "team-top-hologram";
		if (!HCF.getInstance().getConfig().contains(path)) {
			HCF.getInstance().getConfig().createSection(path);
		}
		if (!HCF.getInstance().getConfig().contains(path + ".x")) {
			HCF.getInstance().getConfig().createSection(path + ".x");
		}
		if (!HCF.getInstance().getConfig().contains(path + ".y")) {
			HCF.getInstance().getConfig().createSection(path + ".y");
		}
		if (!HCF.getInstance().getConfig().contains(path + ".z")) {
			HCF.getInstance().getConfig().createSection(path + ".z");
		}
		if (!HCF.getInstance().getConfig().contains(path + ".world")) {
			HCF.getInstance().getConfig().createSection(path + ".world");
		}
		HCF.getInstance().getConfig().set(path + ".x", sender.getLocation().getX());
		HCF.getInstance().getConfig().set(path + ".y", sender.getLocation().getY());
		HCF.getInstance().getConfig().set(path + ".z", sender.getLocation().getZ());
		HCF.getInstance().getConfig().set(path + ".world", sender.getLocation().getWorld().getName());
		HCF.getInstance().saveConfig();
		sender.sendMessage(CC.translate("&aTeam Top hologram successfully set."));

	}
}
