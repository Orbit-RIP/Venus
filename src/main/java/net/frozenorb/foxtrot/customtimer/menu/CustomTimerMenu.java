package net.frozenorb.foxtrot.customtimer.menu;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.customtimer.CustomTimer;
import net.frozenorb.foxtrot.scoreboard.FoxtrotScoreGetter;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
					return CC.translateLines(Arrays.asList(
							"&6Command: &f" + customTimer.getCommand(),
							"&6Time: &f" + FoxtrotScoreGetter.getTimerScore(customTimer.getTime()),
							" ",
							"&7&oClick to end this customtimer"
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
