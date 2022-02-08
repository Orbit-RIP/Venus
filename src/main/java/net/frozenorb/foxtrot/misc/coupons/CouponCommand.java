package net.frozenorb.foxtrot.misc.coupons;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.misc.coupons.menu.CouponDisplayMenu;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CouponCommand {

    @Command(names = {"coupon add", "coupons add"}, permission = "coupons.admin", async = true)
    public static void couponadd(CommandSender sender, @Parameter(name = "player") UUID player, @Parameter(name = "value") double value, @Parameter(name = "desc", wildcard = true) String desc) {
        Foxtrot.getInstance().getCouponHandler().getCoupons().add(new Coupon(player, desc, value));
        sender.sendMessage(ChatColor.GREEN + "Successfully added the coupon " + desc + " to the player " + UUIDUtils.name(player) + ".");
    }

    @Command(names = {"coupon check", "coupons check"}, permission = "coupons.admin", async = true)
    public static void couponcheck(Player sender, @Parameter(name =  "player" )UUID player) {
        new CouponDisplayMenu(player).openMenu(sender);
    }
}
