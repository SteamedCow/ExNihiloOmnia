package exnihiloomnia.items.buckets;

import exnihiloomnia.fluids.ENOFluids;
import exnihiloomnia.items.ENOItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

@SuppressWarnings("deprecation")
public class UniversalPorcelainBucket extends ItemFluidContainer {

//    private final int capacity; // how much the bucket holds
    private final ItemStack empty; // empty item to return and recognize when filling
    private final boolean nbtSensitive;

    public UniversalPorcelainBucket() {
        this(Fluid.BUCKET_VOLUME, new ItemStack(ENOItems.BUCKET_PORCELAIN_EMPTY), false);
    }

    /**
     * @param capacity        Capacity of the container
     * @param empty           Item used for filling with the bucket event and returned when emptied
     * @param nbtSensitive    Whether the empty item is NBT sensitive (usually true if empty and full are the same items)
     */
    public UniversalPorcelainBucket(int capacity, ItemStack empty, boolean nbtSensitive) {
        super(capacity);
//        this.capacity = capacity;
        this.empty = empty;
        this.nbtSensitive = nbtSensitive;

        this.setMaxStackSize(1);

        this.setCreativeTab(ENOItems.ENO_TAB);

        this.setRegistryName("porcelain_bucket");
        this.setUnlocalizedName("bucket_porcelain");

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance());

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return getEmpty() != null;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        if (getEmpty() != null) {
            // Create a copy such that the game can't mess with it
            return getEmpty().copy();
        }
        return super.getContainerItem(itemStack);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    @Override
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {

            if (fluid != FluidRegistry.WATER && fluid != FluidRegistry.LAVA && fluid != ENOFluids.WITCHWATER && !fluid.getName().equals("milk")) {
                // add all fluids that the bucket can be filled  with
                FluidStack fs = new FluidStack(fluid, getCapacity());
                ItemStack stack = new ItemStack(this);
                if (fill(stack, fs, true) == fs.amount)
                    subItems.add(stack);
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        FluidStack fluidStack = getFluid(stack);
        if (fluidStack == null) {
            if(getEmpty() != null)
                return getEmpty().getDisplayName();

            return super.getItemStackDisplayName(stack);
        }

        String unloc = this.getUnlocalizedNameInefficiently(stack);

        if (I18n.canTranslate(unloc + "." + fluidStack.getFluid().getName()))
            return I18n.translateToLocal(unloc + "." + fluidStack.getFluid().getName());

        return I18n.translateToLocalFormatted(unloc + ".name", fluidStack.getLocalizedName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        FluidStack fluidStack = getFluid(itemstack);
        // empty bucket shouldn't exist, do nothing since it should be handled by the bucket event
        if (fluidStack == null)
            return ActionResult.newResult(EnumActionResult.PASS, itemstack);

        // clicked on a block?
        RayTraceResult mop = this.rayTrace(world, player, false);

        if(mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK)
            return ActionResult.newResult(EnumActionResult.PASS, itemstack);

        BlockPos clickPos = mop.getBlockPos();
        // can we place liquid there?
        if (world.isBlockModifiable(player, clickPos)) {
            // the block adjacent to the side we clicked on
            BlockPos targetPos = clickPos.offset(mop.sideHit);

            // can the player place there?
            if (player.canPlayerEdit(targetPos, mop.sideHit, itemstack)) {
                // try placing liquid
                if (FluidUtil.tryPlaceFluid(player, player.getEntityWorld(), targetPos, itemstack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null), fluidStack)
                        && !player.capabilities.isCreativeMode) {
                    // success!
                    player.addStat(StatList.getObjectUseStats(this));

                    itemstack.setCount(itemstack.getCount() - 1);
                    ItemStack emptyStack = getEmpty() != null ? getEmpty().copy() : new ItemStack(this);

                    // check whether we replace the item or add the empty one to the inventory
                    if (itemstack.getCount() <= 0)
                        return ActionResult.newResult(EnumActionResult.SUCCESS, emptyStack);
                    else {
                        // add empty bucket to player inventory
                        ItemHandlerHelper.giveItemToPlayer(player, emptyStack);
                        return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
                    }
                }
            }
        }

        // couldn't place liquid there2
        return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
    }

    // compatibility
    @Deprecated
    public boolean tryPlaceFluid(Block block, World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        IFluidHandler fluidSource = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

        if (block instanceof IFluidBlock) {
            IFluidBlock fluidBlock = (IFluidBlock) block;

            return FluidUtil.tryPlaceFluid(null, worldIn, pos, fluidSource, new FluidStack(fluidBlock.getFluid(), Fluid.BUCKET_VOLUME));
        }
        else if (block.getDefaultState().getMaterial() == Material.WATER)
            FluidUtil.tryPlaceFluid(null, worldIn, pos, fluidSource, new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME));
        else if (block.getDefaultState().getMaterial() == Material.LAVA)
            FluidUtil.tryPlaceFluid(null, worldIn, pos, fluidSource, new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME));

