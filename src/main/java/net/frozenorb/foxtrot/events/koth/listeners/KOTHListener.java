package net.frozenorb.foxtrot.events.koth.listeners;

import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.events.EventActivatedEvent;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.EventType;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.foxtrot.events.koth.events.EventControlTickEvent;

public class KOTHListener implements Listener {

    @EventHandler
    public void onKOTHControlTick(EventControlTickEvent event) {
        
        if (event.getKOTH().getType() != EventType.KOTH) {
            return;
        }

        KOTH koth = event.getKOTH();
        if (koth.getRemainingCapTime() % 180 == 0 && koth.getRemainingCapTime() <= (koth.getCapTime() - 30)) {
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lKOTH &8» &fSomebody is attempting " +
                    "to control &d" + koth.getName() + "&f. &e(" + TimeUtils.formatIntoMMSS(koth.getRemainingCapTime()) + ")"));
        }
    }
}