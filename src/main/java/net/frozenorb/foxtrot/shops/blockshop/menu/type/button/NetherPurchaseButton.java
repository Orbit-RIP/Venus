package net.frozenorb.foxtrot.shops.blockshop.menu.type.button;

import cc.fyre.proton.Proton;
import net.frozenorb.foxtrot.shops.blockshop.menu.type.NetherBlocks;
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

public class NetherPurchaseButton extends Button {

    private final Player player;
    private final NetherBlocks netherBlocks;

    public NetherPurchaseButton(Player player, NetherBlocks netherBlocks) {
        this.player = player;
        this.netherBlocks = netherBlocks;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();
        double balance = Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId());

        description.add("");
        description.add(ChatColor.GRAY + "Price: " + ChatColor.YELLOW + netherBlocks.getPrice());
        description.add(ChatColor.GRAY + "Amount: " + ChatColor.WHITE +  netherBlocks.getAmount());
        description.add("");
        if (balance > netherBlocks.getPrice())
            description.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "LEFT CLICK" + ChatColor.GREEN + " to purchase for " + ChatColor.BOLD + "$" + netherBlocks.getPrice());
        else
            description.add(ChatColor.RED + "You are not able to purchase this item.");

        return description;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack item = this.netherBlocks.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.setLore(getDescription(player));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Material getMaterial(Player player) {
        return netherBlocks.getItem().getType();
    }

    @Override
    public String getName(Player player) {
        return ChatColor.WHITE +  ItemUtils.getName(netherBlocks.getItem());
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        double balance = Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId());

        if (netherBlocks.getPrice() < 0) {
            player.sendMessage(ChatColor.RED + "This item is not purchasable!");
            return;
        }

        if (balance < netherBlocks.getPrice()) {
            player.sendMessage(ChatColor.RED + "You do not have enough money to purchase this!");
            return;
        }

        Proton.getInstance().getEconomyHandler().setBalance(player.getUniqueId(), balance - netherBlocks.getPrice());

        if(player.getInventory().firstEmpty() != -1) {
            for(int i = 1; i <= netherBlocks.getAmount(); i++){
                player.getInventory().addItem(this.netherBlocks.getItem());
            }
        } else {
            for(int i = 1; i <= netherBlocks.getAmount(); i++) {
                player.getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), this.netherBlocks.getItem());
            }
        }

        player.sendMessage(ChatColor.GREEN + "You have purchased " + ItemUtils.getName(netherBlocks.getItem()) + ChatColor.GREEN + " for $" + netherBlocks.getPrice() + ".");
    }

}
