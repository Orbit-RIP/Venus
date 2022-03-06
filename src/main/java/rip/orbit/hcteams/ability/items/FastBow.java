package rip.orbit.hcteams.ability.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.ability.Ability;
import rip.orbit.hcteams.pvpclasses.PvPClass;
import rip.orbit.hcteams.pvpclasses.PvPClassHandler;
import rip.orbit.hcteams.pvpclasses.pvpclasses.ArcherClass;
import rip.orbit.hcteams.util.CC;
import rip.orbit.hcteams.util.cooldown.Cooldowns;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastBow extends Ability {

    private Cooldowns cd = new Cooldowns();

    private int seconds = 0;

    @Override
    public Cooldowns cooldown() {
        return cd;
    }

    @Override
    public String name() {
        return "fastbow";
    }

    @Override
    public String displayName() {
        return "&b&lFast Bow";
    }

    @Override
    public int data() {
        return 12;
    }

    @Override
    public Material mat() {
        return Material.INK_SACK;
    }

    @Override
    public boolean glow() {
        return false;
    }

    @Override
    public List<String> lore() {
        return Arrays.asList(
                "",
                "&7Right click to receive Fast Bow hacks for",
                "&75 seconds",
                "",
                "&c&LNOTE: &cThis item has a 3 minute cooldown, use it carefully",
                ""
        );
    }

    @Override
    public List<String> foundInfo() {
        return Collections.singletonList("KOTH Crate");
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

            PvPClass classs = PvPClassHandler.getPvPClass(player);
            if (classs != null) {
                if (classs.getClass() == ArcherClass.class) {
                    if (classs.getWarmup() != 0) return;
                    player.sendMessage(CC.translate("&cYou cannot use this ability item while wearing Archer Class"));
                    e.setUseItemInHand(Event.Result.DENY);
                    return;
            }
        }

        addCooldown(player, 180);
        e.setCancelled(true);
        takeItem(player);

        player.setMetadata("cheat", new FixedMetadataValue(HCF.getInstance(), "cheat"));

        new BukkitRunnable() {
            @Override
            public void run() {
                player.removeMetadata("cheat", HCF.getInstance());
                player.sendMessage(CC.translate("&c&lWARNING: &cYour Fast Bow Ability has expired."));
            }
        }.runTaskLater(HCF.getInstance(), 20 * 5);

    }
}
}
