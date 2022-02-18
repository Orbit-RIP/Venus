package rip.orbit.hcteams.wanderer;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.claims.LandBoard;
import rip.orbit.hcteams.util.CC;
import rip.orbit.hcteams.util.ConfigFile;
import rip.orbit.hcteams.wanderer.menu.WandererMenu;

import java.util.HashSet;
import java.util.Set;

public class WandererHandler {

    private static final String WANDERER_NAME = "Wanderer";
    private static final String WANDERER_UUID = "";
    private static final int WANDERER_RANGE_MIN = 300;
    private static final int WANDERER_RANGE_MAX = 300;

    private final Set<WandererButton> itemStackSet = new HashSet<>();
    private final ConfigFile configFile;


    public WandererHandler() {
        configFile = new ConfigFile(HCF.getInstance(), "wanderer",
                HCF.getInstance().getDataFolder().getAbsolutePath());

        loadItems();
    }

    public void saveWanderer() {
    }

    public void spawnWanderer() {
        int x = (int) (-WANDERER_RANGE_MIN + (Math.random() * ((WANDERER_RANGE_MAX - (-WANDERER_RANGE_MIN)))));
        int z = (int) (-WANDERER_RANGE_MIN + (Math.random() * ((WANDERER_RANGE_MAX - (-WANDERER_RANGE_MIN)))));

        World world = Bukkit.getWorld("world");
        Location location = new Location(world, x, world.getHighestBlockYAt(x, z), z);
        Team team = LandBoard.getInstance().getTeam(location);

    }

    @EventHandler
    public void onNpcClick(NPCRightClickEvent event) {

        if (!event.getNPC().getName().contains(WANDERER_NAME))
            return;

        new WandererMenu().openMenu(event.getClicker());
    }

    public void loadItems() {
        // TODO: 18.02.2022 cleanup later
        //Borrowed from CitadelHandler.java
        for (String sec : configFile.getConfiguration().getConfigurationSection("loot").getKeys(false)) {
            String main = "loot.";
            ItemStack itemStack = new ItemStack(Material.valueOf(configFile.getString(main + sec + ".Material")));
            ItemMeta meta = itemStack.getItemMeta();
            if (configFile.contains("loot." + sec + ".Name")) {
                meta.setDisplayName(CC.translate(configFile.getString(main + sec + ".Name").replaceAll("§", "&")));
            }

            if (configFile.contains("loot." + sec + ".Lore"))
                meta.setLore(CC.translate(configFile.getStringList(main + sec + ".Lore")));

            itemStack.setDurability(((short) configFile.getInt(main + sec + ".Data")));
            itemStack.setAmount(configFile.getInt(main + sec + ".Amount"));
            itemStack.setItemMeta(meta);
            if (configFile.contains("loot." + sec + ".Enchants")) {
                for (String s : configFile.getConfigurationSection(main + sec + ".Enchants").getKeys(false)) {
                    try {
                        itemStack.addEnchantment(Enchantment.getByName(configFile.getString(main + sec + ".Enchants." + s + ".Type")),
                                configFile.getInt(main + sec + ".Enchants." + s + ".Level"));
                    } catch (IllegalArgumentException e) {
                        itemStack.addUnsafeEnchantment(Enchantment.getByName(configFile.getString(main + sec + ".Enchants." + s + ".Type")),
                                configFile.getInt(main + sec + ".Enchants." + s + ".Level"));

                    }
                }
            }
            itemStackSet.add(new WandererButton(itemStack, configFile.getInt(main + sec + ".Price")));
        }
    }

    public Set<WandererButton> getItemStackSet() {
        return itemStackSet;
    }
}
