package exnihiloomnia.blocks.crucibles;

import exnihiloomnia.blocks.crucibles.tileentity.TileEntityCrucible;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

public class BlockCrucible extends Block implements ITileEntityProvider {

	public BlockCrucible() {
		super(Material.CLAY);

		this.setSoundType(SoundType.STONE);
		this.setHardness(1.0f);
		this.setHarvestLevel("pickaxe", 0);
		this.setCreativeTab(ENOItems.ENO_TAB);
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityCrucible crucible = (TileEntityCrucible) world.getTileEntity(pos);
		ItemStack item = player.getHeldItem(hand);
		FluidBucketWrapper fluidWrapper = new FluidBucketWrapper(item);

		if (item != null && crucible != null) {
			if (item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
				FluidUtil.interactWithFluidHandler(player, hand, crucible.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null));
			} else if (fluidWrapper.getContainer().isEmpty()) {
				ItemStack full = fluidWrapper.getContainer();

				if (full != null) {
					if (player != null) {
						if (!player.capabilities.isCreativeMode) {
							int stackSize = item.getCount();
							if (stackSize > 1) {
								item.setCount(stackSize - 1);
								InventoryHelper.giveItemStackToPlayer(player, full);
							}
							else {
								player.setHeldItem(hand, full);
							}
						}

						crucible.getTank().drain(1000, true);
						return true;
					}
				}
			}

			ItemStack contents = item.copy();
			contents.setCount(1);

			if (crucible.canInsertItem(contents)) {
				crucible.getItemHandler().insertItem(0, contents, false);

				world.playSound(null, pos, SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, 0.5f, 1.0f);
				InventoryHelper.consumeItem(player, item);
				return true;
			}
		}

		//Return true to keep buckets from pouring all over the damn place.
		return true;
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);

		if (tile != null) {
			if (tile instanceof TileEntityCrucible) {
				TileEntityCrucible crucible = (TileEntityCrucible) tile;

				if (crucible.getFluid() != null) {

					if (crucible.getFluidFullness() > crucible.getSolidFullness()) {
						FluidStack fluid = crucible.getFluid();

						if (fluid != null && fluid.getFluid() != null) {
							return crucible.getFluid().getFluid().getLuminosity();
						}
					}
				}

				if (crucible.getLastItemAdded() != null) {

					if (crucible.getFluidFullness() < crucible.getSolidFullness()) {
						ItemStack item = crucible.getLastItemAdded();
						Block block = Block.getBlockFromItem(item.getItem());

						//noinspection deprecation,deprecation
						return block.getLightValue(block.getStateFromMeta(item.getMetadata()));
					}
				}
			}
		}

		return 0;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) { 
		return false; 
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCrucible();
	}
}
