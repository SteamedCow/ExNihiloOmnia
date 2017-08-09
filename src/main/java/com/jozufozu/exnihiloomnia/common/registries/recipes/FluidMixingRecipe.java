package com.jozufozu.exnihiloomnia.common.registries.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class FluidMixingRecipe extends IForgeRegistryEntry.Impl<FluidMixingRecipe>
{
    /**
     * What's in the barrel
     */
    private FluidStack lower;
    
    /**
     * What goes on the barrel
     */
    private FluidStack upper;
    
    private ItemStack output;
    
    public FluidStack getLower()
    {
        return lower;
    }
    
    public FluidStack getUpper()
    {
        return upper;
    }
    
    public ItemStack getOutput()
    {
        return output;
    }

    public static FluidMixingRecipe deserialize(JsonObject object) throws JsonParseException
    {
        if (!object.has(LibRegistries.OUTPUT_BLOCK))
        {
            throw new JsonSyntaxException("Fluid mixing recipe is missing output!");
        }
    
        FluidMixingRecipe out = new FluidMixingRecipe();
        
        out.lower = new FluidStack(JsonHelper.deserializeFluid(object, LibRegistries.IN_BARREL), 1000);
        out.upper = new FluidStack(JsonHelper.deserializeFluid(object, LibRegistries.ON_BARREL), 1000);
        
        out.output = JsonHelper.deserializeItem(object.getAsJsonObject(LibRegistries.OUTPUT_BLOCK), true);
        
        if (Block.getBlockFromItem(out.output.getItem()) == Blocks.AIR)
        {
            throw new JsonSyntaxException("Recipe does not output a block, ignoring");
        }
        
        return out;
    }
}
