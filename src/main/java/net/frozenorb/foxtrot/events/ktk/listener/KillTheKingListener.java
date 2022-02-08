package net.frozenorb.foxtrot.events.ktk.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.ktk.KillTheKing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillTheKingListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player death = e.getEntity();
        Player killer = e.getEntity().getKiller();

        if (Foxtrot.getInstance().getKillTheKing() != null) {
            KillTheKing killTheKing = Foxtrot.getInstance().getKillTheKing();

            if (killTheKing.getUuid().toString().equalsIgnoreCase(death.getUniqueId().toString())) {
                if (killer == null) {
                    Foxtrot.getInstance().setKillTheKing(null);
                } else {
                    killTheKing.win(killer);
                }
            }
        }
    }
}
