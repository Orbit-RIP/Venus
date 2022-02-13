package rip.orbit.hcteams.customtimer.menu;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.customtimer.CustomTimer;
import rip.orbit.hcteams.scoreboard.FoxtrotScoreGetter;
import rip.orbit.hcteams.util.CC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 19/07/2021 / 11:18 PM
 * HCTeams / rip.orbit.hcteams.customtimer.menu
 */
public class CustomTimerMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "Custom Timer List";
	}

	@Override
	public boolean isAutoUpdate() {
		return true;
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		int i = 0;
		for (CustomTimer customTimer : CustomTimer.customTimers) {
			buttons.put(i, new Button() {
				@Override
				public String getName(Player player) {
					return customTimer.getName();
				}

				@Override
				public List<String> getDescription(Player player) {
					return CC.translate(Arrays.asList(
							"&6Command: &f" + customTimer.getCommand(),
							"&6Time: &f" + FoxtrotScoreGetter.getTimerScore(customTimer.getTime()),
							" ",
							"&7&oClick to end this custom timer"
					));
				}

				@Override
				public Material getMaterial(Player player) {
					return Material.PAINTING;
				}

				@Override
				public void clicked(Player player, int slot, ClickType clickType) {
					CustomTimer.customTimers.remove(customTimer);
				}
			});
			++i;
		}
		return buttons;
	}

}
