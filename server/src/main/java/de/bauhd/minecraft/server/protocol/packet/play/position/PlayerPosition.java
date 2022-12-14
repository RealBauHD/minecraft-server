package de.bauhd.minecraft.server.protocol.packet.play.position;

import de.bauhd.minecraft.server.api.world.Position;
import de.bauhd.minecraft.server.protocol.Buffer;
import de.bauhd.minecraft.server.protocol.Connection;
import de.bauhd.minecraft.server.protocol.Protocol;
import de.bauhd.minecraft.server.protocol.packet.Packet;

public final class PlayerPosition implements Packet {

    private double x;
    private double y;
    private double z;
    private boolean onGround;

    @Override
    public void decode(Buffer buf, Protocol.Version version) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.onGround = buf.readBoolean();
    }

    @Override
    public boolean handle(Connection connection) {
        final var player = connection.player();
        final var position = player.getPosition();
        player.sendViewers(new EntityPosition(player.getId(),
                this.delta(position.x(), this.x), this.delta(position.y(), this.y), this.delta(position.z(), this.z), this.onGround));
        player.setPosition(new Position(this.x, this.y, this.z, position.yaw(), position.pitch()));
        return false;
    }

    @Override
    public int minLength() {
        return 25;
    }

    @Override
    public int maxLength() {
        return this.minLength();
    }

    private short delta(final double previous, final double current) {
        return (short) ((current * 32 - previous * 32) * 128);
    }
}
