package exnihiloomnia.crafting.recipes;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BucketRecipes {
	public static void register() {
		ResourceLocation group = new ResourceLocation(ENO.MODID);

		//PORCELAIN bucket
		GameRegistry.addShapedRecipe(ENOItems.BUCKET_PORCELAIN_RAW.getRegistryName(), group,
				new ItemStack(ENOItems.BUCKET_PORCELAIN_RAW, 1),
						"x x",
						" x ",
						'x', new ItemStack(ENOItems.PORCELAIN, 1));

		//PORCELAIN bucket smelting
		GameRegistry.addSmelting(new ItemStack(ENOItems.BUCKET_PORCELAIN_RAW, 1), new ItemStack(ENOItems.BUCKET_PORCELAIN_EMPTY, 1), 0);
	}
}
