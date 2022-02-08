package net.frozenorb.foxtrot.misc.crackers;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Data
public class Cracker {

    @Getter private static List<Cracker> crackers = new ArrayList<>();
    private List<ItemStack> items = new ArrayList<>();
    private List<String> commands = new ArrayList<>();

    private String name, display;

    public Cracker(String name) {
        this.name = name;
    }

    public static void loadCrackers() {
        ConfigurationSection section = Foxtrot.getInstance().getCrackerConfig().getConfiguration().getConfigurationSection("crackers");

        section.getKeys(false).forEach(key -> {
            Cracker p = new Cracker(key);
            Foxtrot.getInstance().getCrackerConfig().getConfiguration().getStringList("crackers." + key + ".items").forEach(str -> p.getItems().add(ItemUtils.deserialize(str)));
            Foxtrot.getInstance().getCrackerConfig().getConfiguration().getStringList("crackers." + key + ".commands").forEach(str -> p.getCommands().add(str));
            crackers.add(p);
        });
    }
}
