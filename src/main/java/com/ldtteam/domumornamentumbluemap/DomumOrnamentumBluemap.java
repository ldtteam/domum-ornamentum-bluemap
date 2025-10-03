package com.ldtteam.domumornamentumbluemap;

import com.ldtteam.domumornamentumbluemap.registry.MateriallyTexturedBlockEntityType;
import com.ldtteam.domumornamentumbluemap.registry.MateriallyTexturedBlockRendererType;
import com.ldtteam.domumornamentumbluemap.registry.MateriallyTexturedBlockResourcePackExtensionType;
import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.world.mca.blockentity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class DomumOrnamentumBluemap
{
    public DomumOrnamentumBluemap()
    {
        BlockEntityType.REGISTRY.register(new MateriallyTexturedBlockEntityType());
        BlockRendererType.REGISTRY.register(MateriallyTexturedBlockRendererType.INSTANCE);
        ResourcePack.Extension.REGISTRY.register(new MateriallyTexturedBlockResourcePackExtensionType());
    }
}
