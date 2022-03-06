package rip.orbit.hcteams.ability.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.orbit.hcteams.ability.Ability;
import rip.orbit.hcteams.util.CC;
import rip.orbit.hcteams.util.cooldown.Cooldowns;

import java.util.Arrays;
import java.util.List;

public class PowerStone extends Ability {

    public Cooldowns cd = new Cooldowns();
    private Cooldowns powerStone = new Cooldowns();

    @Override
    public Cooldowns cooldown() {
        return cd;
    }

    @Override
    public String name() {
        return "powerstone";
    }

    @Override
    public String displayName() {
        return CC.chat("&a&lPower Stone");
    }

    @Override
    public int data() {
        return 0;
    }

    @Override
    public Material mat() {
        return Material.EMERALD;
    }

    @Override
    public boolean glow() {
        return false;
    }

    @Override
    public List<String> lore() {
        return Arrays.asList(
                "",
                "&7Right Click to receive Strength II, Resistance 3",
                "&7and Regeneration 5 for 10 seconds. Within those",
                "&710 seconds you may not use any sort of potions.",
                ""
        );
    }

    @Override
    public List<String> foundInfo() {
        return CC.translate(Arrays.asList(
                "Ability Packages",
                "Partner Crates",
                "Star Shop (/starshop)"
        ));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (isSimilar(e.getItem())) {
            if (!isClick(e, "RIGHT")) {
                e.setUseItemInHand(Event.Result.DENY);
                return;
            }

            if (!canUse(player)) {
                e.setUseItemInHand(Event.Result.DENY);
                return;
            }

            addCooldown(player, 120);
            e.setCancelled(true);
            takeItem(player);

            powerStone.applyCooldown(player, 20);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 2));

            List<String> hitMsg = Arrays.asList(
                    "",
                    "&aYou have used &lPower Stone&a ability!",
                    "");

            hitMsg.forEach(s -> player.sendMessage(CC.chat(s)));
        }
    }

    @EventHandler
    public void PotionSplashEvent(PotionSplashEvent event) {
        Player p = (Player) event.getPotion().getShooter();

        if (powerStone.onCooldown(p)) {
            for (PotionEffect e : event.getPotion().getEffects()) {
                if (event.getPotion().getShooter() instanceof Player) {
                    p.sendMessage(CC.RED + "Potions are disabled undergoing Power Stone effects!");
                    event.setCancelled(true);
                }
            }
        }
    }
}