        return false;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onFillBucket(FillBucketEvent event) {
        if (event.getResult() != Event.Result.DEFAULT)
            return;

        ItemStack emptyBucket = event.getEmptyBucket();
        if (emptyBucket == null ||
                !emptyBucket.isItemEqual(getEmpty()) ||
                (isNbtSensitive() && ItemStack.areItemStackTagsEqual(emptyBucket, getEmpty()))) {
            return;
        }

        RayTraceResult target = event.getTarget();
        if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK)
            return;

        World world = event.getWorld();
        BlockPos pos = target.getBlockPos();

        ItemStack singleBucket = emptyBucket.copy();
        singleBucket.setCount(1);

        FluidActionResult actionResult = FluidUtil.tryPickUpFluid(singleBucket, event.getEntityPlayer(), world, pos, target.sideHit);
        ItemStack filledBucket = actionResult.result;
        if (filledBucket != null) {
            event.setResult(Event.Result.ALLOW);
            event.setFilledBucket(filledBucket);
        }
        else
            event.setCanceled(true);

    }

    public static ItemStack getFilledBucket(UniversalPorcelainBucket item, Fluid fluid) {
        ItemStack stack = new ItemStack(item);
        item.fill(stack, new FluidStack(fluid, item.getCapacity()), true);
        return stack;
    }

  /* FluidContainer Management */
    public FluidStack getFluid(ItemStack container) {
        return FluidStack.loadFluidStackFromNBT(container.getTagCompound());
    }

    public int getCapacity(ItemStack container) {
        return getCapacity();
    }

    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        // has to be exactly 1, must be handled from the caller
        if (container.getCount() != 1)
            return 0;

        // can only fill exact capacity
        if (resource == null || resource.amount < getCapacity())
            return 0;

        // already contains fluid?
        if (getFluid(container) != null)
            return 0;

        // registered in the registry?
        if (FluidRegistry.getBucketFluids().contains(resource.getFluid())) {
            // fill the container
            if (doFill) {
                NBTTagCompound tag = container.getTagCompound();
                if (tag == null)
                    tag = new NBTTagCompound();

                resource.writeToNBT(tag);
                container.setTagCompound(tag);
            }

            return getCapacity();
        }
        else if (resource.getFluid() == FluidRegistry.WATER) {
            if (doFill)
                container.deserializeNBT(new ItemStack(ENOItems.BUCKET_PORCELAIN_WATER).serializeNBT());
            return getCapacity();
        }
        else if (resource.getFluid() == FluidRegistry.LAVA) {
            if (doFill)
                container.deserializeNBT(new ItemStack(ENOItems.BUCKET_PORCELAIN_LAVA).serializeNBT());
            return getCapacity();
        }
        else if (resource.getFluid() == ENOFluids.WITCHWATER) {
            if (doFill)
                container.deserializeNBT(new ItemStack(ENOItems.BUCKET_PORCELAIN_WITCHWATER).serializeNBT());
            return getCapacity();
        }

        return 0;
    }

    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        // has to be exactly 1, must be handled from the caller
        if (container.getCount() != 1)
            return null;

        // can only drain everything at once
        if (maxDrain < getCapacity(container))
            return null;

        FluidStack fluidStack = getFluid(container);

        if (doDrain && fluidStack != null) {
            if(getEmpty() != null)
                container.deserializeNBT(getEmpty().serializeNBT());
            else
                container.setCount(0);
        }

        return fluidStack;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public ItemStack getEmpty()
    {
        return empty;
    }

    public boolean isNbtSensitive()
    {
        return nbtSensitive;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new UniversalPorcelainBucketWrapper(stack);
    }
}
