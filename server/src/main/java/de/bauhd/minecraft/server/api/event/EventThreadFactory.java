package de.bauhd.minecraft.server.api.event;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class EventThreadFactory implements ThreadFactory {

    private final AtomicInteger count = new AtomicInteger(1);

    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        final var thread = new Thread(runnable, "Minecraft Event Executor - " + this.count.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}
