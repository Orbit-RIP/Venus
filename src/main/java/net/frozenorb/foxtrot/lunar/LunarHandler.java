package net.frozenorb.foxtrot.lunar;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.LCCooldown;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class LunarHandler {

    @Getter
    public final boolean supported;

    public LunarHandler(JavaPlugin plugin) {
        supported = Bukkit.getPluginManager().getPlugin("LunarClientAPI") != null;

        if (supported) {
            Bukkit.getPluginManager().registerEvents(new LunarListener(), plugin);
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new LunarWaypointTask(), 40, 40);
        }
    }

    public void sendCooldown(Player player, String name, int seconds, Material material) {
        if (!supported) return;
        LunarClientAPI.getInstance().sendCooldown(player, new LCCooldown(
                name, seconds, TimeUnit.SECONDS, material
        ));
    }

}
