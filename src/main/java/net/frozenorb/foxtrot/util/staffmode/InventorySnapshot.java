//package net.frozenorb.foxtrot.util.staffmode;
//
//import lombok.Getter;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.potion.PotionEffect;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class InventorySnapshot {
//
//    @Getter
//    private ItemStack armor[];
//
//    @Getter
//    private ItemStack contents[];
//
//    @Getter
//    private List<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
//
//    public InventorySnapshot(Player player) {
//        this.armor = player.getInventory().getArmorContents();
//        this.contents = player.getInventory().getContents();
//
//        for (org.bukkit.potion.PotionEffect potionEffect : player.getActivePotionEffects()) {
//            potionEffects.add(potionEffect);
//        }
//    }
//
//}
