package net.frozenorb.foxtrot.team.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author xanderume@gmail (JavaProject)
 */
@AllArgsConstructor
public class TeamPointsChangeEvent extends Event {

    @Getter private static HandlerList handlerList = new HandlerList();

    @Getter private final Team team;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public void call() {
        Foxtrot.getInstance().getServer().getPluginManager().callEvent(this);
    }
}
