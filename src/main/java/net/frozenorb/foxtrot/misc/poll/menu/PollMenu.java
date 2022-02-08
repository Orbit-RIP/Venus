package net.frozenorb.foxtrot.misc.poll.menu;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PollMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return ChatColor.YELLOW + "Current Poll";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int size = (int) (Math.ceil(Foxtrot.getInstance().getPollHandler().getCurrentPoll().getAnswers().size() / 9d) * 9) + 18;

        for (int i = 0; i < 9; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)1, "&e"));
        }

        for (int i = (size - 9); i < size; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)1, "&e"));
        }

        buttons.put(4, new QuestionButton());

        int i = 8;
        for (String str : Foxtrot.getInstance().getPollHandler().getCurrentPoll().getAnswers()) {
            i++;
            buttons.put(i, new AnswerButton(str));
        }

        return buttons;
    }
}
