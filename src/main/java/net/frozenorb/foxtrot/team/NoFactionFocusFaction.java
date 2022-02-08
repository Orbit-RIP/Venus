package net.frozenorb.foxtrot.team;

import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NoFactionFocusFaction {

    @Getter private Map<UUID, Team> playerNoFactionTeamMap = new ConcurrentHashMap<>(); // Player UUID -> Team

    public void setPlayerNoFactionTeamMap(UUID uniqueId, Team targetTeam) {
        playerNoFactionTeamMap.put(uniqueId, targetTeam);
    }
}
