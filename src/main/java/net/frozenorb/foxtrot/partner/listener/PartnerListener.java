package net.frozenorb.foxtrot.partner.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.partner.Partner;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Created by MaikoX
 */

public class PartnerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Partner partner = this.getRandomReward();
        if (!e.getAction().name().startsWith("RIGHT_CLICK_")) {
            return;
        }

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            return;
        }

        ItemStack hand = e.getItem();

        if (hand == null) {
            return;
        }

        if (!hand.isSimilar(InventoryUtils.packageItem(hand.getAmount()))) {
            return;
        }

        if (player.getItemInHand().getAmount() == 1) {
            player.setItemInHand(null);
        } else {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        }

        partner.getItems().forEach(itemStack -> {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(itemStack);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            }
        });


        partner.getCommands().forEach(str -> {
            String command = str.replaceAll("%player%", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        });

        player.updateInventory();
        player.sendMessage(CC.translate(Foxtrot.getInstance().getConfig().getString("partner.message")));
        e.setCancelled(true);
    }

    private Partner getRandomReward() {
        if(Partner.getPartners().size() == 1)
            return Partner.getPartners().get(0);
        return Partner.getPartners().get(new Random().nextInt(Partner.getPartners().size()));
    }
}
