package net.frozenorb.foxtrot.misc.poll.menu;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class QuestionButton extends Button {

    @Override
    public String getName(Player player) {
        return "&6Question: &e" + Foxtrot.getInstance().getPollHandler().getCurrentPoll().getQuestion();
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList("&7&m--*-----------------------------*--", "  &6» &eResponses: &f" + Foxtrot.getInstance().getPollHandler().getVotedUsers().size(),"&7&m--*-----------------------------*--");
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.PAPER;
    }
}
