package net.frozenorb.foxtrot.events.ktk.gui;

import lombok.Data;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.ktk.KillTheKing;
import net.frozenorb.foxtrot.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@Data
public class KillTheKingGUI implements Listener {

    private KillTheKingAdvancementGUI killTheKingAdvancementGUI;

    private Inventory inventory;

    public KillTheKingGUI() {
        inventory = Bukkit.getServer().createInventory(null, 9, "§6Kill The King");

        ItemStack start = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta startM = start.getItemMeta();
        startM.setDisplayName(ColorUtil.format("&a&lSTART"));
        startM.setLore(Arrays.asList(ColorUtil.format("&7Start a kill the king event.")));

        start.setItemMeta(startM);

        inventory.setItem(2, start);

        ItemStack advancements = new ItemStack(Material.PAPER);
        ItemMeta advancementsM = advancements.getItemMeta();
        advancementsM.setDisplayName(ColorUtil.format("&e&lADVANCEMENTS"));
        advancementsM.setLore(Arrays.asList(ColorUtil.format("&7Power up and don't get killed!")));

        advancements.setItemMeta(advancementsM);

        inventory.setItem(6, advancements);

        killTheKingAdvancementGUI = new KillTheKingAdvancementGUI();

        Bukkit.getServer().getPluginManager().registerEvents(this, Foxtrot.getInstance());
        Bukkit.getServer().getPluginManager().registerEvents(killTheKingAdvancementGUI, Foxtrot.getInstance());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();

            if (e.getInventory().getName().equalsIgnoreCase(inventory.getName())) {
                e.setCancelled(true);

                if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null || e.getCurrentItem().getItemMeta().getDisplayName() ==  null) return;

                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ColorUtil.format("&a&lSTART"))) {
                    if (Foxtrot.getInstance().getKillTheKing() == null) {
                        Foxtrot.getInstance().setKillTheKing(new KillTheKing(p.getUniqueId()));
                        p.closeInventory();
                        return;
                    } else {
                        p.sendMessage(ChatColor.RED + "Kill The King is currently active.");
                        return;
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ColorUtil.format("&e&lADVANCEMENTS"))) {
                    KillTheKing killTheKing = Foxtrot.getInstance().getKillTheKing();

                    if (killTheKing != null) {
                        if (killTheKing.getUuid().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
                            p.closeInventory();
                            p.openInventory(killTheKingAdvancementGUI.getInventory());
                            return;
                        } else {
                            p.sendMessage(ChatColor.RED + "You are not the current king.");
                            return;
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Kill The King is currently not active.");
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().getName().equalsIgnoreCase(inventory.getName())) {
            e.setCancelled(true);
        }
    }

}
