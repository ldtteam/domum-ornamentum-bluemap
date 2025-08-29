package com.ldtteam.domumornamentumbluemap.entity;

import de.bluecolored.bluemap.core.world.block.entity.BlockEntity;

import java.util.Map;

/**
 * Block entity definition for Domum materially retexturable blocks.
 */
public class MateriallyTexturedBlockEntity extends BlockEntity
{
    /**
     * The extracted texture data from the original block entity.
     */
    private final Map<String, String> textureData;

    /**
     * Default constructor.
     *
     * @param raw the raw map of texture data.
     */
    @SuppressWarnings("unchecked")
    public MateriallyTexturedBlockEntity(final Map<String, Object> raw)
    {
        super(raw);
        this.textureData = (Map<String, String>) raw.getOrDefault("textureData", Map.of());
    }

    /**
     * Get the texture data from the block entity.
     *
     * @return the map of texture data info.
     */
    public Map<String, String> getTextureData()
    {
        return textureData;
    }
}
