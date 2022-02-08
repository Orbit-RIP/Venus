package net.frozenorb.foxtrot.events.region.oremtn;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.region.oremtn.listeners.OreListener;
import net.frozenorb.foxtrot.team.claims.Claim;
import net.frozenorb.foxtrot.util.ColorUtil;
import cc.fyre.proton.Proton;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;

public class OreHandler {

    private static File file;
    @Getter
    public final static String oreTeamName = "Ore";
    @Getter
    @Setter
    public static OreMountain oreMountain;

    public OreHandler() {
        try {
            file = new File(Foxtrot.getInstance().getDataFolder(), "oremtn.json");

            if (!file.exists()) {
                oreMountain = null;

                if (file.createNewFile()) {
                    Foxtrot.getInstance().getLogger().warning("Created a new ore mountain json file.");
                }
            } else {
                oreMountain = Proton.GSON.fromJson(FileUtils.readFileToString(file), OreMountain.class);
                Foxtrot.getInstance().getLogger().info("Successfully loaded the ore mountain from file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int secs = Foxtrot.getInstance().getServerHandler().isHardcore() ? (90 * 60 * 20) : Foxtrot.getInstance().getServerHandler().getTabServerName().contains("cane") ? Foxtrot.getInstance().getMapHandler().getTeamSize() == 8 ? 20 * 25 * 60 : 20 * 45 * 60 : 12000;
        Foxtrot.getInstance().getServer().getScheduler().runTaskTimer(Foxtrot.getInstance(), () -> {
            oreMountain.reset();

            // Broadcast the reset
            Bukkit.broadcastMessage(ColorUtil.format("&9&lOre Mountain &fhas just been reset."));
        }, secs, secs);

        Foxtrot.getInstance().getServer().getPluginManager().registerEvents(new OreListener(), Foxtrot.getInstance());
    }


    public void save() {
        try {
            FileUtils.write(file, Proton.GSON.toJson(oreMountain));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasOreMountain() {
        return oreMountain != null;
    }

    public static Claim getClaim() {
        try {
            return Foxtrot.getInstance().getTeamHandler().getTeam(oreTeamName).getClaims().get(0);
        } catch (NullPointerException e) {
            return null;
        }
    }

}
