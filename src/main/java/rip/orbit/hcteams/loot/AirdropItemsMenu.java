package rip.orbit.hcteams.loot;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.pagination.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.orbit.hcteams.HCF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirdropItemsMenu extends PaginatedMenu {

    public AirdropItemsMenu() {
        setAutoUpdate(true);
        setUpdateAfterClick(true);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return ChatColor.RED + ChatColor.BOLD.toString() + "Loot";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int i = 0;

        for (ItemStack item : HCF.getInstance().getAirdropHandler().getAirdropLoot()) {
            buttons.put(i++, new AirdropItemButton(item));
        }

        return buttons;
    }

    @Override
    public int size(Player player) {
        return 36;
    }

    public static class AirdropItemButton extends Button {

        private final ItemStack itemStack;

        public AirdropItemButton(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public String getName(Player player) {
            return itemStack.getItemMeta().getDisplayName();
        }

        @Override
        public List<String> getDescription(Player player) {
            List<String> lore = itemStack.getItemMeta().getLore() == null ? new ArrayList<>() : itemStack.getItemMeta().getLore();

            if (player.hasPermission("hcf.airdrops.loot.remove")) {
                lore.add("");
                lore.add(ChatColor.RED + "Click to remove from the airdrop loot.");
            }

            return lore;
        }

        @Override
        public Material getMaterial(Player player) {
            return itemStack.getType();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (player.hasPermission("hcf.airdrops.loot.remove")) {
                player.sendMessage(ChatColor.GREEN + "Removed the \"" + itemStack.getItemMeta().getDisplayName() + ChatColor.GREEN + "\" from the airdrop loot.");
                HCF.getInstance().getAirdropHandler().getAirdropLoot().remove(itemStack);
                HCF.getInstance().getAirdropHandler().saveAirdropLoot();
            }
        }
    }
}