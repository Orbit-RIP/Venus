//package net.frozenorb.foxtrot.crate.menu;
//
//import lombok.SneakyThrows;
//import me.lbuddyboy.crates.api.CrateAPI;
//import me.lbuddyboy.crates.menu.RewardsMenu;
//import me.lbuddyboy.crates.object.Crate;
//import me.lbuddyboy.crates.util.CC;
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.crate.CrateHandler;
//import net.frozenorb.foxtrot.util.ItemBuilder;
//import cc.fyre.proton.menu.Button;
//import cc.fyre.proton.menu.Menu;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.ClickType;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.SkullMeta;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class CrateMenu extends Menu {
//
//    public CrateHandler handler = Foxtrot.getInstance().getCrateHandler();
//    public FileConfiguration conf = handler.getConfigFile().getConfiguration();
//
//    @Override
//    public String getTitle(Player player) {
//        return CC.chat(conf.getString("gui-title"));
//    }
//
//    @Override
//    public int size(Map<Integer, Button> buttons) {
//        return conf.getInt("gui-size");
//    }
//
//
//    @Override
//    public Map<Integer, Button> getButtons(Player player) {
//        Map<Integer, Button> buttons = new HashMap<>();
//
//        for (String sec : conf.getConfigurationSection("crates").getKeys(false)) {
//            String main = "crates.";
//
//            buttons.put(conf.getInt(main + sec + ".slot") - 1, new Button() {
//                @Override
//                public String getName(Player player) {
//                    return CC.chat(conf.getString(main + sec + ".name"));
//                }
//
//                @Override
//                public List<String> getDescription(Player player) {
//                    return CC.translate(conf.getStringList(main + sec + ".lore"));
//                }
//
//                @Override
//                public Material getMaterial(Player player) {
//                    Material material = Material.valueOf(conf.getString(main + sec + ".item"));
//                    int data = 3;
//                    ItemStack stack = new ItemBuilder(material).data((short) data).build();
//
//                    if (conf.getBoolean(main + sec + ".skull.use-skull")) {
//                        String owner = conf.getString(main + sec + ".skull.owner");
//                        SkullMeta meta = (SkullMeta) stack.getItemMeta();
//                        meta.setOwner(owner);
//                        stack.setItemMeta(meta);
//                    }
//                    return stack.getType();
////                    return Material.valueOf(conf.getString(main + sec + ".item"));
//                }
//
//                @Override
//                public byte getDamageValue(Player player) {
//                    return 3;
//                }
//
//                @SneakyThrows
//                @Override
//                public void clicked(Player player, int slot, ClickType clickType) {
//                    Crate crate = CrateAPI.byName(sec);
//                    if (clickType.isLeftClick()) {
//                        if (player.getItemInHand() != null && player.getItemInHand().isSimilar(handler.getKey())) {
//                            if (player.getItemInHand().getAmount() > 1) {
//                                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
//                            } else {
//                                player.setItemInHand(null);
//                            }
//
//                            player.updateInventory();
//                            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.5F, 0.5F);
//                            if (!conf.contains(main + sec + ".times-used")) {
//                                conf.createSection(main + sec + ".times-used");
//                                conf.set(main + sec + ".times-used", 1);
//                            } else {
//                                conf.set(main + sec + ".times-used", conf.getInt(main + sec + ".times-used") + 1);
//                            }
//                            Foxtrot.getInstance().getCrateHandler().getConfigFile().save();
//                            crate.open(player);
//
//                        } else {
//                            player.sendMessage(CC.chat("&cYou do not have a partner key in your hand."));
//                        }
//                    } else if (clickType.isRightClick()) {
//                        new RewardsMenu(crate.getName()).openMenu(player);
//                    }
//                }
//            });
//
//        }
//
//        return buttons;
//    }
//}
