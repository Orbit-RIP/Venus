package net.frozenorb.foxtrot.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cc.fyre.proton.command.param.ParameterType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mysql.jdbc.StringUtils;

import net.frozenorb.foxtrot.Foxtrot;

public class EventParameterType implements ParameterType<Event> {

    public Event transform(CommandSender sender, String source) {
        if (source.equals("active")) {
            for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
                if (event.isActive() && !event.isHidden()) {
                    return event;
                }
            }

            sender.sendMessage(ChatColor.RED + "There is no active Event at the moment.");

            return null;
        }

        Event event = Foxtrot.getInstance().getEventHandler().getEvent(source);

        if (event == null) {
            sender.sendMessage(ChatColor.RED + "No Event with the name " + source + " found.");
            return (null);
        }

        return (event);
    }

    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        List<String> completions = new ArrayList<>();

        for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
            if (StringUtils.startsWithIgnoreCase(event.getName(), source)) {
                completions.add(event.getName());
            }
        }

        return (completions);
    }

}