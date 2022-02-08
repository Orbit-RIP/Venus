package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.util.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		final String[] lines = event.getLines();
		for (int i = 0; i < lines.length; ++i) {
			final String line = lines[i];
			event.setLine(i, ColorUtil.format(line));
		}
	}
}
