package net.frozenorb.foxtrot.shops.blockshop.menu.type.button;

import cc.fyre.proton.Proton;
import net.frozenorb.foxtrot.shops.blockshop.menu.type.StainedGlassBlocks;
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

public class StainedGlassPurchaseButton extends Button {

    private final Player player;
    private final StainedGlassBlocks stainedGlassBlocks;

    public StainedGlassPurchaseButton(Player player, StainedGlassBlocks stainedGlassBlocks) {
        this.player = player;
        this.stainedGlassBlocks = stainedGlassBlocks;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();
        double balance = Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId());

        description.add("");
        description.add(ChatColor.GRAY + "Price: " + ChatColor.YELLOW + stainedGlassBlocks.getPrice());
        description.add(ChatColor.GRAY + "Amount: " + ChatColor.WHITE +  stainedGlassBlocks.getAmount());
        description.add("");
        if (balance > stainedGlassBlocks.getPrice())
            description.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "LEFT CLICK" + ChatColor.GREEN + " to purchase for " + ChatColor.BOLD + "$" + stainedGlassBlocks.getPrice());
        else
            description.add(ChatColor.RED + "You are not able to purchase this item.");

        return description;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack item = this.stainedGlassBlocks.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.setLore(getDescription(player));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Material getMaterial(Player player) {
        return stainedGlassBlocks.getItem().getType();
    }

    @Override
    public String getName(Player player) {
        return ChatColor.WHITE +  ItemUtils.getName(stainedGlassBlocks.getItem());
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        double balance = Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId());

        if (stainedGlassBlocks.getPrice() < 0) {
            player.sendMessage(ChatColor.RED + "This item is not purchasable!");
            return;
        }

        if (balance < stainedGlassBlocks.getPrice()) {
            player.sendMessage(ChatColor.RED + "You do not have enough money to purchase this!");
            return;
        }

        Proton.getInstance().getEconomyHandler().setBalance(player.getUniqueId(), balance - stainedGlassBlocks.getPrice());

        if(player.getInventory().firstEmpty() != -1) {
            for(int i = 1; i <= stainedGlassBlocks.getAmount(); i++){
                player.getInventory().addItem(this.stainedGlassBlocks.getItem());
            }
        } else {
            for(int i = 1; i <= stainedGlassBlocks.getAmount(); i++) {
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), this.stainedGlassBlocks.getItem());
            }
        }

        player.sendMessage(ChatColor.GREEN + "You have purchased " + ChatColor.BOLD + ItemUtils.getName(stainedGlassBlocks.getItem()) + ChatColor.GREEN + " for $" + stainedGlassBlocks.getPrice() + ".");
    }

}
