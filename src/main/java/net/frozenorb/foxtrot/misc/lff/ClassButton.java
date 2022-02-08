package net.frozenorb.foxtrot.misc.lff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor @Getter
public class ClassButton extends Button {

    private ChatColor color;
    private String name;
    private Material material;
    private LFFMenu lffMenu;

    @Override
    public String getName(Player player) {
        return color + name;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(CC.translate("&7&m----------------------------"), CC.translate(" &8* &eYou can &6left click &eto select " + color + name + " Class&e."), CC.translate("&7&m----------------------------"));
    }

    @Override
    public Material getMaterial(Player player) {
        return material;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = ItemBuilder.of(material).name(getName(player));

        if (lffMenu.checkIfClassIsSelected(name)) {
            itemBuilder.enchant(Enchantment.DURABILITY, 10);
        }

        return itemBuilder.setLore(getDescription(player)).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (lffMenu.checkIfClassIsSelected(name)) {
            lffMenu.getClasses().remove(lffMenu.getClassButton(name));
        } else {
            lffMenu.getClasses().add(this);
        }
    }
}
