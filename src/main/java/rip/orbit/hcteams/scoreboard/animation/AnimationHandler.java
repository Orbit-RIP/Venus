package rip.orbit.hcteams.scoreboard.animation;

import lombok.Getter;
import org.bukkit.Bukkit;
import rip.orbit.hcteams.HCF;

import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 13/02/2022 / 4:38 PM
 * Mars / rip.orbit.mars.util
 */

public class AnimationHandler {

    @Getter private String footer;
    private int currentFooter = 0;

    public AnimationHandler() {
        List<String> footers = Arrays.asList(
                "&7&ostore.orbit.rip",
                "&7&odiscord.orbit.rip",
                "&7&oforums.orbit.rip"
        );

        Bukkit.getScheduler().runTaskTimerAsynchronously(HCF.getInstance(), () -> {
            if (currentFooter == footers.size()) currentFooter = 0;

            footer = footers.get(currentFooter++);
        }, 0L, 5 * 20L);

    }

}
