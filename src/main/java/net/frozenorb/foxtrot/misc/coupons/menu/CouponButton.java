package net.frozenorb.foxtrot.misc.coupons.menu;

import lombok.AllArgsConstructor;
import net.frozenorb.foxtrot.misc.coupons.Coupon;
import cc.fyre.proton.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@AllArgsConstructor
public class CouponButton extends Button {

    private Coupon coupon;

    @Override
    public String getName(Player player) {
        return "&6Coupon ID: &e" + (coupon.getUuid().toString().split("-")[0]);
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> toReturn = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));

        toReturn.add("&7&m---------------------------");
        toReturn.add(" &8* &eStatus&7: &f" + (coupon.isRedeemed() ? "&aRedeemed" : "&cUnclaimed"));
        toReturn.add(" &8* &eDescription&7: &f$" + coupon.getDesc());
        toReturn.add(" &8* &eCreation Time&7: &f" + simpleDateFormat.format(new Date(coupon.getTime())) + " EST");

        if (!coupon.isRedeemed()) {
            toReturn.add(" ");
            toReturn.add(" &6* &fYou can &6left click &fto set this coupon to &aredeemed&f.");
        }

        toReturn.add("&7&m---------------------------");
        return toReturn;
    }

    @Override
    public Material getMaterial(Player player) {
        return (coupon.isRedeemed() ? Material.REDSTONE_BLOCK : Material.PAPER);
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (!coupon.isRedeemed()) {
            coupon.setRedeemed(true);
        }
    }
}
