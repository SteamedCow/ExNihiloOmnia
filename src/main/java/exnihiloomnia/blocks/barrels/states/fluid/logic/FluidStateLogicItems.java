package exnihiloomnia.blocks.barrels.states.fluid.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

public class FluidStateLogicItems extends BarrelLogic {
	private static final ResourceLocation wateringCan = new ResourceLocation("extrautils2", "WateringCan");

	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();
		FluidBucketWrapper fluidWrapper = new FluidBucketWrapper(item);
		FluidStack iFluid = fluidWrapper.getFluid();
		boolean canFill = fluidWrapper.canFillFluidType(fluid);

		return item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
				|| (fluid != null
				&& (iFluid != null && barrel.getFluidTank().fill(iFluid, false) > 0
				|| canFill && fluid.amount >= barrel.getFluidTank().getCapacity()))
				|| item.getItem().getRegistryName().equals(wateringCan);
	}

	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		FluidStack fluid = barrel.getFluid();
		FluidBucketWrapper fluidWrapper = new FluidBucketWrapper(item);
		FluidStack iFluid = fluidWrapper.getFluid();

		if (fluid != null ) {
			//watering can!
			if (item.getItem().getRegistryName().equals(wateringCan)) {
				if (fluid.amount > 0 && item.getItemDamage() > 0) {
					FluidStack out = barrel.getFluidTank().drain(item.getItemDamage(), true);
					if (out != null)
						item.setItemDamage(item.getItemDamage() - out.amount);
				}
			}

			if (item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
				FluidUtil.interactWithFluidHandler(player, hand, barrel.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null));
			} else if (fluidWrapper.getContainer().isEmpty() && fluid.amount >= barrel.getFluidTank().getCapacity()) {
				ItemStack full = fluidWrapper.getContainer();

				if (full != null) {
					if (player != null) {
						if (!player.capabilities.isCreativeMode) {
							int stackSize = item.getCount();
							if (stackSize > 1) {
								item.setCount(stackSize - 1);
								InventoryHelper.giveItemStackToPlayer(player, full);
							}
							else
								player.setHeldItem(hand, full);
						}
					}
					else {
						InventoryHelper.consumeItem(null, item);

						barrel.addOutput(full);
					}

					barrel.getFluidTank().drain(barrel.getFluidTank().getCapacity(), true);
				}
			}
			else if (iFluid != null && barrel.getFluidTank().fill(iFluid, false) > 0) {
				if (player != null) {
//					if (!player.capabilities.isCreativeMode) {
//						item.setItem(InventoryHelper.getContainer(item).getItem());
//					}
				} else {
					InventoryHelper.consumeItem(null, item);
					barrel.addOutput(InventoryHelper.getContainer(item));
				}
				
				barrel.getFluidTank().fill(iFluid, true);
			}
		}
		
		return false;
	}
}
