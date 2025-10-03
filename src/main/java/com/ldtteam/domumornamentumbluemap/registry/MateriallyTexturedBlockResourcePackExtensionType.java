package com.ldtteam.domumornamentumbluemap.registry;

import com.ldtteam.domumornamentum.util.Constants;
import com.ldtteam.domumornamentumbluemap.extension.MateriallyTexturedBlockResourcePackExtension;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;

/**
 * Block entity type definition for Domum materially retexturable blocks.
 */
public class MateriallyTexturedBlockResourcePackExtensionType implements ResourcePack.Extension<MateriallyTexturedBlockResourcePackExtension>
{
    public static final Key RENDERER_TYPE_KEY = new Key(Constants.MOD_ID, Constants.BlockEntityTypes.MATERIALLY_RETEXTURABLE);

    @Override
    public Key getKey()
    {
        return RENDERER_TYPE_KEY;
    }

    @Override
    public MateriallyTexturedBlockResourcePackExtension create(final ResourcePack pack)
    {
        return new MateriallyTexturedBlockResourcePackExtension(pack);
    }
}
