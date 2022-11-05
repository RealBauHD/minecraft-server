package de.bauhd.minecraft.server.protocol.packet.play.title;

import de.bauhd.minecraft.server.MinecraftServer;
import de.bauhd.minecraft.server.protocol.Protocol;
import de.bauhd.minecraft.server.protocol.packet.Packet;
import io.netty5.buffer.Buffer;
import net.kyori.adventure.text.Component;

import static de.bauhd.minecraft.server.protocol.packet.PacketUtils.writeString;

public final class Subtitle implements Packet {

    private Component text;

    public Subtitle(final Component text) {
        this.text = text;
    }

    public Subtitle() {}

    @Override
    public void decode(Buffer buf, Protocol.Version version) {

    }

    @Override
    public void encode(Buffer buf, Protocol.Version version) {
        writeString(buf, MinecraftServer.getGsonSerializer(version).serialize(this.text));
    }

    @Override
    public String toString() {
        return "Subtitle{" +
                "text=" + this.text +
                '}';
    }
}