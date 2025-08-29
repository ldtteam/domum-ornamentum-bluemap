package com.ldtteam.domumornamentumbluemap.entity;

import de.bluecolored.bluemap.core.world.mca.blockentity.MCABlockEntity;
import de.bluecolored.bluenbt.NBTName;

import java.util.Map;

/**
 * Block entity definition for Domum materially retexturable blocks.
 */
public class MateriallyTexturedBlockEntity extends MCABlockEntity
{
    /**
     * The extracted texture data from the original block entity.
     */
    @NBTName("textureData")
    private Map<String, String> textureData;

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
