package com.ldtteam.domumornamentumbluemap.registry;

import com.ldtteam.domumornamentum.util.Constants;
import com.ldtteam.domumornamentumbluemap.entity.MateriallyTexturedBlockEntity;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.block.entity.BlockEntity;
import de.bluecolored.bluemap.core.world.block.entity.BlockEntityType;

import java.util.Map;

/**
 * Block entity type definition for Domum materially retexturable blocks.
 */
public class MateriallyTexturedBlockEntityType implements BlockEntityType
{
    @Override
    public Key getKey()
    {
        return new Key(Constants.MOD_ID, Constants.BlockEntityTypes.MATERIALLY_RETEXTURABLE);
    }

    @Override
    public BlockEntity load(final Map<String, Object> raw)
    {
        return new MateriallyTexturedBlockEntity(raw);
    }
}
