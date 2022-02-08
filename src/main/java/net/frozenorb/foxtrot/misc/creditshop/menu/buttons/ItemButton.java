package net.frozenorb.foxtrot.misc.creditshop.menu.buttons;

import lombok.RequiredArgsConstructor;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.misc.creditshop.CreditShop;
import cc.fyre.proton.menu.Button;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ItemButton extends Button {

    private final CreditShop creditShop;

    @Override
    public String getName(Player player) {
        return null;
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return null;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return creditShop.toItemStack();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        CreditShop item = CreditShop.getByStack(getButtonItem(player));

        if (item == null)
            return;

        if (Foxtrot.getInstance().getCreditsMap().getCredits(player.getUniqueId()) < item.getPrice()) {
            player.sendMessage(ChatColor.RED + "You do not have enough tokens to buy this item.");
            return;
        }

        if (!item.getCommand().isEmpty()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format(item.getCommand(), player.getName()));
            Foxtrot.getInstance().getCreditsMap().setCredits(player.getUniqueId(), Foxtrot.getInstance().getCreditsMap().getCredits(player.getUniqueId()) - item.getPrice());
            return;
        }

        Foxtrot.getInstance().getCreditsMap().setCredits(player.getUniqueId(), Foxtrot.getInstance().getCreditsMap().getCredits(player.getUniqueId()) - item.getPrice());
        player.getInventory().addItem(item.getItemStack().lore().build()).values().forEach(itemStack
                -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
    }
}

