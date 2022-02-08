package net.frozenorb.foxtrot.task;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearItemTask extends BukkitRunnable {

	@Override
	public void run(){
		for (World world : Bukkit.getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (!(entity instanceof Player || entity instanceof Minecart || entity instanceof Wither || entity instanceof ItemFrame || entity instanceof EnderDragon || entity instanceof EnderPearl)) {
					entity.remove();
				}
			}
		}

	}
}
