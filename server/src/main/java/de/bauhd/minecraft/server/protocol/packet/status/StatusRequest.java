package de.bauhd.minecraft.server.protocol.packet.status;

import de.bauhd.minecraft.server.protocol.Connection;
import de.bauhd.minecraft.server.protocol.packet.Packet;

public final class StatusRequest implements Packet {

    public static final StatusRequest INSTANCE = new StatusRequest();

    @Override
    public boolean handle(Connection connection) {
        connection.send(new StatusResponse());
        return false;
    }

    @Override
    public int maxLength() {
        return 0;
    }

    @Override
    public String toString() {
        return "StatusRequest";
    }
}
