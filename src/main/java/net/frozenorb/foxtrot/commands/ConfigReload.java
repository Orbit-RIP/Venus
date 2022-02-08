package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;


public class ConfigReload {
	@Command(names={ "foxtrot reload"}, permission="foxtrot.reload")
	public static void balance(Player sender) {
		Foxtrot.getInstance().reloadConfig();
		sender.sendMessage(CC.translate("&aYou reloaded the config!"));
	}
}
