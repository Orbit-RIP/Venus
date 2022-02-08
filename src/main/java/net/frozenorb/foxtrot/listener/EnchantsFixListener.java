//package net.frozenorb.foxtrot.listener;
//
//import com.hcrival.enchants.customenchant.CustomEnchant;
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.util.ItemUtils;
//import org.bukkit.Bukkit;
//import org.bukkit.enchantments.custom.CustomEnchantment;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.entity.PotionEffectExpireEvent;
//import org.bukkit.inventory.ItemStack;
//
//public class EnchantsFixListener implements Listener {
//
//    @EventHandler
//    public void onPotionExpire(PotionEffectExpireEvent event) {
//        if (event.getEntity() instanceof Player) {
//            Player player = (Player) event.getEntity();
//
//            for (ItemStack itemStack : player.getInventory().getArmorContents()) {
//                if (ItemUtils.hasLore(itemStack)) {
//                    for (String lore : itemStack.getItemMeta().getLore()) {
////                        CustomEnchant customEnchant = CustomEnchant.byString(lore.substring(2).toUpperCase());
//                        if (customEnchant != null) {
//                            Bukkit.getServer().getScheduler().runTaskLater(Foxtrot.getInstance(), () -> customEnchant.fulfill(player), 1L);
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
