package net.frozenorb.foxtrot.listener;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.model.DBCollectionFindOptions;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.ItemBuilder;
import cc.fyre.proton.Proton;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RefundListener implements Listener {

    @EventHandler
    public void onCloseInv(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Refund Inventory")) {
            if (event.getPlayer() instanceof Player) {
                Player p = (Player) event.getPlayer();
                targetMap.remove(p);
            }
        }
    }

    @EventHandler
    public void onClickInv(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player clicked = (Player) event.getWhoClicked();
            if (event.getInventory().getTitle().equalsIgnoreCase("Refund Inventory")) {
                if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                    Player player = targetMap.get(clicked);
                    Foxtrot.getInstance().getServer().getScheduler().runTaskAsynchronously(Foxtrot.getInstance(), () -> {
                        Proton.getInstance().getIRedisCommand().runRedisCommand((redis) -> {
                            ItemStack[] contents = Proton.PLAIN_GSON.fromJson(redis.get("lastInv:contents:" + player.getUniqueId().toString()), ItemStack[].class);
                            ItemStack[] armor = Proton.PLAIN_GSON.fromJson(redis.get("lastInv:armorContents:" + player.getUniqueId().toString()), ItemStack[].class);

                            cleanLoot(contents);
                            cleanLoot(armor);

                            Foxtrot.getInstance().getServer().getScheduler().runTask(Foxtrot.getInstance(), () -> {
                                player.getInventory().setContents(contents);
                                player.getInventory().setArmorContents(armor);
                                player.updateInventory();
                                DBCollection mongoCollection = Foxtrot.getInstance().getMongoPool().getDB(Foxtrot.MONGO_DB_NAME).getCollection("Deaths");
                                for (DBObject object : mongoCollection.find(new BasicDBObject("uuid", player.toString().replace("-", "")), new DBCollectionFindOptions().limit(10).sort(new BasicDBObject("when", -1)))) {
                                    clicked.chat("/invrestore " + object.get("_id").toString());
                                }
                                clicked.sendMessage(ChatColor.GREEN + "Loaded " + player.getName() + "'s last inventory.");
                            });

                            return null;
                        });
                    });
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static HashMap<Player, Player> targetMap = new HashMap<>();

    public static void openRefundInv(Player player, Player target) {
        targetMap.put(player, target);
        Inventory inv = Bukkit.createInventory(null, 54, "Refund Inventory");
        Proton.getInstance().getIRedisCommand().runRedisCommand((redis) -> {
            ItemStack[] contents = Proton.PLAIN_GSON.fromJson(redis.get("lastInv:contents:" + target.getUniqueId().toString()), ItemStack[].class);
            ItemStack[] armor = Proton.PLAIN_GSON.fromJson(redis.get("lastInv:armorContents:" + target.getUniqueId().toString()), ItemStack[].class);

            inv.setContents(contents);

            inv.setItem(36, armor[0]);
            inv.setItem(37, armor[1]);
            inv.setItem(38, armor[2]);
            inv.setItem(39, armor[3]);
            inv.setItem(40, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(41, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(42, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(43, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(44, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));

            inv.setItem(45, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(46, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(47, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(48, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(49, new ItemBuilder(Material.SKULL_ITEM).lore("Click here to refund.").displayName("" + target.getName() + "'s Inventory").build());
            inv.setItem(50, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(51, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(52, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            inv.setItem(53, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            return null;
        });
        player.openInventory(inv);
    }

    public static void recordInventory(Player player) {
        recordInventory(player.getUniqueId(), player.getInventory().getContents(), player.getInventory().getArmorContents());
    }

    public static void recordInventory(UUID player, ItemStack[] contents, ItemStack[] armor) {
        Foxtrot.getInstance().getServer().getScheduler().runTaskAsynchronously(Foxtrot.getInstance(), () -> Proton.getInstance().getIRedisCommand().runRedisCommand((redis) -> {
            redis.set("lastInv:contents:" + player.toString(), Proton.PLAIN_GSON.toJson(contents));
            redis.set("lastInv:armorContents:" + player.toString(), Proton.PLAIN_GSON.toJson(armor));
            return null;
        }));
    }
    public static void cleanLoot(ItemStack[] stack) {
        for (ItemStack item : stack) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
                ItemMeta meta = item.getItemMeta();

                List<String> lore = item.getItemMeta().getLore();
                lore.remove(ChatColor.DARK_GRAY + "PVP Loot");
                meta.setLore(lore);

                item.setItemMeta(meta);
            }
        }
    }
}
