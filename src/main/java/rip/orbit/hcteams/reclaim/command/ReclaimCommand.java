package rip.orbit.hcteams.reclaim.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.util.CC;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

import java.util.UUID;

public class ReclaimCommand {

    @Command(names = {"reclaim"}, permission = "")
    public static void execute(Player player) {

        if (HCF.getInstance().getReclaimHandler().getReclaimMap().isToggled(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have already reclaimed this map.");
            return;
        }

        Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId());

        if (HCF.getInstance().getReclaimConfig().getConfiguration().getStringList("groups." + profile.getActiveRank().getName()) == null || !HCF.getInstance().getReclaimConfig().getConfiguration().contains("groups." + profile.getActiveRank().getName())) {
            player.sendMessage(ChatColor.RED + "It appears there is no reclaim found for your rank.");
            return;
        }

        for (String command : HCF.getInstance().getReclaimConfig().getConfiguration().getStringList("groups." + profile.getActiveRank().getName() + ".commands")) {
            HCF.getInstance().getServer().dispatchCommand(HCF.getInstance().getServer().getConsoleSender(), command.replace("%player%",player.getName()));
        }

        HCF.getInstance().getReclaimHandler().getReclaimMap().setToggled(player.getUniqueId(),true);
    }

    @Command(names = "resetreclaim", permission = "op")
    public static void resetredeem(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name = "player") UUID target) {
        sender.sendMessage(CC.translate("&aReset " + UUIDUtils.name(target) + "'s Reclaim"));
        HCF.getInstance().getReclaimHandler().getReclaimMap().setToggled(target, false);
    }

}
