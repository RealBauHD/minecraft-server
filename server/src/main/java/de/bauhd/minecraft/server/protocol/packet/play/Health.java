package de.bauhd.minecraft.server.protocol.packet.play;

import de.bauhd.minecraft.server.protocol.Buffer;
import de.bauhd.minecraft.server.protocol.Protocol;
import de.bauhd.minecraft.server.protocol.packet.Packet;

public final class Health implements Packet {

    @Override
    public void encode(Buffer buf, Protocol.Version version) {
        buf
                .writeFloat(20)
                .writeVarInt(20)
                .writeFloat(2);
    }
}
