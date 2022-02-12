package rip.orbit.hcteams.map.kits.editor.menu;

import cc.fyre.proton.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.kits.DefaultKit;
import rip.orbit.hcteams.map.kits.Kit;
import rip.orbit.hcteams.map.kits.editor.button.EditKitMenu;
import rip.orbit.hcteams.util.object.Formats;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
final class KitIconButton extends Button {

    private final Optional<Kit> kitOpt;
    private final DefaultKit originalKit;

    @Override
    public String getName(Player player) {
        if (kitOpt.isPresent()) {
            if (!kitOpt.get().getName().equals(originalKit.getName())) {
                return ChatColor.GREEN.toString() + ChatColor.BOLD + "Edit `" + kitOpt.get().getName() + "` Kit";
            } else {
                return ChatColor.GREEN.toString() + ChatColor.BOLD + "Edit " + originalKit.getName() + " Kit";
            }
        } else {
            return ChatColor.GREEN.toString() + ChatColor.BOLD + "Create " + originalKit.getName() + " Kit";
        }
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();
        description.add("");
        description.addAll(Formats.renderLines(ChatColor.GRAY.toString(), originalKit.getDescription()));
        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return originalKit.getIcon().getType();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        Kit resolvedKit;
        if (kitOpt.isPresent()) {
            resolvedKit = kitOpt.get();
        } else {
            resolvedKit = new Kit(originalKit);
            HCF.getInstance().getMapHandler().getKitManager().trackUserKit(player.getUniqueId(), resolvedKit);
        }

        new EditKitMenu(resolvedKit).openMenu(player);
    }

}