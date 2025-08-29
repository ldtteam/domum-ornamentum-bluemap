package com.ldtteam.domumornamentumbluemap;

import com.ldtteam.domumornamentumbluemap.datagen.DomumBlockStateProvider;
import com.ldtteam.domumornamentumbluemap.registry.MateriallyTexturedBlockEntityType;
import com.ldtteam.domumornamentumbluemap.registry.MateriallyTexturedBlockRendererType;
import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.world.mca.blockentity.BlockEntityType;
import net.minecraft.server.packs.PackType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(Constants.MOD_ID)
public class DomumOrnamentumBluemap
{
    public DomumOrnamentumBluemap(final IEventBus modBus)
    {
        BlockEntityType.REGISTRY.register(new MateriallyTexturedBlockEntityType());
        BlockRendererType.REGISTRY.register(new MateriallyTexturedBlockRendererType());

        modBus.addListener(this::dataGeneratorSetup);
    }

    private void dataGeneratorSetup(final GatherDataEvent event)
    {
        event.getGenerator()
            .addProvider(true,
                new DomumBlockStateProvider(event.getGenerator().getPackOutput(), event.getResourceManager(PackType.CLIENT_RESOURCES)));
    }
}
