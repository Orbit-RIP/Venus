package net.frozenorb.foxtrot.events.region.glowmtn;

import java.io.File;
import java.io.IOException;

import net.frozenorb.foxtrot.events.region.glowmtn.listeners.GlowListener;
import net.frozenorb.foxtrot.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.claims.Claim;
import cc.fyre.proton.Proton;
import net.minecraft.util.org.apache.commons.io.FileUtils;

public class GlowHandler {

    private static File file;
    @Getter
    private final static String glowTeamName = "Glowstone";
    @Getter
    @Setter
    private GlowMountain glowMountain;

    public GlowHandler() {
        try {
            file = new File(Foxtrot.getInstance().getDataFolder(), "glowmtn.json");

            if (!file.exists()) {
                glowMountain = null;

                if (file.createNewFile()) {
                    Foxtrot.getInstance().getLogger().warning("Created a new glow mountain json file.");
                }
            } else {
                glowMountain = Proton.GSON.fromJson(FileUtils.readFileToString(file), GlowMountain.class);
                Foxtrot.getInstance().getLogger().info("Successfully loaded the glow mountain from file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int secs = Foxtrot.getInstance().getServerHandler().isHardcore() ? (90 * 60 * 20) : Foxtrot.getInstance().getServerHandler().getTabServerName().contains("cane") ? Foxtrot.getInstance().getMapHandler().getTeamSize() == 8 ? 20 * 25 * 60 : 20 * 45 * 60 : 12000;
        Foxtrot.getInstance().getServer().getScheduler().runTaskTimer(Foxtrot.getInstance(), () -> {
            getGlowMountain().reset();

            // Broadcast the reset
            Bukkit.broadcastMessage(ColorUtil.format("&6&lGlowstone Mountain §fhas just been reset."));
        }, secs, secs);

        Foxtrot.getInstance().getServer().getPluginManager().registerEvents(new GlowListener(), Foxtrot.getInstance());
    }

    public void save() {
        try {
            FileUtils.write(file, Proton.GSON.toJson(glowMountain));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasGlowMountain() {
        return glowMountain != null;
    }

    public static Claim getClaim() {
        try {
            return Foxtrot.getInstance().getTeamHandler().getTeam(glowTeamName).getClaims().get(0); // null if no glowmtn is set!
        } catch (NullPointerException e) {
            return null;
        }
    }
}