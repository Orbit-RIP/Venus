package net.frozenorb.foxtrot.misc.crackers.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.misc.crackers.Cracker;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class CrackerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Cracker cracker = this.getRandomReward();
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

        if (!hand.isSimilar(InventoryUtils.crackerItem(hand.getAmount()))) {
            return;
        }

        if (player.getItemInHand().getAmount() == 1) {
            player.setItemInHand(null);
        } else {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        }

        cracker.getItems().forEach(itemStack -> {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(itemStack);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            }
        });


        cracker.getCommands().forEach(str -> {
            String command = str.replaceAll("%player%", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        });

        player.updateInventory();
        player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1.0f, 1.0f);
        player.sendMessage(CC.translate(Foxtrot.getInstance().getConfig().getString("cracker.message")));
        e.setCancelled(true);
    }

    private Cracker getRandomReward() {
        if(Cracker.getCrackers().size() == 1)
            return Cracker.getCrackers().get(0);
        return Cracker.getCrackers().get(new Random().nextInt(Cracker.getCrackers().size()));
    }
}
