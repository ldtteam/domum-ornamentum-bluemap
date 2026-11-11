package com.ldtteam.domumornamentumbluemap.pack;

import com.ldtteam.domumornamentumbluemap.Constants;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModFileInfo;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Installs the BlueMap-only resource pack shipped inside this mod into BlueMap's packs folder.
 * <p>
 * BlueMap loads its packs folder before any mod jars, so the blockstates in this pack (which point to the custom renderer)
 * always take priority over the ones shipped by Domum Ornamentum, regardless of the order in which the mod jars are listed.
 * The pack is fully replaced on every startup, so it always matches the installed version of this mod.
 */
public class BlueMapPackInstaller
{
    private static final Logger LOGGER = LogUtils.getLogger();

    private BlueMapPackInstaller() {}

    /**
     * Copy the pack into the BlueMap packs folder, replacing any previously installed copy.
     */
    public static void install()
    {
        final Path target = FMLPaths.CONFIGDIR.get().resolve("bluemap").resolve("packs").resolve(Constants.MOD_ID);

        final IModFileInfo modFileInfo = ModList.get().getModFileById(Constants.MOD_ID);
        if (modFileInfo == null)
        {
            LOGGER.error("Could not find own mod file, BlueMap pack will not be installed.");
            return;
        }

        // Datagen writes the pack to the reports output folder, see DomumBlockStateProvider.
        final Path source = modFileInfo.getFile().findResource("reports", Constants.BLUEMAP_PACK_FOLDER);
        if (!Files.isDirectory(source))
        {
            LOGGER.error("BlueMap pack is missing from the mod file ({}), BlueMap pack will not be installed.", source);
            return;
        }

        try
        {
            deleteDirectory(target);
            copyDirectory(source, target);
            LOGGER.info("Installed BlueMap pack to {}. This folder can safely be deleted when this mod is removed.", target);
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to install BlueMap pack to {}.", target, e);
        }
    }

    private static void deleteDirectory(final Path directory) throws IOException
    {
        if (!Files.exists(directory))
        {
            return;
        }

        final List<Path> paths;
        try (Stream<Path> stream = Files.walk(directory))
        {
            // Delete children before their parent directories.
            paths = stream.sorted(Comparator.reverseOrder()).toList();
        }

        for (final Path path : paths)
        {
            Files.delete(path);
        }
    }

    private static void copyDirectory(final Path source, final Path target) throws IOException
    {
        final List<Path> paths;
        try (Stream<Path> stream = Files.walk(source))
        {
            paths = stream.toList();
        }

        for (final Path path : paths)
        {
            // The source lives in the mod jar's file system, so resolve by string to switch over to the target's file system.
            final Path destination = target.resolve(source.relativize(path).toString());
            if (Files.isDirectory(path))
            {
                Files.createDirectories(destination);
            }
            else
            {
                Files.copy(path, destination);
            }
        }
    }
}