package rip.orbit.hcteams.team.menu.button;

import lombok.AllArgsConstructor;
import cc.fyre.proton.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.orbit.hcteams.commands.staff.TeamManageCommand;
import rip.orbit.hcteams.team.Team;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class RenameButton extends Button {

    private Team team;


    @Override
	public void clicked(Player player, int i, ClickType clickType) {
        player.closeInventory();
        TeamManageCommand.renameTeam(player, team);
    }


    @Override
	public String getName(Player player) {
        return "§eRename Team";
    }


    @Override
	public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }


    @Override
	public byte getDamageValue(Player player) {
        return 0;
    }


    @Override
	public Material getMaterial(Player player) {
        return Material.SIGN;
    }
}
