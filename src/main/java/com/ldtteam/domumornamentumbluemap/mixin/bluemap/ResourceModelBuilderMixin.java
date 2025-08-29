package com.ldtteam.domumornamentumbluemap.mixin.bluemap;

import com.ldtteam.domumornamentumbluemap.entity.MateriallyTexturedBlockEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.bluecolored.bluemap.core.map.hires.blockmodel.ResourceModelBuilder;
import de.bluecolored.bluemap.core.resources.ResourcePath;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockmodel.BlockModel;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockmodel.Face;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variants;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.util.Direction;
import de.bluecolored.bluemap.core.world.block.BlockNeighborhood;
import de.bluecolored.bluemap.core.world.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This mixin modifies the underlying {@link ResourceModelBuilder} from BlueMap to allow special texture selection for Domum blocks, based on the material data.
 * <p></p>
 * Version 5.3 has no dedicated API for this yet, and 1.20.1 will not receive any further versions, hence why a mixin was chosen.
 */
@Mixin(value = ResourceModelBuilder.class, remap = false)
public class ResourceModelBuilderMixin
{
    @Final
    @Shadow
    private ResourcePack resourcePack;

    @Shadow
    private BlockNeighborhood<?> block;

    @ModifyExpressionValue(method = "createElementFace", at = @At(value = "INVOKE", target = "Lde/bluecolored/bluemap/core/resources/pack/resourcepack/blockmodel/TextureVariable;getTexturePath(Ljava/util/function/Function;)Lde/bluecolored/bluemap/core/resources/ResourcePath;"))
    private ResourcePath<Texture> domumornamentumbluemap_getCorrectTexture(
        final @Nullable ResourcePath<Texture> original,
        final @Local(name = "faceDir") Direction faceDir,
        final @Local(name = "face") Face face)
    {
        if (original != null)
        {
            final BlockEntity blockEntity = block.getBlockEntity();
            if (blockEntity instanceof MateriallyTexturedBlockEntity materiallyTexturedBlockEntity)
            {
                final Map<String, String> textureData = materiallyTexturedBlockEntity.getTextureData();

                final String replacementBlock = textureData.getOrDefault(original.toString(), "");
                if (!replacementBlock.isBlank())
                {
                    final Optional<BlockState> blockState = Optional.ofNullable(resourcePack.getBlockState(new ResourcePath<>(replacementBlock)));
                    final Optional<Variant> variant = blockState.map(BlockState::getVariants).map(Variants::getDefaultVariant)
                        .or(() -> blockState.map(BlockState::getVariants).flatMap(m -> Arrays.stream(m.getVariants()).findFirst()))
                        .or(() -> blockState.map(BlockState::getMultipart).flatMap(m -> Arrays.stream(m.getParts()).findFirst()))
                        .flatMap(m -> Arrays.stream(m.getVariants()).findFirst());

                    if (variant.isPresent())
                    {
                        final BlockModel mappedBlockModel = resourcePack.getBlockModel(variant.get().getModel());
                        if (mappedBlockModel != null)
                        {
                            final Optional<Face> mappedFace =
                                Arrays.stream(mappedBlockModel.getElements()).filter(Objects::nonNull).map(element -> element.getFaces().get(faceDir)).findFirst();

                            if (mappedFace.isPresent())
                            {
                                return mappedFace.get().getTexture().getTexturePath(mappedBlockModel.getTextures()::get);
                            }
                        }
                    }
                }
            }
        }

        return original;
    }
}
