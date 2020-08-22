package exnihiloomnia.world.generation;

import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderVoidEnd extends WorldProviderEnd {
    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderVoidEnd(world, world.getSeed());
    }
}
