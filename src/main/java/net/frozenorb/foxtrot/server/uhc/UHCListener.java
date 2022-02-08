package net.frozenorb.foxtrot.server.uhc;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.util.HiddenStringUtils;
import net.frozenorb.foxtrot.util.MetaUtil;
import cc.fyre.proton.util.ItemBuilder;
import cc.fyre.proton.util.PlayerUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerHealthChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;

public class UHCListener implements Listener {

    private static final String BELOW_NAME_OBJECTIVE_NAME = "BelowName";
    private static final char HEART_CHAR = '❤';
    private static final DecimalFormat HEARTS_FORMAT = new DecimalFormat("#.#");

    private static Field aField = null;
    private static Field bField = null;
    private static Field cField = null;
    private static Field dField = null;

    private Random r = new Random();

    static {
        try {
            aField = PacketPlayOutScoreboardScore.class.getDeclaredField("a");
            aField.setAccessible(true);

            bField = PacketPlayOutScoreboardScore.class.getDeclaredField("b");
            bField.setAccessible(true);

            cField = PacketPlayOutScoreboardScore.class.getDeclaredField("c");
            cField.setAccessible(true);

            dField = PacketPlayOutScoreboardScore.class.getDeclaredField("d");
            dField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private Map<UUID, Long> deathTime = new HashMap<>();
    ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE, 1);
    ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

    public UHCListener() {
        ItemMeta itemMeta = goldenHead.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add(HiddenStringUtils.encodeString("goldenHead"));

        itemMeta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Golden Head");

        itemMeta.setLore(lore);

        goldenHead.setItemMeta(itemMeta);

        ShapedRecipe recipe = new ShapedRecipe(goldenHead);
        recipe.shape("***", "*H*", "***");
        recipe.setIngredient('*', Material.GOLD_INGOT);
        recipe.setIngredient('H', new MaterialData(Material.SKULL_ITEM, (byte) 3));

        Bukkit.getServer().addRecipe(recipe);

//        ShapedRecipe recipe = new ShapedRecipe(goldenHead);
//        recipe.shape(
//                "NNN",
//                "NAN",
//                "NNN"
//        );
//        recipe.setIngredient('N', Material.GOLD_INGOT);
//        recipe.setIngredient('A', Material.SKULL_ITEM);

//        Bukkit.addRecipe(recipe);
    }

    private void init(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        Objective objective = scoreboard.registerNewObjective(BELOW_NAME_OBJECTIVE_NAME, Criterias.HEALTH);
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective.setDisplayName(ChatColor.DARK_RED.toString() + HEART_CHAR);

        Bukkit.getScheduler().runTask(Foxtrot.getInstance(), () -> {
            updateAllTo(player);
            updateToAll(player);
        });
    }

    private void updateToAll(Player player) {
        Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

        // not yet initialized
        if (objective == null) {
            return;
        }

        try {
            PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();
            aField.set(packet, player.getName());
            bField.set(packet, BELOW_NAME_OBJECTIVE_NAME);
            cField.set(packet, getHealth(player));
            dField.set(packet, 0);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getScoreboard() == null || onlinePlayer.getScoreboard().getObjective(DisplaySlot.BELOW_NAME) == null) {
                    continue;
                }

                ((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAllTo(Player player) {
        Objective belowNameObjective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

        // not yet initialized
        if (belowNameObjective == null) {
            return;
        }

        try {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (player.getScoreboard() == null || player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME) == null) {
                    continue;
                }

                PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();
                aField.set(packet, onlinePlayer.getName());
                bField.set(packet, BELOW_NAME_OBJECTIVE_NAME);
                cField.set(packet, getHealth(onlinePlayer));
                dField.set(packet, 0);

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @EventHandler
//    public void onCraft(CraftItemEvent event) {
//        if (isOldCrappleRecipe(event.getRecipe())) {
//            event.setCancelled(true);
//        }
//    }
//
//    @EventHandler
//    public void onPrepare(PrepareItemCraftEvent event) {
//        if (isOldCrappleRecipe(event.getRecipe())) {
//            ItemStack result = ItemBuilder.of(Material.WOOL).data(DyeColor.RED.getWoolData()).name("&cThis recipe is disabled.").addToLore("&eUse gold nuggets instead of ingots.").build();
//
//            event.getInventory().setResult(result);
//        }
//    }
//
//    private boolean isOldCrappleRecipe(Recipe r) {
//        if (!(r instanceof ShapedRecipe)) {
//            return false;
//        }
//
//        ShapedRecipe recipe = (ShapedRecipe) r;
//
//        char[] goldChars = ("abc" + "df" + "ghi").toCharArray();
//        char appleChar = 'e';
//
//        for (char c : goldChars) {
//            if (recipe.getIngredientMap().get(c) == null) {
//                return false;
//            }
//
//            if (recipe.getIngredientMap().get(c).getType() != Material.GOLD_INGOT) {
//                return false;
//            }
//        }
//
//        return recipe.getIngredientMap().get(appleChar).getType() == Material.APPLE;
//    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> init(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onHealthChange(PlayerHealthChangeEvent event) {
        updateToAll(event.getPlayer());
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (item.getType() != Material.GOLDEN_APPLE || item.isSimilar(goldenHead)) {
            return;
        }

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 11, 1), true);
        double statement = 20.0 - 1.0;
        if (player.getHealth() > statement) {
            player.setHealth(20.0);
        } else {
            player.setHealth(player.getHealth() + 1.0);
        }
    }


    @EventHandler
    public void onHeadEat(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        if (player.getItemInHand().getType() == Material.SKULL_ITEM) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 7, 1), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1), true);
            ItemStack stack = player.getItemInHand();
            stack.setAmount(stack.getAmount() - 1);
            player.setItemInHand(stack);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            Player damaged = (Player) event.getEntity();
            Player damager = PlayerUtils.getDamageSource(event.getDamager());

