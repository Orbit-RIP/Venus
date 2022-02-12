package rip.orbit.hcteams.events.patch.listener;

import cc.fyre.proton.event.HalfHourEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.events.patch.PatchHandler;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.claims.LandBoard;
import rip.orbit.hcteams.util.CC;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 9:04 PM
 * HCTeams / rip.orbit.hcteams.events.pumpkinpatch.listener
 */
public class PatchListener implements Listener {

	public static int mined = 0;

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent event) {
		Team team = LandBoard.getInstance().getTeam(event.getBlock().getLocation());

		if (team != null) {
			if (team.getName().equals(PatchHandler.PATCH_TEAM_NAME)) {
				event.getBlock().getDrops().clear();
				for (ItemStack stack : HCF.getInstance().getPatchHandler().getLoot()) {
					event.getBlock().getDrops().add(stack);
				}
				mined++;
				if (mined >= HCF.getInstance().getPatchHandler().getLocations().size()) {
					Bukkit.broadcastMessage(PatchHandler.PATCH_PREFIX + CC.translate("&f Is now fully picked."));
				}
			}
		}
	}

	@EventHandler
	public void onHalfHour(HalfHourEvent event) {
		HCF.getInstance().getPatchHandler().respawn();
	}

}
