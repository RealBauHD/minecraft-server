package de.bauhd.minecraft.server.protocol.packet.play;

import de.bauhd.minecraft.server.AdvancedMinecraftServer;
import de.bauhd.minecraft.server.api.world.Position;
import de.bauhd.minecraft.server.protocol.Connection;
import de.bauhd.minecraft.server.protocol.Protocol;
import de.bauhd.minecraft.server.protocol.packet.Packet;
import io.netty5.buffer.Buffer;

import static de.bauhd.minecraft.server.protocol.packet.PacketUtils.readPosition;
import static de.bauhd.minecraft.server.protocol.packet.PacketUtils.readVarInt;

public final class UseItemOn implements Packet {

    private int hand;
    private Position position;
    private int face;
    private float x;
    private float y;
    private float z;
    private boolean insideBlock;
    private int sequence;

    @Override
    public void decode(Buffer buf, Protocol.Version version) {
        this.hand = readVarInt(buf);
        this.position = readPosition(buf);
        this.face = readVarInt(buf);
        this.x = buf.readFloat();
        this.y = buf.readFloat();
        this.z = buf.readFloat();
        this.insideBlock = buf.readBoolean();
        this.sequence = readVarInt(buf);
    }

    @Override
    public void handle(Connection connection) {
        final var player = connection.player();
        final var slot = player.getItem(player.getHeldItemSlot() + 36);
        if (slot == null) {
            return;
        }
        AdvancedMinecraftServer.getInstance().sendAll(new BlockUpdate(this.position, slot.materialId())); // not correctly
    }

    @Override
    public String toString() {
        return "UseItemOn{" +
                "hand=" + this.hand +
                ", position=" + this.position +
                ", face=" + this.face +
                ", x=" + this.x +
                ", y=" + this.y +
                ", z=" + this.z +
                ", insideBlock=" + this.insideBlock +
                ", sequence=" + this.sequence +
                '}';
    }
}
