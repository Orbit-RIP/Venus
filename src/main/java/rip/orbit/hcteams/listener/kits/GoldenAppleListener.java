package rip.orbit.hcteams.listener.kits;

import lombok.Getter;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import rip.orbit.hcteams.HCF;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GoldenAppleListener implements Listener {

    @Getter private static Map<UUID, Long> crappleCooldown = new HashMap<>();

    @EventHandler (ignoreCancelled=false)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {

        Player player = event.getPlayer();

        if (event.getItem() == null || event.getItem().getType() != Material.GOLDEN_APPLE) {
            return;
        }
        
        
        long cooldown = HCF.getInstance().getMapHandler().getScoreboardTitle().contains("Arcane") ? 10900 : HCF.getInstance().getMapHandler().getTeamSize() == 8 ? 30900 : 10900;
        
        if (!HCF.getInstance().getServerHandler().isUhcHealing()) {
            if (event.getItem().getDurability() == 0 && !crappleCooldown.containsKey(player.getUniqueId())) {
                crappleCooldown.put(player.getUniqueId(), System.currentTimeMillis() + (cooldown));
                return;
            }

            if (event.getItem().getDurability() == 0 && crappleCooldown.containsKey(player.getUniqueId())) {
                long millisRemaining = crappleCooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                double value = (millisRemaining / 1000D);
                double sec = value > 0.1 ? Math.round(10.0 * value) / 10.0 : 0.1;

                if (crappleCooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
                    player.sendMessage(ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD + sec + ChatColor.RED + " seconds!");
                    event.setCancelled(true);
                    return;
                } else {
                    crappleCooldown.put(player.getUniqueId(), System.currentTimeMillis() + cooldown);
                    return;
                }
            }
        }

        if (event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 0) return;

        if (HCF.getInstance().getMapHandler().getGoppleCooldown() == -1) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Super golden apples are currently disabled.");
            return;
        }

        long cooldownUntil = HCF.getInstance().getOppleMap().getCooldown(event.getPlayer().getUniqueId());

        if (cooldownUntil > System.currentTimeMillis()) {
            long millisLeft = cooldownUntil - System.currentTimeMillis();
            String msg = TimeUtils.formatIntoDetailedString((int) millisLeft / 1000);

            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
            return;
        }

        HCF.getInstance().getOppleMap().useGoldenApple(
            event.getPlayer().getUniqueId(),
            HCF.getInstance().getMapHandler().isKitMap() || HCF.getInstance().getServerHandler().isVeltKitMap() ?
                TimeUnit.MINUTES.toSeconds(5) :
                (HCF.getInstance().getMapHandler().getGoppleCooldown() * 60) // minutes to seconds
        );
        long millisLeft = HCF.getInstance().getOppleMap().getCooldown(event.getPlayer().getUniqueId()) - System.currentTimeMillis();

        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "███" + ChatColor.BLACK + "██" + ChatColor.DARK_GREEN + "███");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "███" + ChatColor.BLACK + "█" + ChatColor.DARK_GREEN + "████");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "██" + ChatColor.GOLD + "████" + ChatColor.DARK_GREEN + "██" + ChatColor.GOLD + " Super Golden Apple:");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "█" + ChatColor.GOLD + "██" + ChatColor.WHITE + "█" + ChatColor.GOLD + "███" + ChatColor.DARK_GREEN + "█" + ChatColor.DARK_GREEN + "   Consumed");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "█" + ChatColor.GOLD + "█" + ChatColor.WHITE + "█" + ChatColor.GOLD + "████" + ChatColor.DARK_GREEN + "█" + ChatColor.YELLOW + " Cooldown Remaining:");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "█" + ChatColor.GOLD + "██████" + ChatColor.DARK_GREEN + "█" + ChatColor.BLUE + "   " + TimeUtils.formatIntoDetailedString((int) millisLeft / 1000));
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "█" + ChatColor.GOLD + "██████" + ChatColor.DARK_GREEN + "█");
        event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "██" + ChatColor.GOLD + "████" + ChatColor.DARK_GREEN + "██");
    }

}
