package net.frozenorb.foxtrot.misc.creditshop.menu.buttons;

import cc.fyre.proton.Proton;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.persist.maps.PlaytimeMap;
import net.frozenorb.foxtrot.util.ItemBuilder;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.rank.Rank;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProfileButton extends Button {


    @Override
    public void clicked(Player player, int slot, ClickType clickType) { }

    @Override
    public String getName(Player player) {
        return "&6" + player.getName() + "'s Profile";
    }

    @Override
    public List<String> getDescription(Player player) {
        Rank rank = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId()).getActiveRank();
        PlaytimeMap playtime = Foxtrot.getInstance().getPlaytimeMap();
        int playtimeTime = (int) playtime.getPlaytime(player.getUniqueId());
            playtimeTime += playtime.getCurrentSession(player.getUniqueId()) / 1000;


        return Arrays.asList(
                "",
                "&eRank&7: &f" + rank.getColor() + rank.getFancyName(),
                "&ePlaytime&7: &f" + TimeUtils.formatIntoDetailedString(playtimeTime),
                "&eBalance&7: &2&L$&a" + NumberFormat.getNumberInstance(Locale.US).format(Proton.getInstance().getEconomyHandler().getBalance(player.getUniqueId())),
                "");
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.SKULL_ITEM;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 3;
    }
}
