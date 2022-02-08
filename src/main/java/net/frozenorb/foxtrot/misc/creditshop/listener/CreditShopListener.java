package net.frozenorb.foxtrot.misc.creditshop.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.events.EventCapturedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CreditShopListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (killer == null)
            return;

        int credits = Foxtrot.getInstance().getCreditsMap().getCredits(killer.getUniqueId());

//        if (!CustomTimerCreateCommand.isDoubleTokens()) {
        Foxtrot.getInstance().getCreditsMap().setCredits(killer.getUniqueId(), credits + 1);
    }

    @EventHandler
    public void onEventCaptured(EventCapturedEvent e) {
        Player player = e.getPlayer();

        if (player == null)
            return;

        int credits = Foxtrot.getInstance().getCreditsMap().getCredits(player.getUniqueId());
        Foxtrot.getInstance().getCreditsMap().setCredits(player.getUniqueId(), credits + 10);
    }

}
