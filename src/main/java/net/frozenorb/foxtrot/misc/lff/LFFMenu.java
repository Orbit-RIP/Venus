package net.frozenorb.foxtrot.misc.lff;

import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.scoreboard.ScoreFunction;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.nebula.Nebula;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class LFFMenu extends Menu {

    private List<ClassButton> classes = new ArrayList<>();

    @Override
    public String getTitle(Player player) {
        return CC.translate("&eSelect&7: &fClasses");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(buttons.size(), new ClassButton(ChatColor.YELLOW, "Diamond", Material.DIAMOND_BOOTS, this));
        buttons.put(buttons.size(), new ClassButton(ChatColor.GOLD, "Bard", Material.GOLD_BOOTS, this));
        buttons.put(buttons.size(), new ClassButton(ChatColor.GREEN, "Archer", Material.LEATHER_BOOTS, this));
        buttons.put(buttons.size(), new ClassButton(ChatColor.GRAY, "Rogue", Material.CHAINMAIL_BOOTS, this));
        buttons.put(buttons.size(), new ClassButton(ChatColor.BLUE, "Base Bitch", Material.GRASS, this));

        buttons.put(8, new Button() {
            @Override
            public String getName(Player player) {
                return ChatColor.GREEN + "Confirm Selection";
            }

            @Override
            public List<String> getDescription(Player player) {
                return Arrays.asList("&7&m----------------------------", " &6* &fYou can &eleft click &fto confirm the your selected classes.","&7&m----------------------------");
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.EMERALD;
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (classes.isEmpty()) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "You didn't select any messages.");
                    return;
                }

                if (Foxtrot.getInstance().getLffMap().hasCooldown(player.getUniqueId()) && System.currentTimeMillis() < Foxtrot.getInstance().getLffMap().getCooldown(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You still have a cooldown of " + ScoreFunction.TIME_FANCY.apply((float)(Foxtrot.getInstance().getLffMap().getCooldown(player.getUniqueId()) - System.currentTimeMillis()) / 1000));
                    return;
                }

                player.closeInventory();

                Bukkit.broadcastMessage(CC.translate("&7&m------------------------------------"));
                Bukkit.broadcastMessage(CC.translate(" &6» "
                        + Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId()).getFancyName()
                        + " &fis looking for a faction."));
                Bukkit.broadcastMessage(CC.translate("   &6* &fPreferred Classes&7: " + StringUtils.join(classes.stream().map(claz -> claz.getColor() + claz.getName()).collect(Collectors.toList()), "&7, ")));
                Bukkit.broadcastMessage(CC.translate("&7&m------------------------------------"));


                Foxtrot.getInstance().getLffMap().setCurrentTime(player.getUniqueId());
            }
        });

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    public boolean checkIfClassIsSelected(String name) {
        return getClassButton(name) != null;
    }

    public ClassButton getClassButton(String name) {
        return classes.stream().filter(ajoisdfias -> ajoisdfias.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }


}
