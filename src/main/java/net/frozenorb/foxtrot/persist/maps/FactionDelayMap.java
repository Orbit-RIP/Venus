package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.persist.PersistMap;

import java.util.UUID;

public class FactionDelayMap extends PersistMap<Long> {

    public FactionDelayMap() {
        super("FactionDelayMap", "FactionDelay");
    }

    @Override
    public String getRedisValue(Long aLong) {
        return (String.valueOf(aLong));
    }

    @Override
    public Object getMongoValue(Long aLong) {
        return aLong.intValue();
    }

    @Override
    public Long getJavaObject(String str) {
        return Long.parseLong(str);
    }

    public boolean has(UUID uuid) {
        return contains(uuid);
    }

    public Long getDelay(UUID uuid) {
        return (has(uuid) ? getValue(uuid) : 0L);
    }

    public void addCooldown(UUID uuid) {
        updateValueAsync(uuid, System.currentTimeMillis());
    }
}
