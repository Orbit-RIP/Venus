package rip.orbit.hcteams.shops.blockshop.menu.type.button;

import cc.fyre.proton.Proton;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.orbit.hcteams.shops.blockshop.menu.type.StainedClayBlocks;

import java.util.ArrayList;
import java.util.List;

public class StainedClayPurchaseButton extends Button {

    private final Player player;
    private final StainedClayBlocks stainedClayBlocks;

    public StainedClayPurchaseButton(Player player, StainedClayBlocks stainedClayBlocks) {
        this.player = player;
        this.stainedClayBlocks = stainedClayBlocks;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();
        double balance = Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId());

        description.add("");
        description.add(ChatColor.GRAY + "Price: " + ChatColor.YELLOW + stainedClayBlocks.getPrice());
        description.add(ChatColor.GRAY + "Amount: " + ChatColor.WHITE +  stainedClayBlocks.getAmount());
        description.add("");
        if (balance > stainedClayBlocks.getPrice())
            description.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "LEFT CLICK" + ChatColor.GREEN + " to purchase for " + ChatColor.BOLD + "$" + stainedClayBlocks.getPrice());
        else
            description.add(ChatColor.RED + "You are not able to purchase this item.");

        return description;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack item = this.stainedClayBlocks.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.setLore(getDescription(player));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Material getMaterial(Player player) {
        return stainedClayBlocks.getItem().getType();
    }

    @Override
    public String getName(Player player) {
        return ChatColor.WHITE +  ItemUtils.getName(stainedClayBlocks.getItem());
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        double balance = Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId());

        if (stainedClayBlocks.getPrice() < 0) {
            player.sendMessage(ChatColor.RED + "This item is not purchasable!");
            return;
        }

        if (balance < stainedClayBlocks.getPrice()) {
            player.sendMessage(ChatColor.RED + "You do not have enough money to purchase this!");
            return;
        }

        Proton.getInstance().getEconomyHandler().setBalance(player.getUniqueId(), balance - stainedClayBlocks.getPrice());

        if(player.getInventory().firstEmpty() != -1) {
            for(int i = 1; i <= stainedClayBlocks.getAmount(); i++){
                player.getInventory().addItem(this.stainedClayBlocks.getItem());
            }
        } else {
            for(int i = 1; i <= stainedClayBlocks.getAmount(); i++) {
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), this.stainedClayBlocks.getItem());
            }
        }

        player.sendMessage(ChatColor.GREEN + "You have purchased " + ItemUtils.getName(stainedClayBlocks.getItem()) + ChatColor.GREEN + " for $" + stainedClayBlocks.getPrice() + ".");
    }

}
