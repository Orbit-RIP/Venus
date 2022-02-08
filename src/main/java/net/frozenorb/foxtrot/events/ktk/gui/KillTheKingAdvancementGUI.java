package net.frozenorb.foxtrot.events.ktk.gui;

import lombok.Data;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.ktk.KillTheKing;
import net.frozenorb.foxtrot.events.ktk.KillTheKingAdvancement;
import net.frozenorb.foxtrot.team.Team;
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
import java.util.HashMap;
import java.util.Map;

@Data
public class KillTheKingAdvancementGUI implements Listener {

    private Inventory inventory;

    private Map<KillTheKingAdvancement, ItemStack> advancementItemStackMap = new HashMap<>();

    public KillTheKingAdvancementGUI() {
        inventory = Bukkit.createInventory(null, 9, "§aKill The King - Advancements");

        int i = 0;

        for (KillTheKingAdvancement killTheKingAdvancement : KillTheKingAdvancement.getAdvancements()) {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(ColorUtil.format("&e" + killTheKingAdvancement.getPotionEffect().getType().getName() + " " + (killTheKingAdvancement.getPotionEffect().getAmplifier() + 1)));
            itemMeta.setLore(Arrays.asList(ColorUtil.format("&7" + killTheKingAdvancement.getPoints() + " points")));

            itemStack.setItemMeta(itemMeta);
            inventory.setItem(i, itemStack);
            i++;

            advancementItemStackMap.put(killTheKingAdvancement, itemStack);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getInventory().getName().equalsIgnoreCase(inventory.getName())) {
                e.setCancelled(true);

                if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null || e.getCurrentItem().getItemMeta().getDisplayName() ==  null) return;

                for (Map.Entry<KillTheKingAdvancement, ItemStack> entry : advancementItemStackMap.entrySet()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(entry.getValue().getItemMeta().getDisplayName())) {
                        if (Foxtrot.getInstance().getKillTheKing() != null) {
                            KillTheKing killTheKing = Foxtrot.getInstance().getKillTheKing();

                            if (killTheKing.getAdvancementsPurchased().contains(entry.getKey())) {
                                p.sendMessage(ChatColor.RED + "You have already purchased that advancement.");
                                return;
                            } else {
                                Team team = Foxtrot.getInstance().getTeamHandler().getTeam(p);

                                if (team == null) {
                                    p.sendMessage(ChatColor.RED + "You are not in a faction.");
                                    return;
                                }

                                if ((team.getPoints() - entry.getKey().getPoints()) < 0) {
                                    p.sendMessage(ChatColor.RED + "You cannot afford this.");
                                    return;
                                }

                                team.setPoints(team.getPoints() - entry.getKey().getPoints());
                                entry.getKey().purchased(killTheKing);
                                p.closeInventory();
                            }
                        }
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
