package rip.orbit.hcteams.map.stats;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.stats.command.StatsTopCommand;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;
import rip.orbit.nebula.profile.stat.GlobalStatistic;
import rip.orbit.nebula.profile.stat.StatType;

import java.util.UUID;

public class StatsEntry {

    @Getter(value = AccessLevel.PROTECTED) private boolean modified;

    @Getter private UUID owner;
    @Getter private String faction;

    @Getter
    @Setter
    private int kills;

    @Getter
    @Setter
    private int deaths;

    @Getter
    @Setter
    private int killstreak;

    @Getter private int highestKillstreak;

    public StatsEntry(UUID owner) {
        this.owner = owner;
    }
    public StatsEntry(String owner) {
        this.faction = owner;
    }

    public void addKill() {
        kills++;
        killstreak++;

        if (highestKillstreak < killstreak) {
            highestKillstreak = killstreak;
        }

        Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(getOwner());
        for (GlobalStatistic statistic : profile.getGlobalStatistics()) {
            if (HCF.getInstance().getMapHandler().isKitMap()) {
                if (statistic.getStatType() == StatType.KITS) {
                    statistic.setKills(statistic.getKills() + 1);
                    statistic.setKillStreak(killstreak);
                    statistic.setHighestKillStreak(highestKillstreak);
                    break;
                }
            } else {
                if (statistic.getStatType() == StatType.HCF) {
                    statistic.setKills(statistic.getKills() + 1);
                    statistic.setKillStreak(killstreak);
                    statistic.setHighestKillStreak(highestKillstreak);
                    break;
                }
            }
        }

        profile.save();


        modified = true;
    }

    public void addDeath() {

        Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(getOwner());
        for (GlobalStatistic statistic : profile.getGlobalStatistics()) {
            if (HCF.getInstance().getMapHandler().isKitMap()) {
                if (statistic.getStatType() == StatType.KITS) {
                    statistic.setDeaths(statistic.getDeaths() + 1);
                    statistic.setKillStreak(0);
                    break;
                }
            } else {
                if (statistic.getStatType() == StatType.HCF) {
                    statistic.setDeaths(statistic.getDeaths() + 1);
                    statistic.setKillStreak(0);
                    break;
                }
            }
        }

        deaths++;
        killstreak = 0;

        modified = true;
    }

    public void clear() {
        kills = 0;
        deaths = 0;
        killstreak = 0;
        highestKillstreak = 0;

        modified = true;
    }

    public double getKD() {
        if (getDeaths() == 0) {
            return 0;
        }

        return (double) getKills() / (double) getDeaths();
    }

    public Number get(StatsTopCommand.StatsObjective objective) {
        switch (objective) {
            case KILLS:
                return getKills();
            case DEATHS:
                return getDeaths();
            case KD:
                return getKD();
            case HIGHEST_KILLSTREAK:
                return getHighestKillstreak();
            default:
                return 0;
        }
    }
}
