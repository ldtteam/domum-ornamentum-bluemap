package com.ldtteam.domumornamentumbluemap.registry;

import com.ldtteam.domumornamentum.util.Constants;
import com.ldtteam.domumornamentumbluemap.renderer.MateriallyTexturedBlockRenderer;
import de.bluecolored.bluemap.core.map.TextureGallery;
import de.bluecolored.bluemap.core.map.hires.RenderSettings;
import de.bluecolored.bluemap.core.map.hires.block.BlockRenderer;
import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.BlockState;

/**
 * Block entity type definition for Domum materially retexturable blocks.
 */
public class MateriallyTexturedBlockRendererType implements BlockRendererType
{
    public static final Key RENDERER_TYPE_KEY = new Key(Constants.MOD_ID, Constants.BlockEntityTypes.MATERIALLY_RETEXTURABLE);

    public static final MateriallyTexturedBlockRendererType INSTANCE = new MateriallyTexturedBlockRendererType();

    @Override
    public Key getKey()
    {
        return RENDERER_TYPE_KEY;
    }

    @Override
    public boolean isFallbackFor(final BlockState blockState)
    {
        return blockState.getId().getNamespace().equals(Constants.MOD_ID);
    }

    @Override
    public BlockRenderer create(final ResourcePack resourcePack, final TextureGallery textureGallery, final RenderSettings renderSettings)
    {
        return new MateriallyTexturedBlockRenderer(resourcePack, textureGallery, renderSettings);
    }
}
