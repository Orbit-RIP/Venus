package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.entity.Player;

public class SmiteCommand {


	@Command(names={ "smite"}, permission="foxtrot.smite")
	public static void smite(Player sender, @Parameter(name="player", defaultValue="self") Player player) {
		player.getWorld().strikeLightning(player.getLocation());
		sender.sendMessage(CC.translate("&eYou smited {player}!".replace("{player}", player.getName())));
	}
}
