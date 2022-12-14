package de.bauhd.minecraft.server.protocol.packet.play.container;

import de.bauhd.minecraft.server.protocol.Buffer;
import de.bauhd.minecraft.server.protocol.Protocol;
import de.bauhd.minecraft.server.protocol.packet.Packet;

public final class CloseContainer implements Packet {

    private int id;

    public CloseContainer(final int id) {
        this.id = id;
    }

    public CloseContainer() {}

    @Override
    public void decode(Buffer buf, Protocol.Version version) {
        this.id = buf.readUnsignedByte();
    }

    @Override
    public void encode(Buffer buf, Protocol.Version version) {
        buf.writeUnsignedByte(this.id);
    }

    @Override
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return this.minLength();
    }

    @Override
    public String toString() {
        return "CloseContainer{" +
                "id=" + this.id +
                '}';
    }
}
