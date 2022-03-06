//package rip.orbit.hcteams.wanderer.menu;
//
//import cc.fyre.proton.Proton;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.ClickType;
//import org.bukkit.inventory.ItemStack;
//import rip.orbit.hcteams.HCF;
//import rip.orbit.hcteams.util.menu.Button;
//import rip.orbit.hcteams.util.menu.Menu;
//import rip.orbit.hcteams.wanderer.WandererButton;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class WandererMenu extends Menu {
//
//    @Override
//    public String getTitle(Player player) {
//        return "Wanderer";
//    }
//
//    @Override
//    public int size() {
//        return 0;
//    }
//
//    @Override
//    public Map<Integer, Button> getButtons(Player player) {
//        Map<Integer, Button> buttonMap = new HashMap<>();
//        for (WandererButton wandererButton : HCF.getInstance().getWandererHandler().getItemStackSet()) {
//
//            ItemStack clone = wandererButton.getItemStack().clone();
//            clone.getItemMeta().getLore().add("");
//            clone.getItemMeta().getLore().add("Price: " + wandererButton.getPrice());
//
//            buttonMap.put(buttonMap.size(), new Button() {
//                @Override
//                public ItemStack getItem(Player player) {
//                    return clone;
//                }
//
//                @Override
//                public void clicked(Player player, ClickType clickType) {
//                    if (Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId()) < wandererButton.getPrice()) {
//                        player.sendMessage(ChatColor.RED + "You do not have enough balance to buy this.");
//                        return;
//                    }
//
//
//                }
//            });
//        }
//        return buttonMap;
//
//    }
//}
