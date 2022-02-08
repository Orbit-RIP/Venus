package net.frozenorb.foxtrot.misc.poll;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Poll {
    private String question;
    private List<String> answers = new ArrayList<>();

    public Poll(String question, List<String> answers) {
        this.question = question;
        this.answers = answers;
    }

}
