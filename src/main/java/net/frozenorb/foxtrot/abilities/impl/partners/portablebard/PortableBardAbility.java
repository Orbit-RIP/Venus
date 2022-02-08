package net.frozenorb.foxtrot.abilities.impl.partners.portablebard;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.pvpclasses.PvPClass;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.BardClass;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import cc.fyre.proton.util.ItemBuilder;
import rip.orbit.nebula.Nebula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Emilxyz (langgezockt@gmail.com)
 * 13.09.2020 / 00:30
 * HCF / net.frozenorb.foxtrot.abilities.impl.partners.portablebard
 */

public class PortableBardAbility extends Ability {

    public static final HashMap<UUID, Long> COOLDOWN = new HashMap<>();

    private final PotionEffect effect;
    private final Material material;
    private final String identifier;

    public PortableBardAbility(Foxtrot plugin, PotionEffect effect, Material material, String identifier) {
        super(plugin);
        this.effect = effect;
        this.material = material;
        this.identifier = identifier;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().name().startsWith("RIGHT_CLICK_")) {
            return;
        }

        Player player = event.getPlayer();

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            return;
        }

        if (!this.isSimilar(player.getItemInHand())) {
            return;
        }

        Team playerSpawn = LandBoard.getInstance().getTeam(player.getLocation());

        if (playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.SAFE_ZONE) || playerSpawn != null && playerSpawn.hasDTRBitmask(DTRBitmask.NO_ABILITY)) {
            event.setCancelled(true);
            return;
        }

        if (PortableBardAbility.COOLDOWN.containsKey(player.getUniqueId()) && PortableBardAbility.COOLDOWN.get(player.getUniqueId()) != null) {
            long remaining = PortableBardAbility.COOLDOWN.get(player.getUniqueId()) - System.currentTimeMillis();

            if (remaining > 0L) {
                player.sendMessage(ChatColor.RED + "You are still on cooldown for " + CC.translate(this.getDisplayName())
                        + ChatColor.RED + " for another " + DurationFormatUtils.formatDurationWords(PortableBardAbility.COOLDOWN.get(player.getUniqueId())
                        - System.currentTimeMillis(), true, true) + ".");
                event.setCancelled(true);
                player.updateInventory();
                return;
            }
        }

        Team team = plugin.getTeamHandler().getTeam(player);

        List<Player> players = new ArrayList<>();
        players.add(player);

        if (team != null) {
            players.addAll(player.getNearbyEntities(BardClass.BARD_RANGE, (float) BardClass.BARD_RANGE / 2, BardClass.BARD_RANGE).stream()
                    .filter(entity -> entity instanceof Player && team.isMember(entity.getUniqueId()))
                    .map(entity -> (Player) entity)
                    .collect(Collectors.toList()));
        }

        players.forEach(current -> {
            PvPClass.smartAddPotion(current, effect);
            current.sendMessage(String.format(
                    CC.translate("&6» &eYou have recieved the &6%s Effect &efrom %s&e's &6&lPortable Bard&e."),
                    plugin.getAbilityManager().getPotionName(effect.getType()) + " " + (effect.getAmplifier() + 1),
                    Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId()).getFancyName()
            ));
        });

        decrementUses(player, player.getItemInHand());
        PortableBardAbility.COOLDOWN.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
    }

    @Override
    public String getDisplayName() {
        return String.format(
                "&6&lPortable Bard &7(%s)",
                plugin.getAbilityManager().getPotionName(effect.getType())
        );
    }

    @Override
    public String getDescription() {
        return String.format(
                "Give your faction %s effect in a 20 block radius.",
                plugin.getAbilityManager().getPotionName(effect.getType()) + " " + (effect.getAmplifier() + 1)
        );
    }

    @Override
    public String getScoreboardPrefix() {
        return "&6&lPortable Bard";
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public ItemStack getItem(int amount) {
        return ItemBuilder.of(material)
                .name(getDisplayName())
                .amount(amount)
                .enchant(Enchantment.DURABILITY, 10)
                .addToLore(("&7&7&m--*-----------------------*--"))
                .addToLore("&7" + getDescription())
                .addToLore("&fUses: &71")
                .addToLore(("&7&7&m--*-----------------------*--"))
                .build();
    }

    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public HashMap<UUID, Long> getCooldown() {
        return COOLDOWN;
    }

    public void decrementUses(Player player, ItemStack itemStack) {
        if (itemStack.getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        } else {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
    }

}
