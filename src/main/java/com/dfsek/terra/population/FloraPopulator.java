package com.dfsek.terra.population;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import com.dfsek.terra.config.genconfig.biome.BiomeConfig;
import com.dfsek.terra.config.genconfig.biome.BiomeFloraConfig;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.world.Flora;

import java.util.Random;

/**
 * Populates Flora and Trees
 */
public class FloraPopulator extends GaeaBlockPopulator {
    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("FloraTime")) {
            TerraWorld tw = TerraWorld.getWorld(world);
            if(!tw.isSafe()) return;
            int originX = chunk.getX() << 4;
            int originZ = chunk.getZ() << 4;
            TerraBiomeGrid grid = tw.getGrid();
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z, GenerationPhase.POPULATE);
                    if(biome.getDecorator().getFloraChance() <= 0) continue;
                    try {
                        BiomeConfig c = biome.getConfig();
                        BiomeFloraConfig f = c.getFlora();
                        for(int i = 0; i < f.getFloraAttempts(); i++) {
                            Flora item;
                            if(f.isFloraSimplex())
                                item = biome.getDecorator().getFlora().get(f.getFloraNoise(), originX + x, originZ + z);
                            else item = biome.getDecorator().getFlora().get(random);
                            for(Block highest : item.getValidSpawnsAt(chunk, x, z, c.getFloraHeights(item))) {
                                if(random.nextInt(100) < biome.getDecorator().getFloraChance())
                                    item.plant(highest.getLocation());
                            }
                        }
                    } catch(NullPointerException ignore) {
                    }
                }
            }
        }
    }
}
