package net.frozenorb.foxtrot.map.stats.hologram;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class HologramCache {

    private Map<String, Long> timestampHologramCache = new HashMap<>();

    public void addHologram(String name, Hologram hologram) {
        timestampHologramCache.put(name, hologram.getCreationTimestamp());
    }

    public void removeHologram(String name) {
        timestampHologramCache.remove(name);
    }

    public Hologram getHologram(String name) {
        if (!timestampHologramCache.containsKey(name)) return null;

        long l = timestampHologramCache.get(name);

        for (Hologram hologram : HologramsAPI.getHolograms(Foxtrot.getInstance())) {
            if (hologram.getCreationTimestamp() == l) {
                return hologram;
            }
        }

        return null;
    }

}
