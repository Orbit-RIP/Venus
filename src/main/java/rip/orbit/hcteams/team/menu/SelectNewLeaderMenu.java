package rip.orbit.hcteams.team.menu;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.menu.button.MakeLeaderButton;

import java.util.*;

@RequiredArgsConstructor
public class SelectNewLeaderMenu extends Menu {

    @NonNull @Getter Team team;


    @Override
	public String getTitle(Player player) {
        return "Leader for " + team.getName();
    }


    @Override
	public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        ArrayList<UUID> uuids = new ArrayList<>();
        uuids.addAll(team.getMembers());

        Collections.sort(uuids, (u1, u2) -> UUIDUtils.name(u1).toLowerCase().compareTo(UUIDUtils.name(u2).toLowerCase()));

        for (UUID u : uuids) {
            buttons.put(index, new MakeLeaderButton(u, team));
            index++;
        }

        return buttons;
    }


    @Override
	public boolean isAutoUpdate() {
        return true;
    }

}
