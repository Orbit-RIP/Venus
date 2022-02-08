package net.frozenorb.foxtrot.redeem.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.redeem.RedeemHandler;
import net.frozenorb.foxtrot.redeem.object.Partner;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 06/09/2021 / 9:26 PM
 * HCTeams / rip.orbit.hcteams.redeem
 */
public class PartnerAddCommand {

	@Command(names = {"redeem add"}, permission = "op")
	public static void addParty(CommandSender sender, @Parameter(name = "partnerName") String partnerName) {
		if (RedeemHandler.partners.contains(Foxtrot.getInstance().getRedeemHandler().partnerByName(partnerName))) {
			sender.sendMessage(CC.translate("&cThat partner already exists."));
			return;
		}
		Partner partner = new Partner(partnerName);
		partner.save();
		RedeemHandler.partners.add(partner);

		sender.sendMessage(CC.translate("&aYou have just created a new redeemable partner name"));
	}

}
