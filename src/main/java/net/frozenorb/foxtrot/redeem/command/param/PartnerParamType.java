package net.frozenorb.foxtrot.redeem.command.param;

import cc.fyre.proton.command.param.ParameterType;
import com.google.common.collect.Lists;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.redeem.RedeemHandler;
import net.frozenorb.foxtrot.redeem.object.Partner;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 06/09/2021 / 9:41 PM
 * HCTeams / rip.orbit.hcteams.redeem
 */
public class PartnerParamType implements ParameterType<Partner> {
	@Override
	public Partner transform(CommandSender sender, String source) {
		return Foxtrot.getInstance().getRedeemHandler().partnerByName(source);
	}

	@Override
	public List<String> tabComplete(Player player, Set<String> flags, String source) {
		List<String> completions = Lists.newArrayList();

		for (Partner partner : RedeemHandler.partners) {
			if (StringUtils.startsWithIgnoreCase(partner.getName(), source)) {
				completions.add(partner.getName());
			}
		}

		return completions;
	}
}
