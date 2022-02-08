package net.frozenorb.foxtrot.listener;

import com.lunarclient.bukkitapi.LunarClientAPI;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.misc.nametag.FoxtrotNametagProvider;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.dtr.DTRHandler;
import net.frozenorb.foxtrot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

public class ClientListener implements Listener {

    public ClientListener() {
        Bukkit.getScheduler().runTaskTimer(Foxtrot.getInstance(), () -> {
            double tps = Bukkit.spigot().getTPS()[1];
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Bukkit.getOnlinePlayers().forEach(player -> LunarClientAPI.getInstance().overrideNametag(onlinePlayer, fetchNametag(onlinePlayer, player), player));
            }
        }, 0, 40);
    }

    public Comparator<Team> compareFactionPoints = Comparator.comparingLong(Team::getPoints);

    public List<String> fetchNametag(Player target, Player viewer) {
        String nameTag = (target.hasMetadata("invisible")? ChatColor.GRAY + "*" : "") + new FoxtrotNametagProvider().fetchNametag(target, viewer).getPrefix() + target.getName();
        List<String> tag = new ArrayList<>();
        if (!target.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            Team team = Foxtrot.getInstance().getTeamHandler().getTeam(target);
            List<Team> Teams = Foxtrot.getInstance().getTeamHandler().getTeams().stream().filter(Objects::nonNull).filter(x -> x.getPoints() > 0).distinct().sorted(compareFactionPoints).collect(Collectors.toList());
            Collections.reverse(Teams);
            if (team != null) {
                tag.add(ChatColor.GOLD + "[" + Foxtrot.getInstance().getServerHandler().getDefaultRelationColor().toString() + team.getName(viewer) + CC.translate("&7 ") + team.getDTRColor() + team.getDTRSuffix() + ChatColor.GOLD + "]");
            }
            if (target.hasMetadata("invisible") && team == null) {
                tag.add(CC.translate("&7[Mod Mode]"));
            } else if (target.hasMetadata("invisible") && team != null) {
                tag.add(CC.translate("&7[Mod Mode]"));
            }
        }
        tag.add(nameTag);
        return tag;
    }
}