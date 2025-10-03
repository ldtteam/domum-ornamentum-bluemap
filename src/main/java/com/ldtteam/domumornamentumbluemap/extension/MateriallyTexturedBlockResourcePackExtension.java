package com.ldtteam.domumornamentumbluemap.extension;

import com.google.gson.JsonObject;
import com.ldtteam.domumornamentum.util.Constants;
import com.ldtteam.domumornamentumbluemap.registry.MateriallyTexturedBlockRendererType;
import de.bluecolored.bluemap.core.BlueMap;
import de.bluecolored.bluemap.core.resources.ResourcePath;
import de.bluecolored.bluemap.core.resources.adapter.ResourcesGson;
import de.bluecolored.bluemap.core.resources.pack.ResourcePool;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import net.minecraft.resources.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class MateriallyTexturedBlockResourcePackExtension implements ResourcePackExtension
{
    private final ResourcePack blueMapResourcePack;

    private final ResourcePool<JsonObject> doBlockModels;

    public MateriallyTexturedBlockResourcePackExtension(final ResourcePack blueMapResourcePack)
    {
        this.blueMapResourcePack = blueMapResourcePack;
        this.doBlockModels = new ResourcePool<>();
    }

    @Override
    public void loadResources(Iterable<Path> roots) throws IOException, InterruptedException
    {
        for (Path root : roots)
        {
            blueMapResourcePack.loadResourcePath(root, this::loadResourcesFromPath);
        }
    }

    public void loadResourcesFromPath(Path root) throws IOException
    {
        try
        {
            CompletableFuture.runAsync(() -> ResourcePack.list(root.resolve("assets"))
                .map(path -> path.resolve("models"))
                .flatMap(ResourcePack::list)
                .filter(path -> !path.getFileName().toString().equals("item"))
                .flatMap(ResourcePack::walk)
                .filter(path -> path.getFileName().toString().endsWith(".json"))
                .filter(Files::isRegularFile)
                .forEach(file -> doBlockModels.load(new ResourcePath<>(root.relativize(file), 1, 3), key -> {
                    try (BufferedReader reader = Files.newBufferedReader(file))
                    {
                        return ResourcesGson.INSTANCE.fromJson(reader, JsonObject.class);
                    }
                })), BlueMap.THREAD_POOL).join();
        }
        catch (RuntimeException ex)
        {
            Throwable cause = ex.getCause();
            if (cause instanceof IOException)
            {
                throw (IOException) cause;
            }
            if (cause != null)
            {
                throw new IOException(cause);
            }
            throw new IOException(ex);
        }
    }

    @Override
    public void bake()
    {
        blueMapResourcePack.getBlockStates().values().forEach(blockState -> blockState.forEach(variant -> {
            final JsonObject jsonObject = doBlockModels.get(variant.getModel());
            if (jsonObject == null)
            {
                return;
            }

            if (jsonObject.has("loader"))
            {
                final ResourceLocation loaderId = ResourceLocation.tryParse(jsonObject.get("loader").getAsString());
                if (Objects.equals(loaderId, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, Constants.MATERIALLY_TEXTURED_MODEL_LOADER)))
                {
                    variant.setRenderer(MateriallyTexturedBlockRendererType.INSTANCE);
                }
            }
        }));
    }
}
