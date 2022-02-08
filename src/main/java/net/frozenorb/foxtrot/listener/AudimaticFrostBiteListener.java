package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class AudimaticFrostBiteListener implements Listener {
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if (Foxtrot.getInstance().audiice.contains(player)) {
			if (player.isOnGround()) {
				Location to = event.getFrom();
				to.setPitch(event.getTo().getPitch());
				to.setYaw(event.getTo().getYaw());
				event.setTo(to);

			}
			if (!player.isOnGround()) {
				event.getPlayer().setVelocity(new Vector().zero());
				Location to = event.getFrom();
				to.setPitch(event.getTo().getPitch());
				to.setYaw(event.getTo().getYaw());
				event.setTo(to);

			}

		}
	}
}
