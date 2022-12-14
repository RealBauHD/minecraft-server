package de.bauhd.minecraft.server.api.entity;

import de.bauhd.minecraft.server.AdvancedMinecraftServer;
import de.bauhd.minecraft.server.api.entity.player.GameMode;
import de.bauhd.minecraft.server.api.entity.player.GameProfile;
import de.bauhd.minecraft.server.api.entity.player.Player;
import de.bauhd.minecraft.server.api.inventory.Slot;
import de.bauhd.minecraft.server.api.world.Position;
import de.bauhd.minecraft.server.protocol.Connection;
import de.bauhd.minecraft.server.protocol.Protocol;
import de.bauhd.minecraft.server.protocol.packet.Packet;
import de.bauhd.minecraft.server.protocol.packet.login.Disconnect;
import de.bauhd.minecraft.server.protocol.packet.play.ActionBar;
import de.bauhd.minecraft.server.protocol.packet.play.GameEvent;
import de.bauhd.minecraft.server.protocol.packet.play.SystemChatMessage;
import de.bauhd.minecraft.server.protocol.packet.play.TabListHeaderFooter;
import de.bauhd.minecraft.server.protocol.packet.play.title.Subtitle;
import de.bauhd.minecraft.server.protocol.packet.play.title.TitleAnimationTimes;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public final class MinecraftPlayer extends AbstractLivingEntity implements Player {

    private final Pointers pointers = Pointers.builder()
            .withDynamic(Identity.NAME, this::getUsername)
            .withDynamic(Identity.UUID, this::getUniqueId)
            .withDynamic(Identity.DISPLAY_NAME, this::getDisplayName)
            .build();

    private final Connection connection;
    private final UUID uniqueId;
    private final String name;
    private final GameProfile profile;
    private long lastSendKeepAlive;
    private boolean keepAlivePending;
    private GameMode gameMode = GameMode.CREATIVE;
    private Component displayName;
    private Position position = new Position(8.5, 40, 8.5);
    private int heldItem;
    private final Int2ObjectMap<Slot> slots = new Int2ObjectOpenHashMap<>();

    public MinecraftPlayer(final Connection connection, final UUID uniqueId, final String name, final GameProfile profile) {
        this.connection = connection;
        this.uniqueId = uniqueId;
        this.name = name;
        this.profile = profile;
    }

    public @NotNull UUID getUniqueId() {
        return this.uniqueId;
    }

    public @NotNull String getUsername() {
        return this.name;
    }

    @Override
    public @NotNull GameProfile getProfile() {
        return this.profile;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    @Override
    public @Nullable Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public int getPing() {
        return 1;
    }

    @Override
    public void setDisplayName(@Nullable Component displayName) {
        this.displayName = displayName;
    }

    @Override
    public @NotNull GameMode getGameMode() {
        return this.gameMode;
    }

    @Override
    public void setGameMode(@NotNull GameMode gameMode) {
        if (gameMode != this.gameMode) {
            this.gameMode = gameMode;
            this.send(new GameEvent(3, gameMode.ordinal()));
        }
    }

    @Override
    public void disconnect(@NotNull Component component) {
        this.send(new Disconnect(component));
    }

    @Override
    public int getHeldItemSlot() {
        return this.heldItem;
    }

    @Override
    public void setHeldItemSlot(int slot) {
        this.heldItem = slot;
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        this.send(new SystemChatMessage(message, false));
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        this.send(new ActionBar(message));
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        this.send(new TabListHeaderFooter(header, footer));
    }

    @Override
    public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
        if (part == TitlePart.TITLE) {
            this.send(new de.bauhd.minecraft.server.protocol.packet.play.title.Title((Component) value));
        } else if (part == TitlePart.SUBTITLE) {
            this.send(new Subtitle((Component) value));
        } else if (part == TitlePart.TIMES) {
            final var times = (Title.Times) value;

            this.send(new TitleAnimationTimes(times.fadeIn().getNano(), times.stay().getNano(), times.fadeOut().getNano())); // TODO to ticks
        }
    }

    @Override
    public void showBossBar(@NotNull BossBar bar) {
        AdvancedMinecraftServer.getInstance().getBossBarListener().showBossBar(this, bar);
    }

    @Override
    public void hideBossBar(@NotNull BossBar bar) {
        AdvancedMinecraftServer.getInstance().getBossBarListener().hideBossBar(this, bar);
    }

    @NotNull
    @Override
    public Pointers pointers() {
        return this.pointers;
    }

    @Override
    public @NotNull EntityType getType() {
        return EntityType.PLAYER;
    }

    public void send(final Packet packet) {
        this.connection.send(packet);
    }

    public Slot getItem(final int slot) {
        return this.slots.get(slot);
    }

    public void setItem(final short slot, final Slot clickedItem) {
        this.slots.put(slot, clickedItem);
    }

    public Slot getItemInMainHand() {
        return this.slots.get(36 + this.heldItem);
    }

    public Protocol.Version getVersion() {
        return this.connection.version();
    }

    public long lastSendKeepAlive() {
        return this.lastSendKeepAlive;
    }

    public void setLastSendKeepAlive(final long lastSendKeepAlive) {
        this.lastSendKeepAlive = lastSendKeepAlive;
    }

    public boolean keepAlivePending() {
        return this.keepAlivePending;
    }

    public void setKeepAlivePending(final boolean keepAlivePending) {
        this.keepAlivePending = keepAlivePending;
    }

    // TODO change
    @Override
    public void sendViewers(Packet packet) {
        this.getViewers().forEach(player -> ((MinecraftPlayer) player).connection.send(packet));
    }

    // TODO change
    @Override
    public void sendViewers(Packet packet1, Packet packet2) {
        this.getViewers().forEach(player -> {
                ((MinecraftPlayer) player).connection.send(packet1);
                ((MinecraftPlayer) player).connection.send(packet2);
        });
    }

    @Override
    public @NotNull Collection<Player> getViewers() {
        return AdvancedMinecraftServer.getInstance().getAllPlayers().stream().filter(player -> player != this).toList();
    }
}
