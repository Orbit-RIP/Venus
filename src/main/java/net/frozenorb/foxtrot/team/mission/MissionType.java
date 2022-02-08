package net.frozenorb.foxtrot.team.mission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.menu.MissionMenu;
import net.frozenorb.foxtrot.util.ProgressBarBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * @author xanderume@gmail (JavaProject)
 */
@AllArgsConstructor

//TODO: need a better way of creating mission's as this is fucking cancerous

public enum MissionType {

    ENDER_MAN(
            40,
            100,
            team -> team.getEnderManKills().get(),
            Material.ENDER_PEARL,
            "Enderman Mission!",
            "Kill " + ChatColor.WHITE + "100" + ChatColor.GRAY + " enderman to complete this mission!"
    ),
    DIAMOND_ORE(
            40,
            100,
            Team::getDiamondsMined,
            Material.DIAMOND_PICKAXE,
            "Diamonds Mission!",
            "Mine " + ChatColor.WHITE + "100" + ChatColor.GRAY + " diamond ores to complete this mission!"

    ),
    FACTION_KILLS(
            50,
            15,
            Team::getKills,
            Material.DIAMOND_SWORD,
            "Slaughter Mission!",
            "Kill " + ChatColor.WHITE + "15" + ChatColor.GRAY + " players to complete this mission!"
    ),
    KOTH_CAPTURES(
            60,
            10,
            Team::getKothCaptures,
            Material.PAINTING,
            "King Of The Hill Mission!",
            "Capture " + ChatColor.WHITE + "10" + ChatColor.GRAY + " KOTHs to complete this mission!"
    ),
    TEAM_POINTS(
            100,
            60,
            Team::getPoints,
            Material.EMERALD,
            "Points Mission!",
            "Obtain " + ChatColor.WHITE + "60" + ChatColor.GRAY + " points to complete this mission!"
    );

    @Getter private int tokens;
    @Getter private int requiredAmount;
    private MissionTypeHandler requiredVariable;
    @Getter private Material material;
    @Getter private String title;
    @Getter private String description;

    public boolean hasCompleted(Team team) {
        return this.requiredVariable.provider(team) >= this.requiredAmount;
    }

    public int getRequiredVariable(Team team) {
        return this.requiredVariable.provider(team);
    }

    public String getProgressBar(Team team) {
        return new ProgressBarBuilder(20).blockChar("|".charAt(0)).build((ProgressBarBuilder.percentage(this.requiredVariable.provider(team),this.requiredAmount)));
    }

    public static MissionMenu getMenu(Team team) {
        return new MissionMenu(team);
    }
}

