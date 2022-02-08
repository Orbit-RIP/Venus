package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import cc.fyre.proton.util.PlayerUtils;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class SpawnTagListener implements Listener {

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = PlayerUtils.getDamageSource(event.getDamager());

        /* Only tag player damagers, and deny tagging self */
        if (damager != null && damager != event.getEntity()) {
            SpawnTagHandler.addOffensiveSeconds(damager, SpawnTagHandler.getMaxTagTime());
            SpawnTagHandler.addPassiveSeconds((Player) event.getEntity(), SpawnTagHandler.getMaxTagTime());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE || !SpawnTagHandler.isTagged(player) || Foxtrot.getInstance().getServerHandler().isPlaceBlocksInCombat()
                || Foxtrot.getInstance().getTeamHandler().getTeam(player) == null || Foxtrot.getInstance().getServerHandler().isUHCF()) {
            return;
        }

        if (LandBoard.getInstance().getTeam(event.getBlock().getLocation()) == Foxtrot.getInstance().getTeamHandler().getTeam(player)) {
            player.sendMessage(CC.translate("&cYou may not place blocks while your &lSpawn Tag &ctimer is active!"));
            event.setCancelled(true);
        }
    }

  /*  @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        String msg = e.getMessage().toLowerCase();
        if (Foxtrot.getInstance().getServerHandler().isUHCF()) {
            return;
        }
        if (msg.startsWith("/kit") || msg.startsWith("/kits")) {
            Player player = e.getPlayer();
            if (SpawnTagHandler.isTagged(player) && Foxtrot.getInstance().getServerHandler().isUseKitsInCombat()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not use kits while you are &c&lSpawn Tagged&c."));
                e.setCancelled(true);
                return;
            }
        }
        if (msg.startsWith("/gkit") || msg.startsWith("/gkits") || msg.startsWith("/gkitz")) {
            Player player = e.getPlayer();
            if (SpawnTagHandler.isTagged(player)) {
                player.sendMessage(CC.translate("&cYou may not use kits while you are &c&lSpawn Tagged&c."));
                e.setCancelled(true);
                return;
            }
        }

   */

}