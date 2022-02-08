package net.frozenorb.foxtrot.misc.coupons.menu;

import lombok.AllArgsConstructor;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.misc.coupons.Coupon;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class CouponDisplayMenu extends Menu {

    private UUID uuid;

    @Override
    public String getTitle(Player player) {
        return CC.translate("&6Coupons");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Coupon> coupons = Foxtrot.getInstance().getCouponHandler().getCoupons(uuid);

        int size = (int) (Math.ceil(coupons.size() / 9d) * 9) + 18;

        for (int i = 0; i < 9; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)7, "&6"));
        }

        for (int i = (size - 9); i < size; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)7, "&6"));
        }

        int i = 8;
        for (Coupon coupon : coupons) {
            i++;
            buttons.put(i , new CouponButton(coupon));
        }

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}
