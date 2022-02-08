package net.frozenorb.foxtrot.redeem;

import cc.fyre.proton.Proton;
import lombok.Getter;
import net.frozenorb.foxtrot.redeem.command.param.PartnerParamType;
import net.frozenorb.foxtrot.redeem.map.PartnerRedeemMap;
import net.frozenorb.foxtrot.redeem.object.Partner;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RedeemHandler {

	public static List<Partner> partners = new ArrayList<>();
	private final PartnerRedeemMap redeemMap;

	public RedeemHandler() {
		for (Document document : Partner.getCollection().find()) {
			partners.add(new Partner(document.getString("name")));
		}
		Proton.getInstance().getCommandHandler().registerParameterType(Partner.class, new PartnerParamType());
		redeemMap = new PartnerRedeemMap();
		redeemMap.loadFromRedis();
	}

	public Partner partnerByName(String toSearch) {
		for (Partner partner : partners) {
			if (partner.getName().equals(toSearch)) {
				return partner;
			}
		}
		return null;
	}

}
