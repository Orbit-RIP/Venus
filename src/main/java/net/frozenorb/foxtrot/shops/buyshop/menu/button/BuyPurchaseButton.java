package net.frozenorb.foxtrot.shops.buyshop.menu.button;

import cc.fyre.proton.Proton;
import net.frozenorb.foxtrot.shops.buyshop.BuyShopBlocks;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BuyPurchaseButton extends Button {

    private final Player player;
    private final BuyShopBlocks buyBlocks;

    public BuyPurchaseButton(Player player, BuyShopBlocks buyBlocks) {
        this.player = player;
        this.buyBlocks = buyBlocks;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();
        double balance = Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId());

        description.add("");
        description.add(ChatColor.GRAY + "Price: " + ChatColor.YELLOW + buyBlocks.getPrice());
        description.add(ChatColor.GRAY + "Amount: " + ChatColor.WHITE +  buyBlocks.getAmount());
        description.add("");
        if (balance > buyBlocks.getPrice())
            description.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "LEFT CLICK" + ChatColor.GREEN + " to purchase for " + ChatColor.BOLD + "$" + buyBlocks.getPrice());
        else
            description.add(ChatColor.RED + "You are not able to purchase this item.");

        return description;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack item = this.buyBlocks.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.setLore(getDescription(player));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Material getMaterial(Player player) {
        return buyBlocks.getItem().getType();
    }

    @Override
    public String getName(Player player) {
        return ChatColor.WHITE +  ItemUtils.getName(buyBlocks.getItem());
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        double balance = Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId());

        if (buyBlocks.getPrice() < 0) {
            player.sendMessage(ChatColor.RED + "This item is not purchasable!");
            return;
        }

        if (balance < buyBlocks.getPrice()) {
            player.sendMessage(ChatColor.RED + "You do not have enough money to purchase this!");
            return;
        }

        Proton.getInstance().getEconomyHandler().setBalance(player.getUniqueId(), balance - buyBlocks.getPrice());

        if(player.getInventory().firstEmpty() != -1) {
            for(int i = 1; i <= buyBlocks.getAmount(); i++){
                player.getInventory().addItem(this.buyBlocks.getItem());
            }
        } else {
            for(int i = 1; i <= buyBlocks.getAmount(); i++) {
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), this.buyBlocks.getItem());
            }
        }

        player.sendMessage(ChatColor.GREEN + "You have purchased " + ItemUtils.getName(buyBlocks.getItem()) + ChatColor.GREEN + " for $" + buyBlocks.getPrice() + ".");
    }

}
