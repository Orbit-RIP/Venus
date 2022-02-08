package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BlockWordCommand {

	@Command(names = {"BlockWord add"}, permission = "foxtrot.blockword")
	public static void BlockWordCommand(CommandSender sender, @Parameter(name="word") String string) {
		if (Foxtrot.getInstance().getConfig().getStringList("Filter").contains(string.toLowerCase())) {
			sender.sendMessage(ChatColor.RED + "That word is already blocked.");
			return;
		}
		List<String> list = Foxtrot.getInstance().getConfig().getStringList("Filter");
		list.add(string.toLowerCase());
		Foxtrot.getInstance().getConfig().set("Filter", list);
		Foxtrot.getInstance().saveConfig();

		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&aYou have successfully added &f" + string.toLowerCase()
						+ " &ato the list of filter words."));
	}
	@Command(names = {"BlockWord list"}, permission = "foxtrot.blockword")
	public static void BlockWordListCommand(CommandSender sender) {
		List<String> list = Foxtrot.getInstance().getConfig().getStringList("Filter");
		sender.sendMessage(CC.translate("&cList of filter words"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', StringUtils.join(list, ", ")));
	}
	@Command(names = {"BlockWord remove"}, permission = "foxtrot.blockword")
	public static void BlockWordRemoveCommand(CommandSender sender, @Parameter(name="word") String string) {
		if (!Foxtrot.getInstance().getConfig().getStringList("Filter").contains(string.toLowerCase())) {
			sender.sendMessage(ChatColor.RED + "That word is not contains.");
			return;
		}
		List<String> list = Foxtrot.getInstance().getConfig().getStringList("Filter");
		list.remove(string.toLowerCase());
		Foxtrot.getInstance().getConfig().set("Filter", list);
		Foxtrot.getInstance().saveConfig();

		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&aYou have successfully removed &f" + string.toLowerCase()
						+ " &ato the list of filter words."));
	}
}
