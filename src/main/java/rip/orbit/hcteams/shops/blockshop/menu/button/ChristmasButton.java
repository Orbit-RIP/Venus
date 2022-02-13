package rip.orbit.hcteams.shops.blockshop.menu.button;

import cc.fyre.proton.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.shops.blockshop.menu.ChristmasBlockMenu;
import rip.orbit.nebula.util.CC;

import java.util.ArrayList;
import java.util.List;

public class ChristmasButton extends Button {

    @Override
    public String getName(Player player) {
        return CC.translate("&a&lC&c&lh&a&lr&c&li&a&ls&c&Lt&a&lm&c&la&a&ls &c&lB&a&ll&c&Lo&a&Lc&c&lk&a&Ls");
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.SNOW_BLOCK;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        new ChristmasBlockMenu().openMenu(player);
    }
}
