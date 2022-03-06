package rip.orbit.hcteams.loot;

import cc.fyre.proton.Proton;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.MaterialData;
import rip.orbit.hcteams.HCF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AirdropListener implements Listener {

    private final Map<UUID, ItemStack> lastItem = new ConcurrentHashMap<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            lastItem.put(event.getPlayer().getUniqueId(), event.getPlayer().getItemInHand());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void place(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (lastItem.get(event.getPlayer().getUniqueId()) == null || !lastItem.get(event.getPlayer().getUniqueId()).isSimilar(HCF.getInstance().getAirdropHandler().getAirdropItem(1)))
            return;

        event.setCancelled(true);

        Block block = event.getBlock();
        Location loc = block.getLocation();

        for (int i = 1; i < 30; i++) {
            if (block.getWorld().getBlockAt(loc.clone().add(0, i, 0)).getType() != Material.AIR) {
                player.sendMessage(ChatColor.RED + "There must be 30 blocks above this airdrop to deploy.");
                return;
            }
        }

        Block below = loc.getWorld().getBlockAt(loc.clone().add(0, -1, 0));
        if (below.getType().equals(Material.AIR) || below.isLiquid() || below.getType().isTransparent() || !isFull(below))
            return;

        try {
            if (player.getItemInHand().getAmount() <= 1)
                player.getInventory().remove(player.getItemInHand());
            else
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);

            Firework f = block.getWorld().spawn(loc.clone().add(0.5, 0, 0.5), Firework.class);
            FireworkMeta fm = f.getFireworkMeta();
            fm.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.RED).trail(true).flicker(true).build());
            f.setFireworkMeta(fm);

            loc.getWorld().spawnFallingBlock(loc.clone().add(0.5, 30, 0.5), Material.DISPENSER, new MaterialData(Material.DISPENSER).getData());
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void change(EntityChangeBlockEvent event) {

        if (!(event.getEntity() instanceof FallingBlock))
            return;

        if (event.getTo() != Material.DISPENSER)
            return;

        event.setCancelled(true);
        event.getEntity().remove();
        event.getBlock().setType(Material.DISPENSER);
        Dispenser dispenser = (Dispenser) event.getBlock().getState();
        dispenser.setData(new org.bukkit.material.Dispenser(BlockFace.UP));
        Bukkit.getScheduler().runTaskLater(HCF.getInstance(), () -> dispenser.getInventory().setContents(getLoot()), 1);
    }

    public ItemStack[] getLoot() {
        List<ItemStack> loot = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            loot.add(i, HCF.getInstance().getAirdropHandler().getAirdropLoot().get(Proton.RANDOM.nextInt(HCF.getInstance().getAirdropHandler().getAirdropLoot().size())));
        }

        return loot.toArray(new ItemStack[9]);
    }

    public boolean isFull(Block below) {
        boolean full;
        switch (below.getType()) {
            case BED_BLOCK:
            case STEP:
            case WOOD_STEP:
            case GOLD_PLATE:
            case WOOD_PLATE:
            case STONE_PLATE:
            case IRON_PLATE:
            case STONE_BUTTON:
            case WOOD_BUTTON:
            case TRAP_DOOR:
            case ENCHANTMENT_TABLE:
            case SOUL_SAND:
            case BREWING_STAND:
            case ENDER_PORTAL_FRAME:
            case DRAGON_EGG:
            case TRIPWIRE_HOOK:
            case COCOA:
            case CHEST:
            case ENDER_CHEST:
            case TRAPPED_CHEST:
            case FLOWER_POT:
            case ANVIL:
            case REDSTONE_COMPARATOR:
            case DAYLIGHT_DETECTOR:
            case HOPPER:
                full = false;
                break;
            default:
                full = true;
        }

        return full;
    }
}