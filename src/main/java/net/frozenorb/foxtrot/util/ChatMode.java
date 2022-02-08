package net.frozenorb.foxtrot.util;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public enum ChatMode {

    PUBLIC(ImmutableSet.of('!')),
    ALLIANCE(ImmutableSet.of('#')),
    TEAM(ImmutableSet.of('@')),
    OFFICER(ImmutableSet.of('^'));

    Set<Character> forcedPrefixes;

    public static ChatMode findFromForcedPrefix(char forcedPrefix) {
        for (ChatMode chatMode : values()) {
            for (char chatModePrefix : chatMode.forcedPrefixes) {
                if (chatModePrefix == forcedPrefix) {
                    return chatMode;
                }
            }
        }

        return null;
    }

}