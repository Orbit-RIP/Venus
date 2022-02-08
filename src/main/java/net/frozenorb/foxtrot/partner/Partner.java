package net.frozenorb.foxtrot.partner;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaikoX
 */


@Getter @Setter @Data
public class Partner {

    @Getter private static List<Partner> partners = new ArrayList<>();
    private List<ItemStack> items = new ArrayList<>();
    private List<String> commands = new ArrayList<>();

    private String name, display;

    public Partner(String name) {
        this.name = name;
    }

    public static void loadPackages() {
        ConfigurationSection section = Foxtrot.getInstance().getPartnerConfig().getConfiguration().getConfigurationSection("packages");

        section.getKeys(false).forEach(key -> {
            Partner p = new Partner(key);
            Foxtrot.getInstance().getPartnerConfig().getConfiguration().getStringList("packages." + key + ".items").forEach(str -> p.getItems().add(ItemUtils.deserialize(str)));
            Foxtrot.getInstance().getPartnerConfig().getConfiguration().getStringList("packages." + key + ".commands").forEach(str -> p.getCommands().add(str));
            partners.add(p);
        });
    }
}
