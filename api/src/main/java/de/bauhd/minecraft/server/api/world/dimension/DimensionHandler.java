package de.bauhd.minecraft.server.api.world.dimension;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface DimensionHandler {

    void registerDimension(@NotNull Dimension dimension);

    @NotNull Collection<Dimension> getDimensions();

}
