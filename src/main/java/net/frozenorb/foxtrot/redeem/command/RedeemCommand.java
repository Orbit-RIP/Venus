package net.frozenorb.foxtrot.redeem.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.menu.AbilityDisplayMenu;
import net.frozenorb.foxtrot.redeem.RedeemHandler;
import net.frozenorb.foxtrot.redeem.menu.RedeemMenu;
import net.frozenorb.foxtrot.redeem.object.Partner;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 06/09/2021 / 9:40 PM
 * HCTeams / rip.orbit.hcteams.redeem
 */
public class RedeemCommand {

	@Command(names = "redeemview", permission = "")
	public static void redeemview(CommandSender sender) {
		sender.sendMessage(CC.translate(""));
		sender.sendMessage(CC.translate("&6&lRedeem Statistics"));
		sender.sendMessage(CC.translate(""));
		for (Partner partner : RedeemHandler.partners) {
			sender.sendMessage(CC.translate("&7» &6" + partner.getName() + "&7: &f" + partner.getRedeemedAmount()));
		}
		sender.sendMessage(CC.translate(""));
	}

	@Command(names = "resetredeem", permission = "op")
	public static void resetredeem(CommandSender sender, @Parameter(name = "player") UUID target) {
		sender.sendMessage(CC.translate("&aReset Partner Redeem"));
		Foxtrot.getInstance().getRedeemHandler().getRedeemMap().setToggled(target, false);
	}

	@Command(names = {"redeem"}, permission = "")
	public static void redeem(Player sender) {
//		if (partner == null) {
//			sender.sendMessage(CC.translate("&cCould not find a partner with that name."));
//			return;
//		}
//		if (Foxtrot.getInstance().getRedeemHandler().getRedeemMap().isToggled(sender.getUniqueId())) {
//			sender.sendMessage(CC.translate("&cYou have already redeemed a partner."));
//			return;
//		}
//
//		sender.sendMessage(CC.translate("&aSuccessfully redeemed for " + partner.getName()));
//
//		partner.setRedeemedAmount(partner.getRedeemedAmount() + 1);
//		partner.save();
//
//		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "airdrop give " + sender.getName() + " 1");
//		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "package " + sender.getName() + " 1");
//		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bcraw &6» &e" + sender.getName() + " &fhas just used &e/redeem &ffor free rewards!");
//
//
//		Foxtrot.getInstance().getRedeemHandler().getRedeemMap().setToggled(sender.getUniqueId(), true);

		new RedeemMenu().openMenu(sender);
	}



}
