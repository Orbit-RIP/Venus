package net.frozenorb.foxtrot.server.idle;

import net.frozenorb.foxtrot.Foxtrot;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class IdleCheckRunnable extends BukkitRunnable {

    private static final int MINUTES = 10;

    @Override
    public void run() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!Foxtrot.getInstance().getPvPTimerMap().hasTimer(online.getUniqueId()) || !online.hasPermission("neutron.staff") || online.isOp() || online.hasMetadata("frozen")) {
                continue;
            }

            EntityPlayer player = ((CraftPlayer) online).getHandle();

            if (player.x() > 0L) {
                long lastMoved = player.x();

                if (System.currentTimeMillis() - lastMoved >= ((MINUTES * 60) * 1000L)) {
                    online.kickPlayer(ChatColor.RED + "You have been kicked from the server for being idle for more than 10 minutes.");
                }
            }
        }
    }

}