package rip.orbit.hcteams.ability.generator.menu.button;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.util.TimeUtils;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.ability.generator.Generator;
import rip.orbit.hcteams.util.CC;
import rip.orbit.hcteams.util.JavaUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 14/08/2021 / 4:42 AM
 * HCTeams / rip.orbit.hcteams.ability.generator.menu.button
 */

@AllArgsConstructor
public class SummaryButton extends Button {

	private final Generator generator;

	@Override
	public String getName(Player player) {
		return CC.translate("&eGenerator Information");
	}

	@Override
	public List<String> getDescription(Player player) {
		long time = JavaUtils.parse(generator.getDelay() + "m");
		return CC.translate(Arrays.asList(
				" ",
				"&eStatistics",
				"&6&l┃ &fTier&7: &e" + this.generator.getTier(),
				"&6&l┃ &fLevel&7: &e" + this.generator.getLevel(),
				"&6&l┃ &fSpawn Time&7: &e" + TimeUtils.formatIntoMMSS((int) (((generator.getLastSpawn() + time) - System.currentTimeMillis()) / 1000)),
				"&6&l┃ &fDelay Per Ability&7: &e" + this.generator.getDelay() + " Minutes",
				" ",
				"&eInformation",
				"&6&l┃ &fUpgrade the levels of this",
				"&6&l┃ &fby using our second currency",
				"&6&l┃ &f'stars'.",
				"",
				"&7&oTIP: 1 Level = +1 Ability Per Generation",
				" "
		));
	}

	@Override
	public Material getMaterial(Player player) {
		return Material.PAINTING;
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType) {
	}
}
