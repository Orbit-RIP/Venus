package net.frozenorb.foxtrot.misc.giveaway;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import rip.orbit.nebula.Nebula;

public class GiveawayListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (Foxtrot.getInstance().getGiveawayHandler().isActive()) {
            if (Foxtrot.getInstance().getGiveawayHandler().getWord() == null) {
                try {
                    int i = Integer.parseInt(event.getMessage());

                    if (i == Foxtrot.getInstance().getGiveawayHandler().getNumber()) {
                        event.setCancelled(true);
                        Bukkit.broadcastMessage(CC.translate("&7"));
                        Bukkit.broadcastMessage(CC.translate(" &6» &fThe game was won by "
                                + Nebula.getInstance().getProfileHandler().fromUuid(event.getPlayer().getUniqueId()) + "&f."));
                        Bukkit.broadcastMessage(CC.translate(" &6» &fThe number was &6" + Foxtrot.getInstance().getGiveawayHandler().getNumber() + "&f."));
                        Bukkit.broadcastMessage(CC.translate("&7"));

//                        if (!qUtilities.getInstance().getServerManager().isChatMuted())
//                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mutechat");

                        Foxtrot.getInstance().getGiveawayHandler().setActive(false);
                        Foxtrot.getInstance().getGiveawayHandler().setNumber(0);
                    }
                } catch (NumberFormatException e) {}
            } else {
                if (event.getMessage().equalsIgnoreCase(Foxtrot.getInstance().getGiveawayHandler().getWord()) && !Foxtrot.getInstance().getGiveawayHandler().getEntered().contains(event.getPlayer().getUniqueId())) {
                    Bukkit.broadcastMessage(CC.translate(" &6» " + Nebula.getInstance().getProfileHandler().fromUuid(event.getPlayer().getUniqueId()).getFancyName() + " &fhas entered the raffle by typing &6" + Foxtrot.getInstance().getGiveawayHandler().getWord() + "."));
                    Foxtrot.getInstance().getGiveawayHandler().getEntered().add(event.getPlayer().getUniqueId());
                    event.setCancelled(true);
                }
            }
        }
    }
}
