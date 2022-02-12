package rip.orbit.hcteams.events.patch.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.menu.Menu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.events.patch.PatchHandler;
import rip.orbit.hcteams.events.patch.listener.PatchListener;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.util.CC;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 8:56 PM
 * HCTeams / rip.orbit.hcteams.events.pumpkinpatch.command
 */
public class PatchCommand {

	@Command(names = {"patch"}, permission = "")
	public static void patch(CommandSender sender) {
		Team team = HCF.getInstance().getTeamHandler().getTeam(PatchHandler.PATCH_TEAM_NAME);
		sender.sendMessage(CC.translate(" "));
		sender.sendMessage(CC.translate(PatchHandler.PATCH_PREFIX + " Info"));
		sender.sendMessage(CC.translate(" "));
		String loc = "" + team.getHq().getBlockX() + ", " + team.getHq().getBlockZ();
		sender.sendMessage(CC.translate("&f" + PatchHandler.SEASONAL_NAME + " is located @ &b" + loc));
		sender.sendMessage(CC.translate("&fWorld&7: &bOverworld"));
		sender.sendMessage(CC.translate("&fPicked " + PatchHandler.SEASONAL_MINED_NAME + "s&7: &6" + PatchListener.mined + "/" + HCF.getInstance().getPatchHandler().getLocations().size()));
		sender.sendMessage(CC.translate("&fRespawns In: &b"));
		sender.sendMessage(CC.translate(" "));

	}

	@Command(names = {"patch respawn"}, permission = "op")
	public static void respawn(CommandSender sender) {

		HCF.getInstance().getPatchHandler().respawn();

		sender.sendMessage(CC.translate("&aRespawned"));
	}

	@Command(names = {"patch loot"}, permission = "")
	public static void patchLoot(Player sender) {
		new Menu() {

			@Override
			public String getTitle(Player player) {
				return CC.translate("&b" + PatchHandler.SEASONAL_NAME + " Loot");
			}

			@Override
			public Map<Integer, cc.fyre.proton.menu.Button> getButtons(Player player) {
				Map<Integer, cc.fyre.proton.menu.Button> buttons = new HashMap<>();

				int i = 0;
				for (ItemStack stack : HCF.getInstance().getPatchHandler().getLoot()) {
					buttons.put(i++, cc.fyre.proton.menu.Button.fromItem(stack));
				}

				return buttons;
			}
		};
	}

	@Command(names = "patch scan", permission = "op")
	public static void scan(CommandSender sender) {
		HCF.getInstance().getPatchHandler().scanClaim();
		sender.sendMessage(CC.translate("&aScanned claim for flowers."));
	}

}
