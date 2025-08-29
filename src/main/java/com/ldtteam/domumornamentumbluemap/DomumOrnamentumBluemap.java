package com.ldtteam.domumornamentumbluemap;

import com.ldtteam.domumornamentumbluemap.registry.MateriallyTexturedBlockEntityType;
import de.bluecolored.bluemap.core.world.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class DomumOrnamentumBluemap
{
    public DomumOrnamentumBluemap()
    {
        BlockEntityType.REGISTRY.register(new MateriallyTexturedBlockEntityType());
    }
}
