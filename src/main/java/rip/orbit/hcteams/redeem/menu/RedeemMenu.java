package rip.orbit.hcteams.redeem.menu;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.redeem.object.Partner;
import rip.orbit.nebula.util.CC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedeemMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return ChatColor.GRAY + "Redeem an Partner";
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 0;
        for (Partner partner : HCF.getInstance().getRedeemHandler().getPartners()) {
            buttons.put(i, new Button() {
                @Override
                public String getName(Player player) {
                    return CC.GOLD + CC.BOLD + partner.getName();
                }

                @Override
                public List<String> getDescription(Player player) {
                    List<String> lore = new ArrayList<>();
                    lore.add("");
                    lore.add(CC.translate("&7Click to support &f" + partner.getName() + " &7for a reward"));
                    lore.add("");
                    return lore;
                }

                @Override
                public Material getMaterial(Player player) {
                    return Material.NAME_TAG;
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType) {
                    if (HCF.getInstance().getRedeemHandler().getRedeemMap().isToggled(player.getUniqueId())) {
                        player.sendMessage(CC.translate("&cYou have already redeemed a partner."));
                        player.closeInventory();
                        Button.playFail(player);
                        return;
                    }

                    player.sendMessage(CC.translate("&aSuccessfully redeemed for " + partner.getName()));

                    partner.setRedeemedAmount(partner.getRedeemedAmount() + 1);
                    partner.save();

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "airdrop " + player.getName() + " 1");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "package " + player.getName() + " 1");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bcraw &6» &e" + player.getName() + " &fhas just used &e/redeem &ffor free rewards!");
                    player.closeInventory();
                    Button.playSuccess(player);


                    HCF.getInstance().getRedeemHandler().getRedeemMap().setToggled(player.getUniqueId(), true);
                }
            });
            ++i;

        }
        return buttons;
    }
}
