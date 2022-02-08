package net.frozenorb.foxtrot.misc.nametag;

import jdk.nashorn.internal.objects.Global;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.StingerClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.ArcherClass;
import net.frozenorb.foxtrot.team.Team;
import cc.fyre.proton.nametag.NametagInfo;
import cc.fyre.proton.nametag.NametagProvider;
import org.bukkit.potion.PotionEffectType;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

import java.util.*;

public class FoxtrotNametagProvider extends NametagProvider {

    public FoxtrotNametagProvider() {
        super("Foxtrot Provider", 5);
    }

    public static final List<String> COLOR_CODES = Arrays.asList("§a", "§b", "§c", "§d", "§e", "§f",
            "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8");

    private final Map<Team, String> teamMap = new HashMap<>();

    @Override
    public NametagInfo fetchNametag(Player toRefresh, Player refreshFor) {
        Profile toRefreshProfile = Nebula.getInstance().getProfileHandler().fromUuid(toRefresh.getUniqueId());
        Profile refreshForProfile = Nebula.getInstance().getProfileHandler().fromUuid(refreshFor.getUniqueId());
        boolean isTeammate = false;
        boolean isArcherTagged = false;
        Team viewerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(refreshFor);
        Team nonViewerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(toRefresh);
        NametagInfo nametagInfo = null;

//        if (ModSuiteAPI.isInModMode(toRefresh)) {
//            nametagInfo = createNametag(toRefresh, "§3[S] §b", "");
//        if (toRefresh.hasMetadata("invisible")) {
//            nametagInfo = createNametag(toRefresh, "§2[V] §a", "");
//        }

        if (viewerTeam != null) {
            if (viewerTeam.isMember(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, ChatColor.GREEN.toString(), "");
                isTeammate = true;
            } else if (viewerTeam.isAlly(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, Team.ALLY_COLOR.toString(), "");
            }
        }

        // If we already found something above they override these, otherwise we can do these checks.
        if (StingerClass.getMarkedPlayers().containsKey(toRefresh.getUniqueId()) && StingerClass.getMarkedPlayers().get(toRefresh.getUniqueId()) > System.currentTimeMillis()) {
            nametagInfo = createNametag(toRefresh, Foxtrot.getInstance().getServerHandler().getStunTagColor().toString(), "");
        } else if (ArcherClass.getMarkedPlayers().containsKey(toRefresh.getName()) && ArcherClass.getMarkedPlayers().get(toRefresh.getName()) > System.currentTimeMillis()) {
            nametagInfo = createNametag(toRefresh, Foxtrot.getInstance().getServerHandler().getArcherTagColor().toString(), "");
            isArcherTagged = true;
        } else if (ArcherClass.getMarkedPlayers().containsKey(toRefresh.getName()) && ArcherClass.getMarkedPlayers().get(toRefresh.getName()) > System.currentTimeMillis() && viewerTeam != null && viewerTeam.isMember(toRefresh.getUniqueId())) {
            nametagInfo = createNametag(toRefresh, ChatColor.GREEN.toString(), "");
            isArcherTagged = false;
        } else if (viewerTeam != null && viewerTeam.getFocused() != null && viewerTeam.getFocused().equals(toRefresh.getUniqueId())) {
            nametagInfo = createNametag(toRefresh, ChatColor.LIGHT_PURPLE.toString(), "");
        } else if (viewerTeam != null && viewerTeam.getFocusedTeam() != null && viewerTeam.getFocusedTeam().equals(nonViewerTeam)) {
            nametagInfo = createNametag(toRefresh, ChatColor.LIGHT_PURPLE.toString(), "");
        } else if (Foxtrot.getInstance().getNoFactionFocusFaction().getPlayerNoFactionTeamMap().get(refreshFor.getUniqueId()) != null && Foxtrot.getInstance().getNoFactionFocusFaction().getPlayerNoFactionTeamMap().get(refreshFor.getUniqueId()) == nonViewerTeam) {
            nametagInfo = createNametag(toRefresh, ChatColor.LIGHT_PURPLE.toString(), "");
        }

        if (toRefresh.hasPotionEffect(PotionEffectType.INVISIBILITY) && refreshFor != toRefresh && !isArcherTagged && !isTeammate) {
            if (viewerTeam == null)
                nametagInfo = createNametag("", "");
            if (viewerTeam != null && !viewerTeam.isMember(toRefresh.getUniqueId()))
                nametagInfo = createNametag("", "");
        }

        // If nothing custom was set, fall back on yellow.
        return (nametagInfo == null ?
                createNametag(toRefresh, Foxtrot.getInstance().getServerHandler().getDefaultRelationColor().toString(), "") : nametagInfo);
    }

    private NametagInfo createNametag(Player displayed, String prefix, String suffix) {
        return createNametag(prefix, suffix);
    }
}
