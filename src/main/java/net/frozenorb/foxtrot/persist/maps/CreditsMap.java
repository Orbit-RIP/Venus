package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.persist.PersistMap;

import java.util.UUID;

public class CreditsMap extends PersistMap<Integer> {

    public CreditsMap() {
        super("Credits", "Credits");
    }

    @Override
    public String getRedisValue(Integer tokens) {
        return (String.valueOf(tokens));
    }

    @Override
    public Integer getJavaObject(String str) {
        return (Integer.parseInt(str));
    }

    @Override
    public Object getMongoValue(Integer tokens) {
        return (tokens);
    }

    public int getCredits(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setCredits(UUID update, int credits) {

        updateValueAsync(update, credits);
    }

}