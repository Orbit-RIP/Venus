package net.frozenorb.foxtrot.util;

import cc.fyre.proton.command.param.ParameterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by PVPTUTORIAL | Created on 08/05/2020
 */

@Data
@AllArgsConstructor
@Getter
public class DurationParameter {
    private Long duration;
    private String source;
    private boolean perm;

    public DurationParameter(long duration, String source, boolean perm) {
        this.duration = duration;
        this.source = source;
        this.perm = perm;
    }

    public static class Type implements ParameterType {
        public DurationParameter transform(CommandSender sender, String source) {
            try {

                final int toReturn = TimeUtils.parseTime(source); // parse time

                if ((toReturn * 1000L) <= 0) {
                    sender.sendMessage(ChatColor.RED + "Duration must be higher then 0.");
                    return null;
                }

                return new DurationParameter(toReturn * 1000L, source, false); // return if it valid
            } catch (NullPointerException | IllegalArgumentException ex) {
                return new DurationParameter(Long.valueOf(Integer.MAX_VALUE), source, true); // return permanent if it is invalid
            }
        }

        public List tabComplete(Player sender, Set flags, String source) {
            List completions = new ArrayList();
            return completions;
        }
    }
}