package rip.orbit.hcteams.ability.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.ability.Ability;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.team.claims.LandBoard;
import rip.orbit.hcteams.team.dtr.DTRBitmask;
import rip.orbit.hcteams.util.CC;
import rip.orbit.hcteams.util.cooldown.Cooldowns;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastTrack extends Ability {

    public Cooldowns cd = new Cooldowns();

    @Override
    public Cooldowns cooldown() {
        return cd;
    }

    @Override
    public String name() {
        return "fasttrack";
    }

    @Override
    public String displayName() {
        return "&9&lFast Track";
    }

    @Override
    public int data() {
        return 0;
    }

    @Override
    public Material mat() {
        return Material.WATCH;
    }

    @Override
    public boolean glow() {
        return false;
    }

    @Override
    public List<String> lore() {
        return Arrays.asList(
                "",
                "&7Teleport to the active event.",
                ""
        );
    }

    @Override
    public List<String> foundInfo() {
        return Collections.singletonList(
                "KOTH Crate"
        );
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (isSimilar(event.getItem())) {
            if (!isClick(event, "RIGHT")) {
                event.setUseItemInHand(Event.Result.DENY);
                return;
            }

            if (!canUse(player)) {
                event.setUseItemInHand(Event.Result.DENY);
                return;
            }

            Team playerFaction = HCF.getInstance().getTeamHandler().getTeam(player);
            boolean insideTerritory = false;


            if (playerFaction != null && LandBoard.getInstance().getTeam(player.getLocation()) != null && LandBoard.getInstance().getTeam(player.getLocation()).getName().equalsIgnoreCase(playerFaction.getName())) {
                insideTerritory = true;
            }

            if (LandBoard.getInstance().getTeam(player.getLocation()) != null && LandBoard.getInstance().getTeam(player.getLocation()).hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
                insideTerritory = true;
            }

            if (!insideTerritory) {
                player.sendMessage(CC.translate("&cYou must be inside your faction claim or spawn to use this ability item."));
                event.setCancelled(true);
                player.updateInventory();
                return;
            }

            boolean teleported = false;
            for (rip.orbit.hcteams.events.Event koth : HCF.getInstance().getEventHandler().getEvents()) {
                if (koth.isActive()) {
                    Team team = HCF.getInstance().getTeamHandler().getTeam(koth.getName());

                    if (team != null) {
                        World world = Bukkit.getWorld(team.getClaims().get(0).getWorld());
                        int x = team.getClaims().get(0).getX1();
                        int z = team.getClaims().get(0).getZ1();
                        int y = world.getHighestBlockYAt(x, z) + 1;

                        player.teleport(new Location(world, x,y,z).add(0.5, 0.5, 0.5));
                        teleported = true;
                        break;
                    }
                }
            }

            if (teleported) {
                addCooldown(player, 90);
                event.setCancelled(true);
                takeItem(player);

                List<String> hitMsg = Arrays.asList(
                        "",
                        "&bYou have used &lFast Track&b ability!",
                        "");

                hitMsg.forEach(s -> player.sendMessage(CC.chat(s)));

            }
        }
    }
}
