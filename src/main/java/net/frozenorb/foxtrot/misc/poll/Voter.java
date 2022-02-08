package net.frozenorb.foxtrot.misc.poll;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Voter {
    private UUID uuid;
    private String answer;

    public Voter(UUID uuid, String answer) {
        this.uuid = uuid;
        this.answer = answer;
    }

}
