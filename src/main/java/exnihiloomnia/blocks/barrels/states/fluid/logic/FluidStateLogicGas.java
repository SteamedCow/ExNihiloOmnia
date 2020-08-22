package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidStateLogicGas extends BarrelLogic {
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel)  {
		if (barrel.getFluid() != null && barrel.getFluid().getFluid() != null) {
			//if the FLUID is gaseous...
			if (barrel.getFluid().getFluid().isGaseous()) {
				World world = barrel.getWorld();
				
				//and the space above the barrel is EMPTY...
				BlockPos pos = barrel.getPos();
				BlockPos above = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
				
				if(world.isAirBlock(above)) {
					//float free little cloud dude!
					Block fBlock = barrel.getFluid().getFluid().getBlock();
					IBlockState state = fBlock.getBlockState().getBaseState();

					world.setBlockState(above, fBlock.getDefaultState(), 3);
					world.notifyBlockUpdate(above, state, state, 2);
					
					barrel.getFluidTank().drain(barrel.getFluidTank().getCapacity(), true);
					
					return true;
				}
			}
		}
		
		return false;
	}
}
