package net.frozenorb.foxtrot.team.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.mission.MissionType;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xanderume@gmail (JavaProject)
 */
@AllArgsConstructor
public class MissionMenu extends Menu {

    @Getter private Team team;

    @Override
    public String getTitle(Player player) {
        return "Missions";
    }

    @Override
    public int size(Player player) {
        return 9;
    }

    @Override
    public Map<Integer,Button> getButtons(Player player) {

        final Map<Integer,Button> toReturn = new HashMap<>();

        for (MissionType missionType : MissionType.values()) {

            toReturn.put(toReturn.size(),new Button() {

                @Override
                public String getName(Player player) {
                    return (team.getCompletedMissions().contains(missionType) ? ChatColor.GREEN:ChatColor.RED) + missionType.getTitle();
                }

                @Override
                public List<String> getDescription(Player player) {

                    final List<String> toReturn = new ArrayList<>();

                    toReturn.add("");
                    toReturn.add(ChatColor.GRAY + missionType.getDescription());
                    toReturn.add("");
                    toReturn.add(ChatColor.YELLOW + "[" + missionType.getProgressBar(team) + ChatColor.YELLOW + "] " + (missionType.getRequiredVariable(team) > missionType.getRequiredAmount() ? missionType.getRequiredAmount():missionType.getRequiredVariable(team)) + "/" + missionType.getRequiredAmount());
                    toReturn.add("");
                    toReturn.add(ChatColor.GRAY + "Completing this mission will get you " + ChatColor.WHITE + missionType.getRequiredAmount() + ChatColor.GRAY + " tokens!");
                    toReturn.add("");

                    return toReturn;
                }

                @Override
                public Material getMaterial(Player player) {
                    return missionType.getMaterial();
                }

            });

        }

        return toReturn;
    }

}
