package de.bauhd.minecraft.server.protocol.packet.play;

import de.bauhd.minecraft.server.protocol.Buffer;
import de.bauhd.minecraft.server.protocol.Connection;
import de.bauhd.minecraft.server.protocol.Protocol;
import de.bauhd.minecraft.server.protocol.packet.Packet;

public final class SwingArm implements Packet {

    private int hand;

    @Override
    public void decode(Buffer buf, Protocol.Version version) {
        this.hand = buf.readVarInt();
    }

    @Override
    public boolean handle(Connection connection) {
        connection.player().sendViewers(new EntityAnimation(connection.player().getId(), (byte) (this.hand == 1 ? 3 : 0)));
        return false;
    }

    @Override
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return 5;
    }

    @Override
    public String toString() {
        return "SwingArm{" +
                "hand=" + this.hand +
                '}';
    }
}
