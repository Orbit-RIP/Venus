package net.frozenorb.foxtrot.customtimer.command;

import net.frozenorb.foxtrot.commands.SOTWCommand;
import net.frozenorb.foxtrot.customtimer.CustomTimer;
import net.frozenorb.foxtrot.customtimer.menu.CustomTimerMenu;
import net.frozenorb.foxtrot.scoreboard.FoxtrotScoreGetter;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.JavaUtils;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CustomTimerCommand {

	@Command(names = "customtimer create", permission = "foxtrot.customtimer")
	public static void create(CommandSender sender, @Parameter(name = "name") String name, @Parameter(name = "time") String time, @Parameter(name = "command", wildcard = true) String command) {
		long now = System.currentTimeMillis();
		long longTime = JavaUtils.parse(time) + System.currentTimeMillis();
		CustomTimer customTimer = new CustomTimer(name.replace("-", " "), command, longTime);
		CustomTimer.customTimers.add(customTimer);
	}

	@Command(names = "startsotwtimers", permission = "op")
	public static void startsotwtimers(CommandSender sender) {
		create(sender, "&d&lPACKAGE-ALL", "1h", "package giveall 3");
		create(sender, "&C&lKEY-ALL", "2h", "cr giveallkey SOTW 3");
		create(sender, "&e&lAIRDROP-ALL", "3h", "airdrop giveall 2");
		create(sender, "&A&LFLASH-SALE", "6h", "");
		SOTWCommand.sotwStart(sender, "2h");
	}

	@Command(names = "customtimer delete", permission = "foxtrot.customtimer")
	public static void delete(CommandSender sender, @Parameter(name = "name") String name) {
		CustomTimer customTimer = CustomTimer.byName(name.replace("-", " "));
		CustomTimer.customTimers.remove(customTimer);
		sender.sendMessage(CC.translate("&cDeleted " + name + " customtimer."));
	}

	@Command(names = "customtimer list", permission = "foxtrot.customtimer")
	public static void list(CommandSender sender) {
//		sender.sendMessage(CC.translate("&6&lActive Custom Timer List"));
		if (CustomTimer.customTimers.isEmpty()) {
			sender.sendMessage(CC.translate("&cNone"));
		} else {
			if (sender instanceof Player) {
				new CustomTimerMenu().openMenu((Player) sender);
			} else {
			CustomTimer.customTimers.forEach(customTimer -> {
				sender.sendMessage(CC.translate("&7 - " + customTimer.getName() + " (" + FoxtrotScoreGetter.getTimerScore(customTimer.getTime()) + ") {" + customTimer.getCommand() + "}"));
			});
			}
		}
	}

}
