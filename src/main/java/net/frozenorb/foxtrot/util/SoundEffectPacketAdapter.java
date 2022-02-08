package net.frozenorb.foxtrot.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.frozenorb.foxtrot.Foxtrot;

public class SoundEffectPacketAdapter extends PacketAdapter {

    public SoundEffectPacketAdapter() {
        super(Foxtrot.getInstance(), PacketType.Play.Server.NAMED_SOUND_EFFECT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {

//        event.getPlayer().sendMessage(event.getPacket().toString());
//        event.getPlayer().sendMessage(event.getPacketType().toString());
    }
}