            if (damager != null) {
                // we have to delay this for the damage to apply
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (!deathTime.containsKey(damaged.getUniqueId()) || (System.currentTimeMillis() - deathTime.get(damaged.getUniqueId()) > 200L)) {
                            damager.sendMessage(damaged.getDisplayName() + ChatColor.YELLOW + " is now at " + ChatColor.RED + formatHearts(damaged, true) + ChatColor.YELLOW + ".");
                        }
                    }

                }.runTask(Foxtrot.getInstance());
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        if (e.getItem() == null || e.getItem().getItemMeta() == null || e.getItem().getItemMeta().getLore() == null || e.getItem().getItemMeta().getLore().get(0) == null) {
            return;
        }
        if (e.getItem().getType() == Material.GOLDEN_APPLE && e.getItem().getDurability() == 1) {
            e.setCancelled(true);
            return;
        }

        if (HiddenStringUtils.hasHiddenString(e.getItem().getItemMeta().getLore().get(0))) {
            String data = HiddenStringUtils.extractHiddenString(e.getItem().getItemMeta().getLore().get(0));
            Player player = e.getPlayer();

            if (data.equalsIgnoreCase("goldenHead")) {
                e.setCancelled(true);
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 2), true);
//                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, (20 * 60) * 2, 0));
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 7, 1), true);
                ItemStack stack = e.getItem();
                stack.setAmount(stack.getAmount() - 1);
                e.getPlayer().setItemInHand(stack);
                double statement = 20.0 - 2.0;
                if (player.getHealth() > statement) {
                    e.getPlayer().setHealth(20.0);
                } else {
                    e.getPlayer().setHealth(e.getPlayer().getHealth() + 2.0);
                }
            }
        }
    }


    @EventHandler
    public void onRegen(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED) {
            return;
        }

        Player player = (Player) event.getEntity();
        Team team = LandBoard.getInstance().getTeam(player.getLocation());

        if (team == null || !team.isMember(player.getUniqueId()) || SpawnTagHandler.isTagged(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void enderpearlDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getDamager() instanceof EnderPearl) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        deathTime.put(event.getEntity().getUniqueId(), System.currentTimeMillis());
        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> deathTime.remove(event.getEntity().getUniqueId()), 20L);

        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner(event.getEntity().getName());
        skullMeta.setDisplayName(ChatColor.GOLD + event.getEntity().getName() + "'s head");
        itemStack.setItemMeta(skullMeta);

        event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), itemStack);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLeafDecay(LeavesDecayEvent event) {
        if (event.getBlock().getType() == Material.LEAVES) {
            if ((Math.random() * 100) <= 3) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        } else if (event.getBlock().getType() == Material.LEAVES_2) {
            if ((Math.random() * 100) <= 3) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        int multiplier = 1;

        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.SHEARS && event.getPlayer().getItemInHand().hasItemMeta() && event.getPlayer().getItemInHand().getItemMeta().hasDisplayName() && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains(ChatColor.GREEN.toString() + ChatColor.BOLD)) {
            multiplier = 3;
        }

        if (event.getBlock().getType() == Material.LEAVES) {
            if ((Math.random() * 100) <= 7 * multiplier) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        } else if (event.getBlock().getType() == Material.LEAVES_2) {
            if ((Math.random() * 100) <= 7 * multiplier) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }

    private static String formatHearts(Player player, boolean heartChar) {
        return HEARTS_FORMAT.format(getHearts(player)) + (heartChar ? HEART_CHAR : "");
    }

    private static double getHearts(Player player) {
        return getHearts0(player.getHealth() + ((CraftPlayer) player).getHandle().getAbsorptionHearts());
    }

    private static int getHealth(Player player) {
        return (int) Math.ceil(player.getHealth() + ((CraftPlayer) player).getHandle().getAbsorptionHearts());
    }

    private static double getHearts0(double health) {
        return (double) Math.round((Math.ceil(health) / 2) * 2) / 2;
    }

}
