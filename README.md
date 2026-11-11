# Domum Ornamentum - BlueMap Integration

Make your [Domum Ornamentum](https://www.curseforge.com/minecraft/mc-mods/domum-ornamentum) builds show up on [BlueMap](https://www.curseforge.com/minecraft/mc-mods/bluemap) the way they look in game.

[CurseForge](https://www.curseforge.com/minecraft/mc-mods/domum-ornamentum-bluemap-integration) · [Issues](https://github.com/ldtteam/domum-ornamentum-bluemap/issues)

## About

Domum Ornamentum lets you craft decorative blocks such as shingles, timber frames, pillars, paper walls and fancy doors from almost any other block. A roof made from yellow concrete really is yellow, and a timber frame made from dark oak and stone uses exactly those textures.

BlueMap does not know about this. On its own, BlueMap only sees the grey placeholder textures that Domum ships with, so every Domum roof and wall on your map ends up looking the same. This is especially noticeable with MineColonies, where most colony buildings are made from Domum blocks.

This addon teaches BlueMap how Domum blocks work. It reads the materials stored in each block and renders the block with those textures, so your map matches your world.

- Works with all materially textured Domum Ornamentum blocks.
- Only needs to be installed where BlueMap runs, which is usually the server.
- Changes nothing in game. It only affects what BlueMap renders.

## Requirements

| Minecraft | Loader   | Requires                       |
|-----------|----------|--------------------------------|
| 1.21.1    | NeoForge | BlueMap 5.7, Domum Ornamentum  |
| 1.20.1    | Forge    | BlueMap 5.12, Domum Ornamentum |

These are the BlueMap versions the addon is built and tested against. Other BlueMap versions may work, but aren't guaranteed to.

## Installation

1. Install BlueMap and Domum Ornamentum.
2. Put this mod in your `mods/` folder.
3. Start the server. If you already had a map, run `/bluemap force-update` once so the existing tiles are rendered again with the right colors.

## FAQ

### My Domum blocks are still gray on the map

- **Re-render the map.** Tiles that were rendered before this addon was installed keep their old look until they are rendered again. Run `/bluemap force-update`.
- **Update the addon.** In older versions (1.1.0 and earlier) whether the addon worked depended on the order in which the server happened to list the files in `mods/`, so on some servers it silently did nothing. Newer versions don't have this problem.
- If it still happens on the latest version, please [open an issue](https://github.com/ldtteam/domum-ornamentum-bluemap/issues).

### What is the `config/bluemap/packs/domumornamentumbluemap` folder?

On every startup the addon places a small resource pack there. It contains the instructions that tell BlueMap to use this addon to render Domum blocks. BlueMap loads that folder before anything in `mods/`, so these instructions always take priority over the ones that come with Domum Ornamentum itself.

The folder is replaced every time the server starts, so it always matches the installed version of the addon. Don't edit it, your changes will be overwritten.

### I removed the addon, do I need to clean anything up?

Yes, delete the `config/bluemap/packs/domumornamentumbluemap` folder. The addon can't remove it itself once it is gone.

Leaving it behind won't break anything: BlueMap logs a warning about an unknown renderer and draws Domum blocks with their grey placeholder textures, just like it would without the addon. However, if a future Domum Ornamentum update changes its block files, the leftover copy could make some Domum blocks render incorrectly on the map. Deleting the folder avoids that.

### Do players need to install this?

No. It only needs to be installed wherever BlueMap runs. On a dedicated server that is only the server. In singleplayer, BlueMap runs inside your own game, so install it there.

### Does this change how Domum blocks look in game?

No. The addon only provides files for BlueMap and doesn't touch the game's own resources.

---

## Development

### Building

```sh
./gradlew build
```

The jar is written to `build/libs/`. Use the `runClient` / `runServer` run configurations to test in a development environment together with BlueMap and Domum Ornamentum.

### How it works

BlueMap renders a block by looking up the block's blockstate file, which points to one or more models. Each variant in a blockstate can also name a `renderer`, which is the extension point this addon uses.

- **`MateriallyTexturedBlockEntityType` / `MateriallyTexturedBlockEntity`** read the block entity data Domum stores in the world, which holds the materials each block was crafted with.
- **`MateriallyTexturedBlockRendererType` / `MateriallyTexturedBlockRenderer`** register the `domum_ornamentum:materially_retexturable` renderer with BlueMap. It renders the block's model with the placeholder textures swapped for the textures of those materials.
- **`DomumBlockStateProvider`** (datagen) takes Domum's own blockstate files and adds `"renderer": "domum_ornamentum:materially_retexturable"` to every variant.
- **`BlueMapPackInstaller`** copies those generated blockstates into `config/bluemap/packs/domumornamentumbluemap` on startup.

#### Why a BlueMap pack instead of shipping the blockstates in `assets/`?

When BlueMap loads resources, the first file found for a given path wins. The order is:

1. `config/bluemap/packs/` (in reverse alphabetical order)
2. All jars in `mods/`, in whatever order the file system lists them
3. BlueMap's built-in resources and the vanilla client jar

Domum's jar and this addon would both contain `assets/domum_ornamentum/blockstates/*.json`. Which one wins in step 2 depends on the file system, so the addon would only work on some servers. By installing the blockstates as a pack in step 1, they always win.

The generated files are stored under `reports/bluemap/` in the jar instead of `assets/`. That folder is already a complete BlueMap pack, and the game never loads it, so these files can't affect anything outside of BlueMap.

`BlockRendererType#isFallbackFor` is not an alternative here: BlueMap only uses it for blocks that have no blockstate file at all, and Domum always ships its own.

### Regenerating the blockstates

When Domum Ornamentum is updated, its blockstates may change (for example, new blocks are added). Bump `domum_ornamentum_version` in `gradle.properties` and run:

```sh
./gradlew runData
```

This writes the blockstates to `src/generated/resources/reports/bluemap/assets/domum_ornamentum/blockstates/`. Commit the result.
