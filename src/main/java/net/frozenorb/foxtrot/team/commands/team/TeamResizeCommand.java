package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.VisualClaim;
import net.frozenorb.foxtrot.team.claims.VisualClaimType;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class TeamResizeCommand {

    public static final ItemStack SELECTION_WAND = new ItemStack(Material.WOOD_AXE);

    static {
        ItemMeta meta = SELECTION_WAND.getItemMeta();

        meta.setDisplayName("§3Resizing Wand");
        meta.setLore(Arrays.asList(

                "",
                "§3Right/Left Click Block",
                "§b- §fSelect resize's corners",
                "",
                "§3Right Click Air",
                "§b- §fCancel current claim",
                "",
                "§3Shift §3Left Click §3Block/Air",
                "§b- §fPurchase current claim"

        ));

        SELECTION_WAND.setItemMeta(meta);
    }

    //TODO: Remove permission node to deploy
    @Command(names={ "team resize", "t resize", "f resize", "faction resize", "fac resize" }, permission="op")
    public static void teamResize(final Player sender) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.isOwner(sender.getUniqueId()) || team.isCoLeader(sender.getUniqueId()) || team.isCaptain(sender.getUniqueId())) {
            sender.getInventory().remove(SELECTION_WAND);

            if (team.isRaidable()) {
                sender.sendMessage(ChatColor.RED + "You may not resize land while your faction is raidable!");
                return;
            }

            new BukkitRunnable() {

                public void run() {
                    sender.getInventory().addItem(SELECTION_WAND.clone());
                }

            }.runTaskLater(Foxtrot.getInstance(), 1L);

            new VisualClaim(sender, VisualClaimType.RESIZE, false).draw(false);

            if (!VisualClaim.getCurrentMaps().containsKey(sender.getName())) {
                new VisualClaim(sender, VisualClaimType.MAP, false).draw(true);
            }
        } else {
            sender.sendMessage(ChatColor.GOLD + "Only team captains can do this.");
        }
    }

}