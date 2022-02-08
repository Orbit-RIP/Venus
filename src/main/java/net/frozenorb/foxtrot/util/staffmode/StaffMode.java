//package net.frozenorb.foxtrot.util.staffmode;
//
//import com.lunarclient.bukkitapi.nethandler.client.LCPacketStaffModState;
//import lombok.Getter;
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.scoreboard.ScoreFunction;
//import cc.fyre.proton.menu.Button;
//import cc.fyre.proton.menu.Menu;
//import cc.fyre.proton.nametag.FrozenNametagHandler;
//import cc.fyre.proton.util.ItemBuilder;
//import cc.fyre.proton.util.TimeUtils;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.GameMode;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.metadata.FixedMetadataValue;
//import org.bukkit.potion.PotionEffect;
//import pw.navigations.qUtilities.user.User;
//import pw.navigations.qUtilities.user.rank.Rank;
//import pw.navigations.qUtilities.utils.ColorUtils;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Getter
//public class StaffMode {
//
//    @Getter
//    private static Map<UUID, StaffMode> staffModeMap = new HashMap<>();
//
//    private ItemStack[] previousInventory;
//
//    public StaffMode(Player player) {
//        initalise(player);
//
//        staffModeMap.put(player.getUniqueId(), this);
//    }
//
//    public void initalise(Player player) {
//        this.previousInventory = player.getInventory().getContents();
//
//        if (!Foxtrot.getInstance().getLastInventories().containsKey(player.getUniqueId())) {
//            Foxtrot.getInstance().getLastInventories().put(player.getUniqueId(), new InventorySnapshot(player));
//        }
//
//        player.getInventory().clear();
//
//        giveItems(player);
//
//        player.setMetadata("modmode", new FixedMetadataValue(Foxtrot.getInstance(), "modmode"));
//        player.setMetadata("staffmode", new FixedMetadataValue(Foxtrot.getInstance(), "staffmode"));
//        player.setMetadata("staffboard", new FixedMetadataValue(Foxtrot.getInstance(), true));
//
//        player.setGameMode(GameMode.CREATIVE);
//
//        vanish(player);
//
//        FrozenNametagHandler.reloadPlayer(player);
//        FrozenNametagHandler.reloadOthersFor(player);
//
//        for(Player onlinePlayers : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
//            FrozenNametagHandler.reloadPlayer(player, onlinePlayers);
//        }
//    }
//
//    public void destroy(Player player) {
//        player.getInventory().clear();
//
////        if (previousInventory[0] != null && previousInventory[0].getType() != Material.COMPASS) {
////            player.getInventory().setContents(previousInventory);
////        }
//
//        if (Foxtrot.getInstance().getLastInventories().containsKey(player.getUniqueId())) {
//            InventorySnapshot snapshot = Foxtrot.getInstance().getLastInventories().get(player.getUniqueId());
//
//            player.getInventory().setArmorContents(snapshot.getArmor());
//            player.getInventory().setContents(snapshot.getContents());
//
//            for (PotionEffect potionEffect : snapshot.getPotionEffects()) {
//                player.addPotionEffect(potionEffect);
//            }
//
//            Foxtrot.getInstance().getLastInventories().remove(player.getUniqueId());
//        }
//
//        player.updateInventory();
//
//        player.setGameMode(GameMode.SURVIVAL);
//
//        player.removeMetadata("modmode", Foxtrot.getInstance());
//        player.removeMetadata("staffmode", Foxtrot.getInstance());
//
//        unvanish(player);
//
//        staffModeMap.remove(player.getUniqueId());
//
//        FrozenNametagHandler.reloadPlayer(player);
//        FrozenNametagHandler.reloadOthersFor(player);
//    }
//
//    public void giveItems(Player player) {
//        player.getInventory().setItem(0, ItemBuilder.of(Material.COMPASS).name("§eTeleporter").build());
//        player.getInventory().setItem(1, ItemBuilder.of(Material.BOOK).name("§eInspect Inventory").build());
//        player.getInventory().setItem(2, ItemBuilder.of(Material.WATCH).name("§eRandom Teleport").build());
//
//        player.getInventory().setItem(6, ItemBuilder.of(Material.CARPET).data((short) 3).name("§eBetter View").build());
//        player.getInventory().setItem(7, ItemBuilder.of(Material.SKULL_ITEM).name("§eOnline Staff").build());
//        if (player.hasMetadata("vanished")){
//            player.getInventory().setItem(8, ItemBuilder.of(Material.INK_SACK).data((short) 8).name(ChatColor.YELLOW + "Become Invisible").build());
//
//        } else if (!player.hasMetadata("vanished")){
//            player.getInventory().setItem(8, ItemBuilder.of(Material.INK_SACK).data((short) 10).name("§eBecome Visible").build());
//
//        }
//
//        player.updateInventory();
//    }
//
//    public void vanish(Player player) {
//        player.setMetadata("vanished", new FixedMetadataValue(Foxtrot.getInstance(), "vanished"));
//
//        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
//            online.hidePlayer(player);
//        }
//    }
//
//    public void unvanish(Player player) {
//        player.removeMetadata("vanished", Foxtrot.getInstance());
//        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
//            online.showPlayer(player);
//        }
//    }
//
//    public void openOnlineStaff(Player player) {
//        new Menu() {
//            @Override
//            public String getTitle(Player player) {
//                return ChatColor.YELLOW + "Online Staff";
//            }
//
//            @Override
//            public Map<Integer, Button> getButtons(Player player) {
//                Map<Integer, Button> buttons = new HashMap<>();
//
//                List<User> profileList = new ArrayList<>();
//
//                for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers().stream().filter(other -> User.getByPlayer(other.getPlayer()).getRank().getWeight() >= Rank
//                        .getByName("Trial-Mod").getWeight()).collect(Collectors.toList())) {
//                    profileList.add(User.getByPlayer(otherPlayer));
//                }
//
//                profileList.sort(Comparator.comparingInt(profile -> profile.getRank().getWeight()));
//                Collections.reverse(profileList);
//
//                for (User profile : profileList) {
//                    buttons.put(buttons.size(), new Button() {
//
//                        @Override
//                        public String getName(Player player) {
//                            return ColorUtils.Color(profile.getRank().getPrefix() + profile.getName());
//                        }
//
//
//                        @Override
//                        public Material getMaterial(Player player) {
//                            return Material.SKULL_ITEM;
//                        }
//
//
//                        @Override
//                        public List<String> getDescription(Player player) {
//                            List<String> toReturn = new ArrayList<>();
//
//                            Player bukkitPlayer = Foxtrot.getInstance().getServer().getPlayer(profile.getUuid());
//
//                            long playtimeTime = Foxtrot.getInstance().getPlaytimeMap().getPlaytime(profile.getUuid());
//
//                            if (bukkitPlayer != null && player.canSee(bukkitPlayer)) {
//                                playtimeTime += Foxtrot.getInstance().getPlaytimeMap().getCurrentSession(bukkitPlayer.getUniqueId()) / 1000;
//                            }
//                            toReturn.add("&7&m--------------------------");
//                            toReturn.add("&eRank&7: &f" + profile.getRank().getColoredName());
//                            toReturn.add("&ePlaytime&7: &f" + TimeUtils.formatIntoDetailedString((int)playtimeTime));
//                            toReturn.add("&7&m--------------------------");
//                            return toReturn;
//                        }
//                    });
//                }
//
//                return buttons;
//            }
//        }.openMenu(player);
//    }
//}