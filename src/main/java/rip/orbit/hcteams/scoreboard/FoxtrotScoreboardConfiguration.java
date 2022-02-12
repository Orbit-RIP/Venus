package rip.orbit.hcteams.scoreboard;

import cc.fyre.proton.scoreboard.config.ScoreboardConfiguration;
import cc.fyre.proton.scoreboard.construct.TitleGetter;
import rip.orbit.hcteams.HCF;

public class FoxtrotScoreboardConfiguration {

    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration configuration = new ScoreboardConfiguration();

        configuration.setTitleGetter(new TitleGetter(HCF.getInstance().getMapHandler().getScoreboardTitle()));
        configuration.setScoreGetter(new FoxtrotScoreGetter());

        return (configuration);
    }

}