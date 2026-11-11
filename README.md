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

---

## Development

### Building

```sh
./gradlew build
```

The jar is written to `build/libs/`. Use the `runClient` / `runServer` run configurations to test in a development environment together with BlueMap and Domum Ornamentum.
