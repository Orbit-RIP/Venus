package net.frozenorb.foxtrot.misc.poll.menu;

import lombok.AllArgsConstructor;
import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class AnswerButton extends Button {

    private String str;

    @Override
    public String getName(Player player) {
        return "&6" + str;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList("&7&m--*-----------------------------*--", "  &6» &ePercentage: &f" + Foxtrot.getInstance().getPollHandler().getPercentage(str) + "%", "&7&m--*-----------------------------*--");
    }

    @Override
    public Material getMaterial(Player player) {
        return Foxtrot.getInstance().getPollHandler().hasVoted(player.getUniqueId())
                && Foxtrot.getInstance().getPollHandler().getVoteAnswer(player.getUniqueId()).equalsIgnoreCase(str) ? Material.EMERALD_BLOCK : Material.BOOK;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (!Foxtrot.getInstance().getPollHandler().hasVoted(player.getUniqueId())) {
            Foxtrot.getInstance().getPollHandler().castVote(player, str);
        }
    }
}
