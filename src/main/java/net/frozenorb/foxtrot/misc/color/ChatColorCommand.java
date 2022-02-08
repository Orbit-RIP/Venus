package net.frozenorb.foxtrot.misc.color;

import net.frozenorb.foxtrot.misc.color.menu.ChatColorMenu;
import net.frozenorb.foxtrot.misc.coupons.menu.CouponDisplayMenu;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.entity.Player;

/**
 * Created by PVPTUTORIAL | Created on 05/05/2020
 */

public class ChatColorCommand {

    @Command(names = {"color", "chatcolor", "setchatcolor"}, permission = "")
    public static void chatcolorCommand(Player player) {
        new ChatColorMenu(player.getUniqueId()).openMenu(player);
    }

}
