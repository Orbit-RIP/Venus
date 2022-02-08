package net.frozenorb.foxtrot.abilities;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.managers.AbilityManager;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by PVPTUTORIAL | Created on 01/04/2020
 */

public abstract class Ability implements Listener {
    public Foxtrot plugin;

    public Ability(Foxtrot plugin) {
        this.plugin = plugin;

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public abstract String getIdentifier();
    public abstract String getDisplayName();
    public abstract String getDescription();
    public abstract String getScoreboardPrefix();
    public abstract ItemStack getItem(int amount);
    public abstract int getUses();
    public abstract HashMap<UUID, Long> getCooldown();

    public boolean isSimilar(ItemStack itemStack) {
        return itemStack.getType() == this.getItem(1).getType()
                && itemStack.hasItemMeta()
                && itemStack.getItemMeta().hasDisplayName()
                && itemStack.getItemMeta().getDisplayName().startsWith(this.getItem(1).getItemMeta().getDisplayName())
                && itemStack.getItemMeta().hasLore()
                && itemStack.getItemMeta().getLore().equals(this.getItem(1).getItemMeta().getLore().stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList()));
    }

    public boolean canBeUsed(Player damaged) {
        Team team = LandBoard.getInstance().getTeam(damaged.getLocation());

        return team != null && !team.hasDTRBitmask(DTRBitmask.KOTH);
    }

    public AbilityManager getAbilityManager() {
        return plugin.getAbilityManager();
    }

    public String getPlayerName(Player player) {
        Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId());
        return player.hasPotionEffect(PotionEffectType.INVISIBILITY)
                ? "&7&oUnknown" : profile.getFancyName();
    }
}
