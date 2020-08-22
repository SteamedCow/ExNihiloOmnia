package exnihiloomnia.crafting.recipes;

import exnihiloomnia.ENO;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CrucibleRecipes {
	public static void register() {
		ResourceLocation group = new ResourceLocation(ENO.MODID);

		GameRegistry.addShapedRecipe(ENOBlocks.CRUCIBLE_RAW.getRegistryName(), group,
				new ItemStack(ENOBlocks.CRUCIBLE_RAW, 1),
				"x x",
				"x x",
				"xxx",
				'x', new ItemStack(ENOItems.PORCELAIN, 1));
		
		GameRegistry.addSmelting(ENOBlocks.CRUCIBLE_RAW, new ItemStack(ENOBlocks.CRUCIBLE), 0.0f);
	}
}
