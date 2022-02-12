package rip.orbit.hcteams.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.commands.staff.EOTWCommand;

public class MapListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        double multiplier = HCF.getInstance().getMapHandler().getBaseLootingMultiplier();

        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();

            if (player.getItemInHand() != null && player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {
                switch (player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)) {
                    case 1:
                        multiplier = HCF.getInstance().getMapHandler().getLevel1LootingMultiplier();
                        break;
                    case 2:
                        multiplier = HCF.getInstance().getMapHandler().getLevel2LootingMultiplier();
                        break;
                    case 3:
                        multiplier = HCF.getInstance().getMapHandler().getLevel3LootingMultiplier();
                        break;
                    default:
                        break;
                }
            }
        }

        event.setDroppedExp((int) Math.ceil(event.getDroppedExp() * multiplier));
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true) // This is actually 'Lowest', but Bukkit calls listeners LOWEST -> HIGHEST, so HIGHEST is what's actually called last. #BukkitBeLike
    public void onBlockBreak(BlockBreakEvent event) {
        if (!HCF.getInstance().getMapHandler().isFastSmeltEnabled()
                || event.getPlayer().getItemInHand() == null
                || !event.getPlayer().getItemInHand().getType().name().contains("PICKAXE")
                || event.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
            return;
        }

        Material blockType = event.getBlock().getType();

        if (blockType == Material.GOLD_ORE || blockType == Material.IRON_ORE) {
            ItemStack drop;

            if (blockType == Material.GOLD_ORE) {
                drop = new ItemStack(Material.GOLD_INGOT);
                HCF.getInstance().getGoldMinedMap().setMined(event.getPlayer().getUniqueId(), HCF.getInstance().getGoldMinedMap().getMined(event.getPlayer().getUniqueId()) + 1);
            } else {
                drop = new ItemStack(Material.IRON_INGOT);
                HCF.getInstance().getIronMinedMap().setMined(event.getPlayer().getUniqueId(), HCF.getInstance().getIronMinedMap().getMined(event.getPlayer().getUniqueId()) + 1);
            }

            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            event.setCancelled(true);
            event.getPlayer().giveExp(4);
            event.getBlock().setType(Material.AIR);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        
        if (!EOTWCommand.realFFAStarted()) {
            return;
        }
        
        String message = event.getMessage().toLowerCase();
        if (!message.startsWith("/kit") && !message.startsWith("/showkits") && !message.startsWith("/gkit")) {
            return;
        }

        if (event.getPlayer().isOp()) {
            event.getPlayer().sendMessage(ChatColor.RED + "You bypassed the FFA Kits block because you're op.");
            return;
        }

        event.getPlayer().sendMessage(ChatColor.RED + "Kits are disabled during FFA.");
        event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof ThrownExpBottle)) {
            return;
        }
        
        ThrownExpBottle thrown = (ThrownExpBottle) event.getEntity();
        ProjectileSource shooter = thrown.getShooter();
        
        if (!(shooter instanceof Player)) {
            return;
        }
        
        ItemStack inHand = ((Player) shooter).getItemInHand();
        if (inHand != null && inHand.getType() == Material.EXP_BOTTLE
                && inHand.hasItemMeta()
                && inHand.getItemMeta().hasLore()
                && inHand.getItemMeta().getLore().size() == 1) {
            String number = ChatColor.stripColor(inHand.getItemMeta().getLore().get(0)).replace("XP: ", "").replaceAll(",", "");
            Integer xp = Integer.valueOf(number);
            thrown.setMetadata("XP", new FixedMetadataValue(HCF.getInstance(), xp));
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSplash(ExpBottleEvent event) {
        ThrownExpBottle bottle = event.getEntity();
        if (bottle.hasMetadata("XP")) {
            event.setExperience(bottle.getMetadata("XP").get(0).asInt()); 
            bottle.removeMetadata("XP", HCF.getInstance());
        }
    }


}