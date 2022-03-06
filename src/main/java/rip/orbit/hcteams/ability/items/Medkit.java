package rip.orbit.hcteams.ability.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.orbit.hcteams.ability.Ability;
import rip.orbit.hcteams.util.CC;
import rip.orbit.hcteams.util.cooldown.Cooldowns;

import java.util.Arrays;
import java.util.List;

public class Medkit extends Ability {

    public Cooldowns cd = new Cooldowns();

    @Override
    public Cooldowns cooldown() {
        return cd;
    }

    @Override
    public String name() {
        return "medkit";
    }

    @Override
    public String displayName() {
        return CC.chat("&c&lMed-Kit");
    }

    @Override
    public int data() {
        return 0;
    }

    @Override
    public Material mat() {
        return Material.PAPER;
    }

    @Override
    public boolean glow() {
        return false;
    }

    @Override
    public List<String> lore() {
        return Arrays.asList(
                "",
                "&7Right click to heal yourself back to full health",
                "&7with some added medication",
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

            addCooldown(player, 60);
            e.setCancelled(true);
            takeItem(player);

            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 30 * 20, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20, 2));

            List<String> hitMsg = Arrays.asList(
                    "",
                    "&cYou have used &lMed Kit&c ability!",
                    "");

            hitMsg.forEach(s -> player.sendMessage(CC.chat(s)));
        }
    }

}
