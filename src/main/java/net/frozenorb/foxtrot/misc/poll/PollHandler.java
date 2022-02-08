package net.frozenorb.foxtrot.misc.poll;

import net.frozenorb.foxtrot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter @Setter
public class PollHandler {

    private Poll currentPoll = null;
    private List<Voter> votedUsers = new ArrayList<>();

    public void createPoll(Poll poll) {
        this.setCurrentPoll(poll);

        Bukkit.broadcastMessage(CC.translate(""));
        Bukkit.broadcastMessage(CC.translate(" &6» &fA &6new poll&f has been created. You can vote on this using &6/poll&e."));
        Bukkit.broadcastMessage(CC.translate(""));
    }

    public boolean hasVoted(UUID uuid) {
        for (Voter voter : this.getVotedUsers()) {
            if (voter.getUuid().equals(uuid)) {
                return true;
            }
        }

        return false;
    }

    public String getVoteAnswer(UUID uuid) {
        for (Voter voter : this.getVotedUsers()) {
            if (voter.getUuid().equals(uuid)) {
                return voter.getAnswer();
            }
        }

        return null;
    }

    public Voter getVote(UUID uuid) {
        for (Voter voter : this.getVotedUsers()) {
            if (voter.getUuid().equals(uuid)) {
                return voter;
            }
        }

        return null;
    }

    public void castVote(Player player, String answer) {
        if (hasVoted(player.getUniqueId())) {
            this.votedUsers.remove(this.getVote(player.getUniqueId()));

            this.votedUsers.add(new Voter(player.getUniqueId(), answer));

            player.sendMessage(CC.translate(" &6» &fYou have successfully casted your vote for &6" + answer + "&f."));
            return;
        }

        this.getVotedUsers().add(new Voter(player.getUniqueId(), answer));

        player.sendMessage(CC.translate(" &6» &fYou have successfully casted your vote for &6" + answer + "&f."));
    }

    public int getPercentage(String answer) {
        int amount = 0;

        for (Voter voter : getVotedUsers()) {
            if (voter.getAnswer().equalsIgnoreCase(answer)) {
                amount++;
            }
        }

        float percentage = (float) (amount * 100) / getVotedUsers().size();

        return (int) percentage;
    }

    public String getWinner() {
        String a = null;
        int i = 0;

        for (String answer : this.getCurrentPoll().getAnswers()) {
            if (this.getPercentage(answer) > i) {
                i = this.getPercentage(answer);
                a = answer;
            }
        }

        return a;
    }


}
