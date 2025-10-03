package com.ldtteam.domumornamentumbluemap.registry;

import com.ldtteam.domumornamentum.util.Constants;
import com.ldtteam.domumornamentumbluemap.entity.MateriallyTexturedBlockEntity;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.BlockEntity;
import de.bluecolored.bluemap.core.world.mca.blockentity.BlockEntityType;

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
    public Class<? extends BlockEntity> getBlockEntityClass()
    {
        return MateriallyTexturedBlockEntity.class;
    }
}
