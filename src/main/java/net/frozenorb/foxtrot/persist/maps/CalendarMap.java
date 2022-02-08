package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.persist.PersistMap;

public class CalendarMap extends PersistMap<Integer> {

    public CalendarMap() {
        super("Claimed", "Calendar");
    }

    @Override
    public String getRedisValue(Integer integer) {
        return null;
    }

    @Override
    public Object getMongoValue(Integer integer) {
        return null;
    }

    @Override
    public Integer getJavaObject(String str) {
        return null;
    }
}
