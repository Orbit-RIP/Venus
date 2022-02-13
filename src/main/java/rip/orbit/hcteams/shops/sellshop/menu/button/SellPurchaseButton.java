package rip.orbit.hcteams.shops.sellshop.menu.button;

import cc.fyre.proton.Proton;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.shops.sellshop.SellShopBlocks;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class SellPurchaseButton extends Button {

    private final Player player;
    private final SellShopBlocks sellBlocks;

    public SellPurchaseButton(Player player, SellShopBlocks sellBlocks) {
        this.player = player;
        this.sellBlocks = sellBlocks;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();

        description.add("");
        description.add(ChatColor.GRAY + "Price: " + ChatColor.YELLOW + sellBlocks.getPrice());
        description.add(ChatColor.GRAY + "Amount: " + ChatColor.WHITE +  sellBlocks.getAmount());
        description.add("");
        int amountInInventory = Math.min(sellBlocks.getAmount(), HCF.getInstance().getServerHandler().countItems(player, sellBlocks.getItem().getType(), 0));
        if (amountInInventory < sellBlocks.getAmount()) {
            description.add(ChatColor.RED + "You are not able to sell this item.");
        } else
            description.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "LEFT CLICK" + ChatColor.GREEN + " to sell for " + ChatColor.BOLD + "$" + sellBlocks.getPrice());

        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return sellBlocks.getItem().getType();
    }

    @Override
    public String getName(Player player) {
        return ChatColor.WHITE +  ItemUtils.getName(sellBlocks.getItem());
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        int amountInInventory = Math.min(sellBlocks.getAmount(), HCF.getInstance().getServerHandler().countItems(player, sellBlocks.getItem().getType(), 0));
        if (amountInInventory < sellBlocks.getAmount()) {
            player.sendMessage(ChatColor.RED + "You don't have enough of this item to sell.");
            return;
        }
        double totalPrice = amountInInventory * sellBlocks.getPrice() / sellBlocks.getAmount();

        HCF.getInstance().getServerHandler().removeItem(player, sellBlocks.getItem(), amountInInventory);
        player.updateInventory();

        Proton.getInstance().getEconomyHandler().deposit(player.getUniqueId(), totalPrice);
        player.sendMessage(ChatColor.GREEN + "Successfully sold " + amountInInventory + "x " + ChatColor.BOLD + ItemUtils.getName(sellBlocks.getItem()) + ChatColor.GREEN + " for $" + NumberFormat.getNumberInstance(Locale.US).format(totalPrice) + ".");
        player.sendMessage(ChatColor.GREEN + "New balance: $" + NumberFormat.getNumberInstance(Locale.US).format(Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId())));
    }

}
