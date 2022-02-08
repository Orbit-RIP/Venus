package net.frozenorb.foxtrot.misc.creditshop.menu.buttons;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BalanceButton extends Button {

    @Override
    public String getName(Player player) {
        return "&6Balance";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "",
                "&eTokens: &f" + Foxtrot.getInstance().getCreditsMap().getCredits(player.getUniqueId()),
                "");
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.GOLD_INGOT;
    }
}
