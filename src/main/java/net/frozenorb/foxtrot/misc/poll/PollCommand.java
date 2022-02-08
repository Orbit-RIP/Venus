package net.frozenorb.foxtrot.misc.poll;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.misc.poll.menu.PollMenu;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PollCommand {

    @Command(names = {"poll", "polls"}, async = true, permission = "")
    public static void pollcmd(Player sender) {
        if (Foxtrot.getInstance().getPollHandler().getCurrentPoll() != null) {
            new PollMenu().openMenu(sender);
        }
    }

    @Command(names = { "poll create", "polls create" }, async = true, permission = "poll.admin")
    public static void pollcreate(Player sender, @Parameter(name = "question", wildcard = true)String question) {
        Foxtrot.getInstance().getPollHandler().createPoll(new Poll(question, Collections.emptyList()));
    }

    @Command(names = { "poll addanswer", "polls addanswer"}, async = true, permission = "poll.admin")
    public static void polladdanswer(Player sender, @Parameter(name = "addanswer", wildcard = true)String answer) {
        List<String> answers = new ArrayList<>(Foxtrot.getInstance().getPollHandler().getCurrentPoll().getAnswers());

        if (answers.contains(answer)) {
            sender.sendMessage(CC.translate("&cThat answer already exists."));
            return;
        }

        answers.add(answer);

        Foxtrot.getInstance().getPollHandler().getCurrentPoll().setAnswers(answers);

        sender.sendMessage(CC.translate(" &6» &fYou have added &7\"&6" + answer + "&7\" &fto the answer list."));
    }

    @Command(names = { "poll close", "polls close" }, async = true, permission = "poll.admin")
    public static void pollclose(Player sender) {
        Bukkit.broadcastMessage(CC.translate("&7&m--*------------------------------*--"));
        Bukkit.broadcastMessage(CC.translate(""));
        Bukkit.broadcastMessage(CC.translate(" &6» &fThe poll has ended. The results are the following:"));

        for (String answer : Foxtrot.getInstance().getPollHandler().getCurrentPoll().getAnswers()) {
            Bukkit.broadcastMessage(CC.translate("   &7* &6" + answer + "&7: &f" + Foxtrot.getInstance().getPollHandler().getPercentage(answer) + "%"));
        }

        Bukkit.broadcastMessage(CC.translate(""));
        Bukkit.broadcastMessage(CC.translate("&7&m--*------------------------------*--"));

        sender.sendMessage(CC.translate(" &6» &fYou have closed the poll. The winner was &7\"&6" + Foxtrot.getInstance().getPollHandler().getWinner() + "&7\"&f."));
        Foxtrot.getInstance().getPollHandler().setCurrentPoll(null);
        Foxtrot.getInstance().getPollHandler().setVotedUsers(new ArrayList<>());
    }

    @Command(names = { "poll delete", "polls delete" }, async = true, permission = "poll.admin")
    public static void polldelete(Player sender) {
        Foxtrot.getInstance().getPollHandler().setCurrentPoll(null);
        Foxtrot.getInstance().getPollHandler().setVotedUsers(new ArrayList<>());
        sender.sendMessage(CC.translate(" &6» &fYou have deleted the current poll."));
    }

}
