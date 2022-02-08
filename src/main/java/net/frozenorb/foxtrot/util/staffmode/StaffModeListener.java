//package net.frozenorb.foxtrot.util.staffmode;
//
//import com.comphenix.protocol.PacketType;
//import com.comphenix.protocol.ProtocolLibrary;
//import com.comphenix.protocol.events.PacketAdapter;
//import com.comphenix.protocol.events.PacketEvent;
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.team.claims.LandBoard;
//import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
//import cc.fyre.proton.menu.Button;
//import cc.fyre.proton.menu.Menu;
//import cc.fyre.proton.nametag.FrozenNametagHandler;
//import cc.fyre.proton.Proton;
//import cc.fyre.proton.util.ItemBuilder;
//import net.minecraft.server.v1_7_R4.*;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.block.Chest;
//import org.bukkit.block.DoubleChest;
//import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
//import org.bukkit.entity.Player;
//import org.bukkit.event.Event;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.block.Action;
//import org.bukkit.event.block.BlockBreakEvent;
//import org.bukkit.event.block.BlockPlaceEvent;
//import org.bukkit.event.entity.*;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.event.inventory.InventoryDragEvent;
//import org.bukkit.event.inventory.InventoryOpenEvent;
//import org.bukkit.event.inventory.InventoryType;
//import org.bukkit.event.player.*;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.potion.PotionEffectType;
//import pw.navigations.qUtilities.api.UtilitiesAPI;
//
//import java.lang.reflect.Field;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class StaffModeListener implements Listener {
//
//
//    @EventHandler
//    public void onJoin(PlayerJoinEvent event) {
//        Player player = event.getPlayer();
//
//        if (player.hasPermission("neutron.staff")) {
//            new StaffMode(player);
//        }
//
//        for (Player other : Bukkit.getOnlinePlayers()) {
//            if (other == player) return;
//            if (other.hasMetadata("vanished")) {
//                player.hidePlayer(other);
//            }
//        }
//    }
//
//    @EventHandler
//    public void onQuit(PlayerQuitEvent event) {
//        Player player = event.getPlayer();
//        StaffMode staffMode = new StaffMode(player);
//
//        if (player.hasMetadata("modmode")) {
//            staffMode.destroy(player);
//        }
//    }
//
//    @EventHandler
//    public void onInteract(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//
//        if(!player.hasMetadata("modmode")) {
//            return;
//        }
//
//        try {
//            if(event.getClickedBlock().getType().equals(Material.FENCE_GATE)
//                    || event.getClickedBlock().getType().equals(Material.WOOD_BUTTON)
//                    || event.getClickedBlock().getType().equals(Material.STONE_BUTTON)
//                    || event.getClickedBlock().getType().equals(Material.GOLD_PLATE)
//                    || event.getClickedBlock().getType().equals(Material.IRON_PLATE)
//                    || event.getClickedBlock().getType().equals(Material.STONE_BUTTON)
//                    || event.getClickedBlock().getType().equals(Material.WOOD_PLATE)
//                    || event.getClickedBlock().getType().equals(Material.STONE_PLATE) || event.getClickedBlock().getType().equals(Material.WOOD_DOOR) || event.getClickedBlock().getType().equals(Material.WOODEN_DOOR)
//                    || event.getClickedBlock().getType().equals(Material.TRAP_DOOR))
////					|| event.getClickedBlock().getType().equals(Material.CHEST))
//            {
//                if(event.getClickedBlock().getType().equals(Material.CHEST)) {
////					event.setCancelled(true);
////					ActivateChest(event.getPlayer(), true, true, event.getClickedBlock().getLocation().getBlockX(), event.getClickedBlock().getLocation().getBlockY(), event.getClickedBlock().getLocation().getBlockZ());
//                }
//                event.setCancelled(true);
//
//            }
//        }catch (NullPointerException ignored) {
//
//        }
//
//        if (event.getItem() == null || event.getItem().getItemMeta() == null || event.getItem().getItemMeta().getDisplayName() == null)
//            return;
//
//        StaffMode staffMode = StaffMode.getStaffModeMap().get(player.getUniqueId());
//
//        event.setCancelled(true);
//        event.setUseInteractedBlock(Event.Result.DENY);
//        event.setUseItemInHand(Event.Result.DENY);
//
//        if (event.getClickedBlock() != null && event.getClickedBlock().getType() != null && event.getClickedBlock().getType() == Material.CHEST) {
//            Chest chest = (Chest) event.getClickedBlock().getState();
//            player.openInventory(chest.getBlockInventory());
//        }
//
//        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Online Staff")) {
//            staffMode.openOnlineStaff(player);
//        }
//
//        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Random Teleport")) {
//
//            List<Player> editedOnlinePlayers = new ArrayList<>();
//
//            for(Player p : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
//                if(UtilitiesAPI.getRank(p.getUniqueId()).equalsIgnoreCase("Partner")
//                        || UtilitiesAPI.getRank(p.getUniqueId()).equalsIgnoreCase("Media-Owner")
//                        || UtilitiesAPI.getRank(p.getUniqueId()).equalsIgnoreCase("Owner")) {
//                    continue;
//                }
//
//                if(LandBoard.getInstance().getTeam(p.getLocation()) != null && LandBoard.getInstance().getTeam(p.getLocation()).hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
//                    continue;
//                }
//                editedOnlinePlayers.add(p);
//            }
//
//            editedOnlinePlayers.remove(player);
//
//            if(editedOnlinePlayers.size() == 0) {
//                player.sendMessage(ChatColor.RED + "No one online to teleport to.");
//                return;
//            }
//
//            Player random = (editedOnlinePlayers.get(Proton.RANDOM.nextInt(editedOnlinePlayers.size())));
//
//
//            player.teleport(random);
//            player.sendMessage(ChatColor.GOLD + "Teleporting you to " + UtilitiesAPI.colorName(random));
//        }
//
//        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Become Visible")) {
//            staffMode.unvanish(player);
//            player.getInventory().setItem(8, ItemBuilder.of(Material.INK_SACK).data((short) 8).name(ChatColor.YELLOW + "Become Invisible").build());
//            player.updateInventory();
//        }
//
//        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Become Invisible")) {
//            staffMode.vanish(player);
//            player.getInventory().setItem(8, ItemBuilder.of(Material.INK_SACK).data((short) 10).name(ChatColor.YELLOW + "Become Visible").build());
//            player.updateInventory();
//        }
//
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onPlayerPotionEffect(PotionEffectAddEvent event) {
//        if(event.getEntity() instanceof Player) {
//            Player player = (Player) event.getEntity();
//
//            if(!player.hasMetadata("moddmode")) {
//                if(event.getEffect().getType().equals(PotionEffectType.INVISIBILITY)) {
//                    FrozenNametagHandler.reloadPlayer(player);
//                    FrozenNametagHandler.reloadOthersFor(player);
//                }
//            }
//        }
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onPlayerLoosePotionEffect(PotionEffectRemoveEvent event) {
//        if(event.getEntity() instanceof Player) {
//            Player player = (Player) event.getEntity();
//
//            if(!player.hasMetadata("moddmode")) {
//                if(event.getEffect().getType().equals(PotionEffectType.INVISIBILITY)) {
//                    FrozenNametagHandler.reloadPlayer(player);
//                    FrozenNametagHandler.reloadOthersFor(player);
//                }
//            }
//        }
//    }
//
//    @EventHandler
//    public void onPlayerInteract(PlayerInteractEntityEvent event) {
//        if (event.getRightClicked() instanceof Player) {
//            Player player = event.getPlayer();
//            Player rightClicked = (Player) event.getRightClicked();
//            ItemStack itemStack = player.getItemInHand();
//
//            if (itemStack == null || itemStack.getItemMeta() == null || itemStack.getItemMeta().getDisplayName() == null || !player.hasMetadata("modmode"))
//                return;
//
//            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Inspect Inventory")) {
//                new Menu() {
//
//                    @Override
//                    public String getTitle(Player player) {
//                        return rightClicked.getName();
//                    }
//
//                    @Override
//                    public Map<Integer, Button> getButtons(Player player) {
//                        Map<Integer, Button> buttons = new HashMap<>();
//
//                        for (ItemStack stack : rightClicked.getInventory().getContents()) {
//                            buttons.put(buttons.size(), Button.fromItem(stack));
//                        }
//
//                        for (ItemStack stack : rightClicked.getInventory().getArmorContents()) {
//                            buttons.put(buttons.size(), Button.fromItem(stack));
//                        }
//
//                        return buttons;
//                    }
//                }.openMenu(player);
//            }
//
//            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Freeze Player")) {
//                Bukkit.dispatchCommand(player, "freeze " + rightClicked.getName());
//            }
//        }
//    }
//
//
//    @EventHandler
//    public void onCommand(PlayerCommandPreprocessEvent event){
//        Player player = event.getPlayer();
//        if (event.getMessage().equalsIgnoreCase("/vanish")){
//            if (player.hasPermission("hcteams.command.vanish") && player.hasMetadata("modmode") ){
//                if (player.hasMetadata("vanished")) {
//                    player.getInventory().setItem(8, ItemBuilder.of(Material.INK_SACK).data((short) 8).name(ChatColor.YELLOW + "Become Invisible").build());
//                    player.updateInventory();
//                } else if (!player.hasMetadata("vanished")) {
//                    player.getInventory().setItem(8, ItemBuilder.of(Material.INK_SACK).data((short) 10).name(ChatColor.YELLOW + "Become Visible").build());
//                    player.updateInventory();
//
//                }
//            }
//
//        }
//
//
//    }
//
//    @EventHandler
//    public void onDamage(EntityDamageEvent event) {
//        if (event.getEntity().hasMetadata("modmode")) event.setCancelled(true);
//    }
//
//    @EventHandler
//    public void onInventoryInteract(InventoryClickEvent event) {
//        if (event.getWhoClicked().hasMetadata("modmode")) event.setCancelled(true);
//    }
//
//    @EventHandler
//    public void onDamage(EntityDamageByEntityEvent event) {
//        if (event.getDamager().hasMetadata("modmode")) event.setCancelled(true);
//    }
//
//    @EventHandler
//    public void onBlockBreak(BlockBreakEvent event) {
//        if (event.getPlayer().hasMetadata("modmode")) event.setCancelled(true);
//    }
//
//    @EventHandler
//    public void onBlockPlace(BlockPlaceEvent event) {
//        if (event.getPlayer().hasMetadata("modmode")) event.setCancelled(true);
//    }
//
//    @EventHandler
//    public void onDrop(PlayerDropItemEvent event) {
//        if (event.getPlayer().hasMetadata("modmode")) event.setCancelled(true);
//    }
//
//    @EventHandler
//    public void onPickup(PlayerPickupItemEvent event) {
//        if (event.getPlayer().hasMetadata("modmode")) event.setCancelled(true);
//    }
//
//    @EventHandler
//    public void onInvDrag(InventoryDragEvent event) {
//        if (event.getWhoClicked().hasMetadata("modmode")) event.setCancelled(true);
//    }
//
//
//    public boolean ActivateChest(Player p, boolean anychest, boolean silentchest, int x, int y, int z) {
//        EntityPlayer player = ((CraftPlayer)p).getHandle();
//        World world = player.world;
//        Object chest = (TileEntityChest)world.getTileEntity(x, y, z);
//        if (chest == null) {
//            return true;
//        } else {
//            int id = net.minecraft.server.v1_7_R4.Block.getId(world.getType(x, y, z));
//            if (!anychest) {
//                if (world.getType(x, y + 1, z).c()) {
//                    return true;
//                }
//
//                if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x - 1, y, z)) == id && world.getType(x - 1, y + 1, z).c()) {
//                    return true;
//                }
//
//                if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x + 1, y, z)) == id && world.getType(x + 1, y + 1, z).c()) {
//                    return true;
//                }
//
//                if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x, y, z - 1)) == id && world.getType(x, y + 1, z - 1).c()) {
//                    return true;
//                }
//
//                if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x, y, z + 1)) == id && world.getType(x, y + 1, z + 1).c()) {
//                    return true;
//                }
//            }
//
//            if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x - 1, y, z)) == id) {
//                chest = new InventoryLargeChest("Large chest", (TileEntityChest)world.getTileEntity(x - 1, y, z), (IInventory)chest);
//            }
//
//            if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x + 1, y, z)) == id) {
//                chest = new InventoryLargeChest("Large chest", (IInventory)chest, (TileEntityChest)world.getTileEntity(x + 1, y, z));
//            }
//
//            if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x, y, z - 1)) == id) {
//                chest = new InventoryLargeChest("Large chest", (TileEntityChest)world.getTileEntity(x, y, z - 1), (IInventory)chest);
//            }
//
//            if (Block.getId(world.getType(x, y, z + 1)) == id) {
//                chest = new InventoryLargeChest("Large chest", (IInventory)chest, (TileEntityChest)world.getTileEntity(x, y, z + 1));
//            }
//
//            boolean returnValue = true;
//            if (!silentchest) {
//                player.openContainer((IInventory)chest);
//            } else {
//                try {
//                    int windowId = 0;
//
//                    try {
//                        Field windowID = player.getClass().getDeclaredField("containerCounter");
//                        windowID.setAccessible(true);
//                        windowId = windowID.getInt(player);
//                        windowId = windowId % 100 + 1;
//                        windowID.setInt(player, windowId);
//                    } catch (NoSuchFieldException var14) {
//                    }
//
//                    player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(windowId, 0, ((IInventory)chest).getInventoryName(), ((IInventory)chest).getSize(), false));
//                    player.activeContainer = new SilentContainerChest(player.inventory, (IInventory)chest);
//                    player.activeContainer.windowId = windowId;
//                    player.activeContainer.addSlotListener(player);
//
//                    returnValue = false;
//                } catch (Exception var15) {
//                    var15.printStackTrace();
//                    p.sendMessage(ChatColor.RED + "Error while sending silent chest.");
//                }
//            }
//
//
//            return returnValue;
//        }
//    }
//}