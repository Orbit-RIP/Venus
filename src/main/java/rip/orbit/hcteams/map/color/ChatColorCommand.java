package rip.orbit.hcteams.map.color;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.map.color.menu.ChatColorMenu;

/**
 * Created by PVPTUTORIAL | Created on 05/05/2020
 */

public class ChatColorCommand {

    @Command(names = {"color", "chatcolor", "setchatcolor"}, permission = "")
    public static void chatcolorCommand(Player player) {
        new ChatColorMenu(player.getUniqueId()).openMenu(player);
    }

}
