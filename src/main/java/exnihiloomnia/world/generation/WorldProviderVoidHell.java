package exnihiloomnia.world.generation;

import exnihiloomnia.world.ENOWorld;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderVoidHell extends WorldProviderHell {

	@Override
    public IChunkGenerator createChunkGenerator() {
		return new ChunkProviderVoidHell(this.world, ENOWorld.getNetherFortressesAllowed(), world.getSeed());
    }
}
