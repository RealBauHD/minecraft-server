package de.bauhd.minecraft.server.protocol.packet.play;

import de.bauhd.minecraft.server.AdvancedMinecraftServer;
import de.bauhd.minecraft.server.api.world.Position;
import de.bauhd.minecraft.server.protocol.Connection;
import de.bauhd.minecraft.server.protocol.Protocol;
import de.bauhd.minecraft.server.protocol.packet.Packet;
import io.netty5.buffer.Buffer;

import static de.bauhd.minecraft.server.protocol.packet.PacketUtils.readPosition;
import static de.bauhd.minecraft.server.protocol.packet.PacketUtils.readVarInt;

public final class PlayerAction implements Packet {

    private int status;
    private Position position;
    private byte face;
    private int sequence;

    @Override
    public void decode(Buffer buf, Protocol.Version version) {
        this.status = readVarInt(buf);
        this.position = readPosition(buf);
        this.face = buf.readByte();
        this.sequence = readVarInt(buf);
    }

    @Override
    public boolean handle(Connection connection) {
        if (this.status == 0) { // 0 only if instant break
            // TODO get chunk viewers
            AdvancedMinecraftServer.getInstance().sendAll(new BlockUpdate(this.position, 0));
        }
        return false;
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
                "status=" + this.status +
                ", position=" + this.position +
                ", face=" + this.face +
                ", sequence=" + this.sequence +
                '}';
    }
}
