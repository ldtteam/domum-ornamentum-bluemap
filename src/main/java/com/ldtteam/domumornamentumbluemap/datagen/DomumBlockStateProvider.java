package com.ldtteam.domumornamentumbluemap.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ldtteam.domumornamentum.block.ModBlocks;
import com.ldtteam.domumornamentumbluemap.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.ldtteam.domumornamentumbluemap.registry.MateriallyTexturedBlockRendererType.RENDERER_TYPE_KEY;

@SuppressWarnings("ClassCanBeRecord")
public class DomumBlockStateProvider implements DataProvider
{
    private final PackOutput      packOutput;
    private final ResourceManager resourceManager;

    public DomumBlockStateProvider(final PackOutput packOutput, final ResourceManager resourceManager)
    {
        this.packOutput = packOutput;
        this.resourceManager = resourceManager;
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(final @NotNull CachedOutput output)
    {
        final List<ResourceLocation> materialBlocks =
            Arrays.stream(ModBlocks.getMateriallyTexturableBlocks()).map(BuiltInRegistries.BLOCK::getKey).map(m -> m.withPrefix("blockstates/").withSuffix(".json")).toList();

        final List<Map.Entry<ResourceLocation, Resource>> allBlockStates = resourceManager.listResources("blockstates", materialBlocks::contains).entrySet().stream().toList();

        final CompletableFuture<?>[] futures = new CompletableFuture<?>[allBlockStates.size()];
        for (int i = 0; i < allBlockStates.size(); i++)
        {
            futures[i] = saveBlockState(allBlockStates.get(i).getKey(), allBlockStates.get(i).getValue(), output);
        }
        return CompletableFuture.allOf(futures).thenRun(this::blueMapPackCopy);
    }

    @Override
    @NotNull
    public String getName()
    {
        return "Domum Block States Provider";
    }

    private CompletableFuture<?> saveBlockState(final ResourceLocation key, final Resource value, final CachedOutput output)
    {
        try
        {
            final Path outputPath = packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(key.getNamespace()).resolve(key.getPath());

            final JsonElement jsonElement = JsonParser.parseReader(value.openAsReader());
            if (!jsonElement.isJsonObject())
            {
                return DataProvider.saveStable(output, jsonElement, outputPath);
            }

            final JsonObject jsonObject = jsonElement.getAsJsonObject();

            for (final Map.Entry<String, JsonElement> entry : GsonHelper.getAsJsonObject(jsonObject, "variants", new JsonObject()).entrySet())
            {
                if (entry.getValue().isJsonObject())
                {
                    final JsonObject variant = entry.getValue().getAsJsonObject();
                    variant.addProperty("renderer", RENDERER_TYPE_KEY.getFormatted());
                }
            }

            for (final JsonElement entry : GsonHelper.getAsJsonArray(jsonObject, "multipart", new JsonArray()))
            {
                if (entry.isJsonObject())
                {
                    final JsonObject multipart = entry.getAsJsonObject();
                    if (multipart.has("apply") && multipart.get("apply").isJsonObject())
                    {
                        final JsonObject apply = multipart.getAsJsonObject("apply");
                        apply.addProperty("renderer", RENDERER_TYPE_KEY.getFormatted());
                    }
                }
            }

            return DataProvider.saveStable(output, jsonObject, outputPath);
        }
        catch (Exception e)
        {
            return CompletableFuture.failedFuture(e);
        }
    }

    private void blueMapPackCopy()
    {
        try
        {
            final Path sourceDirectory = Paths.get(String.format("../../src/datagen/generated/%s/assets", Constants.MOD_ID));
            final Path targetDirectory = Paths.get("./../client/config/bluemap/packs/local/assets");
            Files.createDirectories(targetDirectory);

            Files.writeString(targetDirectory.getParent().resolve("pack.mcmeta"),
                "{\"pack\":{\"description\":{\"text\":\"Local copy of assets\"},\"pack_format\":48}}",
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
            FileUtils.copyDirectory(sourceDirectory.toFile(), targetDirectory.toFile());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
