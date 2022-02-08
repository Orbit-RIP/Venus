package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TeamCheckInvCommand {

    @Command(names={ "team checkinv", "t checkinv", "f checkinv", "faction checkinv", "fac checkinv" }, permission="foxtrot.factiontpall")
    public static void teamCheckInv(Player sender, @Parameter(name="team") Team team, @Parameter(name="item") String material) {

        if (Material.getMaterial(material) == null) {
            sender.sendMessage(CC.RED + "That material is invalid!");
            return;
        }

        sender.sendMessage(CC.GRAY + CC.HORIZONTAL_SEPARATOR);
        sender.sendMessage(CC.translate("&6&lTeam Inventory Lookup &ffor " + team.getName(sender)));
        sender.sendMessage(CC.translate("  &e» &fSearching for " + material));
        sender.sendMessage(CC.GRAY + CC.HORIZONTAL_SEPARATOR);

        for (Player player : team.getOnlineMembers()) {
            if (player.getInventory().contains(Material.getMaterial(material))) {
                sender.sendMessage(CC.translate(player.getDisplayName() + " &fhas that item in their inventory!"));
            } else {
                sender.sendMessage(CC.RED + "No one else in that faction has the item");
            }
        }

        sender.sendMessage(CC.GRAY + CC.HORIZONTAL_SEPARATOR);
        return;
    }

}
