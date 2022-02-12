package rip.orbit.hcteams.chatgames.command;

import org.bukkit.command.CommandSender;
import rip.orbit.hcteams.HCF;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 27/08/2021 / 4:35 PM
 * HCTeams / rip.orbit.hcteams.chatgames.command
 */
public class ChatGameCommand {

	@cc.fyre.proton.command.Command(names = "chatgame autostart math", permission = "chatgame.admin")
	public static void autostartMath(CommandSender sender) {
		HCF.getInstance().getChatGameHandler().getChatGames().get(1).start();
	}
	@cc.fyre.proton.command.Command(names = "chatgame autostart question", permission = "chatgame.admin")
	public static void autostartQuestion(CommandSender sender) {
		HCF.getInstance().getChatGameHandler().getChatGames().get(0).start();
	}
}
