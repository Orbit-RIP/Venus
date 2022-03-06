package rip.orbit.hcteams.scoreboard;

import cc.fyre.proton.Proton;
import cc.fyre.proton.scoreboard.config.ScoreboardConfiguration;
import cc.fyre.proton.scoreboard.construct.TitleGetter;
import org.bukkit.Bukkit;
import rip.orbit.hcteams.HCF;

import java.util.Arrays;
import java.util.List;

public class FoxtrotScoreboardConfiguration {

    private static int currentTitle = 0;

    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration configuration = new ScoreboardConfiguration();

        configuration.setTitleGetter(new TitleGetter(getATitle() + HCF.getInstance().getMapHandler().getScoreboardTitle()));
        configuration.setScoreGetter(new FoxtrotScoreGetter());

        return (configuration);
    }

    public static String getATitle() {
        List<String> titles = Arrays.asList(
                "&6&LHCFACTIONS",
                "&f&lH&6&lCFACTIONS",
                "&f&lHC&6&lFACTIONS",
                "&f&lHCF&6&lACTIONS",
                "&f&lHCFA&6&lCTIONS",
                "&f&lHCFAC&6&lTIONS",
                "&f&lHCFACT&6&lIONS",
                "&f&lHCFACTI&6&lONS",
                "&f&lHCFACTIO&6&lNS",
                "&f&lHCFACTION&6&lS",
                "&f&lHCFACTIONS&6&l",
                "&f&lHCFACTION&e&lS",
                "&f&lHCFACTIO&e&lNS",
                "&f&lHCFACTI&e&lONS",
                "&f&lHCFACT&e&lIONS",
                "&f&lHCFAC&e&lTIONS",
                "&f&lHCFA&e&lCTIONS",
                "&f&lHCF&e&lACTIONS",
                "&f&lHC&e&lFACTIONS",
                "&f&lH&e&lCFACTIONS",
                "&f&l&e&lHCFACTIONS",
                "&e&lHCFACTIONS",
                "&e&lHCFACTIONS",
                "&7&lHCFACTIONS",
                "&7&lHCFACTIONS",
                "&6&lHCFACTIONS",
                "&6&lHCFACTIONS",
                "&6&lHCFACTIONS"
        );

        Bukkit.getScheduler().runTaskTimerAsynchronously(HCF.getInstance(), () -> {
            if (currentTitle == titles.size()) currentTitle = 0;

            String newTitle = titles.get(currentTitle++);

            Proton.getInstance().getScoreboardHandler().getConfiguration().setTitleGetter(new TitleGetter(newTitle));
        }, 40L, (long) (0.3 * 20L));
        return null;
    }

}