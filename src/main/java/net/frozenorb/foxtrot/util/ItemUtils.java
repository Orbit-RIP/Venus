package net.frozenorb.foxtrot.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ItemUtils {

    public static boolean hasLore(ItemStack itemStack) {
        return itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getLore() != null && !itemStack.getItemMeta().getLore().isEmpty();
    }

    public static ItemStack deserialize(String serializedItem){
        String[] strings = serializedItem.split(" ");
        Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
        String[] args;
        ItemStack item = new ItemStack(Material.AIR);
        for (String str: strings) {
            args = str.split(":");
            if(Material.matchMaterial(args[0]) != null && item.getType() == Material.AIR){
                item.setType(Material.matchMaterial(args[0]));
                if(args.length == 2) item.setDurability(Short.parseShort(args[1]));
                break;
            }
        }
        if (item.getType() == Material.AIR) {
            Bukkit.getLogger().info("Could not find a valid material for the item in \"" + serializedItem + "\"");
            return null;
        }
        for(String str:strings){
            args = str.split(":", 2);
            if(isNumber(args[0])) item.setAmount(Integer.parseInt(args[0]));
            if(args.length == 1) continue;
            if(args[0].equalsIgnoreCase("name")){
                setName(item, ChatColor.translateAlternateColorCodes('&', args[1]));
                continue;
            }
            if(args[0].equalsIgnoreCase("lore")){
                setLore(item, ChatColor.translateAlternateColorCodes('&', args[1]));
                continue;
            }
            if(args[0].equalsIgnoreCase("rgb")){
                setArmorColor(item, args[1]);
                continue;
            }
            if(args[0].equalsIgnoreCase("owner")){
                setOwner(item, args[1]);
                continue;
            }
            if(Enchantment.getByName(args[0].toUpperCase()) != null){
                enchants.put(Enchantment.getByName(args[0].toUpperCase()), Integer.parseInt(args[1]));
                continue;
            }
        }
        item.addUnsafeEnchantments(enchants);
        return item.getType().equals(Material.AIR) ? null : item;
    }

    /**
     * Checks if a {@link ItemStack} is an instant heal potion (if its type is {@link PotionType#INSTANT_HEAL})
     */
    public static final Predicate<ItemStack> INSTANT_HEAL_POTION_PREDICATE = item -> {
        if (item.getType() != Material.POTION) {
            return false;
        }

        PotionType potionType = Potion.fromItemStack(item).getType();
        return potionType == PotionType.INSTANT_HEAL;
    };

    /**
     * Checks if a {@link ItemStack} is a bowl of mushroom soup (if its type is {@link Material#MUSHROOM_SOUP})
     */
    public static final Predicate<ItemStack> SOUP_PREDICATE = item -> item.getType() == Material.MUSHROOM_SOUP;

    /**
     * Checks if a {@link ItemStack} is a debuff potion
     */
    public static final Predicate<ItemStack> DEBUFF_POTION_PREDICATE = item -> {
        if (item.getType() == Material.POTION) {
            PotionType type = Potion.fromItemStack(item).getType();
            return type == PotionType.WEAKNESS || type == PotionType.SLOWNESS
                    || type == PotionType.POISON || type == PotionType.INSTANT_DAMAGE;
        } else {
            return false;
        }
    };

    /**
     * Checks if a {@link ItemStack} is edible (if its type passes {@link Material#isEdible()})
     */
    public static final Predicate<ItemStack> EDIBLE_PREDICATE = item -> item.getType().isEdible();

    /**
     * Returns the number of stacks of items matching the predicate provided.
     *
     * @Parameter items ItemStack array to scan
     * @Parameter predicate The predicate which will be applied to each non-null temStack.
     * @return The amount of ItemStacks which matched the predicate, or 0 if {@code items} was null.
     */
    public static int countStacksMatching(ItemStack[] items, Predicate<ItemStack> predicate) {
        if (items == null) {
            return 0;
        }

        int amountMatching = 0;

        for (ItemStack item : items) {
            if (item != null && predicate.test(item)) {
                amountMatching++;
            }
        }

        return amountMatching;
    }

    public static boolean hasEmptyInventory(Player player) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                return false;
            }
        }

        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                return false;
            }
        }

        return true;
    }

    private static String getOwner(ItemStack item){
        if(!(item.getItemMeta() instanceof SkullMeta)) return null;
        return ((SkullMeta)item.getItemMeta()).getOwner();
    }

    private static void setOwner(ItemStack item, String owner){
        try{
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(owner);
            item.setItemMeta(meta);
        }catch(Exception exception){
            return;
        }
    }

    private static String getName(ItemStack item){
        if(!item.hasItemMeta()) return null;
        if(!item.getItemMeta().hasDisplayName()) return null;
        return item.getItemMeta().getDisplayName().replace(" ", "_").replace(ChatColor.COLOR_CHAR, '&');
    }

    private static void setName(ItemStack item, String name){
        name = name.replace("_", " ");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    private static String getLore(ItemStack item){
        if(!item.hasItemMeta()) return null;
        if(!item.getItemMeta().hasLore()) return null;
        StringBuilder builder = new StringBuilder();
        List<String> lore = item.getItemMeta().getLore();
        for(int ind = 0;ind<lore.size();ind++){
            builder.append((ind > 0 ? "|" : "") + lore.get(ind).replace(" ", "_").replace(ChatColor.COLOR_CHAR, '&'));
        }
        return builder.toString();
    }

    private static void setLore(ItemStack item, String lore){
        lore = lore.replace("_", " ");
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(lore.split("\\|")));
        item.setItemMeta(meta);
    }

    private static Color getArmorColor(ItemStack item){
        if(!(item.getItemMeta() instanceof LeatherArmorMeta)) return null;
        return ((LeatherArmorMeta)item.getItemMeta()).getColor();
    }

    private static void setArmorColor(ItemStack item, String str){
        try{
            String[] colors = str.split("\\|");
            int red = Integer.parseInt(colors[0]);
            int green = Integer.parseInt(colors[1]);
            int blue = Integer.parseInt(colors[2]);
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(Color.fromRGB(red, green, blue));
            item.setItemMeta(meta);
        }catch(Exception exception){
            return;
        }
    }

    private static boolean isNumber(String str){
        try{
            Integer.parseInt(str);
        }catch(NumberFormatException exception){
            return false;
        }
        return true;
    }
}