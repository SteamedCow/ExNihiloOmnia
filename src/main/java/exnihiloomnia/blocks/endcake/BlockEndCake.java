package exnihiloomnia.blocks.endcake;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.helpers.InventoryHelper;
import net.minecraft.block.BlockCake;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockEndCake extends BlockCake {
    public BlockEndCake() {
        super();
        setCreativeTab(ENOItems.ENO_TAB);
        setSoundType(SoundType.CLOTH);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);

        if (heldItem != null) {
            if (heldItem.getItem() == Items.ENDER_EYE) {
                int i = state.getValue(BITES);

                if (i > 0) {
                    worldIn.setBlockState(pos, state.withProperty(BITES, i - 1), 3);
                    InventoryHelper.consumeItem(playerIn, heldItem);
                }
            }
        } else if ((playerIn.canEat(ENOConfig.end_cake_hunger)) || playerIn.capabilities.isCreativeMode) {
            playerIn.getFoodStats().addStats(2, 0.1F);
            int i = state.getValue(BITES);

            if (i < 6)
                worldIn.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
            else
                worldIn.setBlockToAir(pos);

            if (playerIn.dimension == 0)
                playerIn.changeDimension(1);
        }
        return true;
    }

    @NotNull
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ENOBlocks.END_CAKE);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        this.getDefaultState().withProperty(BITES, 6);
    }
}
